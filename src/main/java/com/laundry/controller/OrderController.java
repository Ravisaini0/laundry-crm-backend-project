package com.laundry.controller;

import com.laundry.dto.ApiResponse;
import com.laundry.dto.OrderItemDTO;
import com.laundry.dto.OrderRequestDTO;
import com.laundry.dto.OrderResponseDTO;
import com.laundry.dto.OrderTrackingDTO;
import com.laundry.entity.LaundryShop;
import com.laundry.entity.Order;
import com.laundry.entity.OrderItem;
import com.laundry.entity.User;
import com.laundry.exception.ResourceNotFoundException;
import com.laundry.model.OrderStatus;
//import com.laundry.repository.LaundryShopRepository;
import com.laundry.repository.OrderItemRepository;
import com.laundry.repository.UserRepository;
import com.laundry.service.OrderService;
import com.laundry.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final OrderItemRepository orderItemRepository;
  //  private final LaundryShopRepository laundryShopRepository;

    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequestDTO request,
                                         Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

     // find nearest shop
        LaundryShop shop = orderService.findNearestShop(
                request.getLatitude(),
                request.getLongitude()
        );

        if (shop == null) {
            return ResponseEntity
                    .status(404)
                    .body("No nearby laundry shop found");
        }

        // find nearest delivery boy
        User deliveryBoy = orderService.findNearestDeliveryBoy(
                request.getLatitude(),
                request.getLongitude()
        );

        Order order = new Order();

        order.setPickupAddress(request.getPickupAddress());
        order.setPickupDate(request.getPickupDate());
        order.setPickupTime(request.getPickupTime());
        order.setUser(user);
        order.setShop(shop);
        order.setStatus(OrderStatus.REQUESTED);
        order.setCreatedAt(LocalDateTime.now());

        if(deliveryBoy != null){

            order.setDeliveryBoy(deliveryBoy);

            order.setStatus(OrderStatus.ASSIGNED);

            deliveryBoy.setAvailable(false);

            userRepository.save(deliveryBoy);

        }

        Order savedOrder = orderService.save(order);

        double totalAmount = 0;

        for (OrderItemDTO itemDTO : request.getItems()) {

            OrderItem item = OrderItem.builder()
                    .clothType(itemDTO.getClothType())
                    .quantity(itemDTO.getQuantity())
                    .price(itemDTO.getPrice())
                    .order(savedOrder)
                    .build();

            orderItemRepository.save(item);

            totalAmount += itemDTO.getPrice() * itemDTO.getQuantity();
        }

        paymentService.createPayment(totalAmount, savedOrder);

        return ResponseEntity.ok(
                new ApiResponse<>("Order created successfully", savedOrder.getId())
        );
    }

    @GetMapping
    public ResponseEntity<?> getMyOrders(Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Order> orders = orderService.getOrdersByUser(user);

        List<OrderResponseDTO> response = orders.stream()
                .map(order -> new OrderResponseDTO(
                        order.getId(),
                        order.getPickupAddress(),
                        order.getPickupDate(),
                        order.getPickupTime(),
                        order.getStatus(),
                        order.getDeliveryBoy() != null
                                ? order.getDeliveryBoy().getName()
                                : null,
                        order.getItems() != null
                                ? order.getItems().stream()
                                .map(item -> new OrderItemDTO(
                                        item.getClothType(),
                                        item.getQuantity(),
                                        item.getPrice()
                                ))
                                .collect(Collectors.toList())
                                : List.of()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new ApiResponse<>("Orders fetched successfully", response)
        );
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id,
                                         Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = orderService.getOrderById(id);

        if (!order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Not your order");
        }

        if (order.getStatus() != OrderStatus.REQUESTED) {
            return ResponseEntity.badRequest()
                    .body("Only requested orders can be cancelled");
        }

        paymentService.refundPayment(order);

        order.setStatus(OrderStatus.CANCELLED);
        orderService.save(order);

        return ResponseEntity.ok("Order cancelled & refunded");
    }
    @GetMapping("/track/{orderId}")
    public ResponseEntity<?> trackOrder(@PathVariable Long orderId) {

        Order order = orderService.getOrderById(orderId);

        OrderTrackingDTO tracking = new OrderTrackingDTO(
                order.getId(),
                order.getStatus(),
                order.getDeliveryBoy() != null ?
                        order.getDeliveryBoy().getName() : null,
                order.getPickupDate(),
                order.getPickupTime()
        );

        return ResponseEntity.ok(
                new ApiResponse<>("Order tracking fetched", tracking)
        );
    }
}