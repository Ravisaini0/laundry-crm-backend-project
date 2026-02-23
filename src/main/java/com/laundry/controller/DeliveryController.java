package com.laundry.controller;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.laundry.dto.ApiResponse;
import com.laundry.dto.OrderResponseDTO;
import com.laundry.entity.Order;
import com.laundry.entity.User;
import com.laundry.exception.ResourceNotFoundException;
import com.laundry.model.OrderStatus;
import com.laundry.repository.OrderRepository;
import com.laundry.repository.UserRepository;


@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public DeliveryController(OrderRepository orderRepository,
                              UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

//    @GetMapping("/my-orders")
//    public ResponseEntity<?> getMyAssignedOrders(Authentication authentication) {
//
//        String email = authentication.getName();
//
//        User deliveryBoy = userRepository.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        List<Order> orders = orderRepository.findByDeliveryBoy(deliveryBoy);
//
//        return ResponseEntity.ok(orders);
//    }
    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyAssignedOrders(Authentication authentication) {

        String email = authentication.getName();

        User deliveryBoy = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Order> orders = orderRepository.findByDeliveryBoy(deliveryBoy);

        List<OrderResponseDTO> response = orders.stream()
                .map(order -> new OrderResponseDTO(
                        order.getId(),
                        order.getPickupAddress(),
                        order.getPickupDate(),
                        order.getPickupTime(),
                        order.getStatus(),
                        deliveryBoy.getName()
                ))
                .toList();

        return ResponseEntity.ok(
                new ApiResponse<>("Assigned orders fetched successfully", response)
        );
    }
    @PutMapping("/update-status/{orderId}")
    public ResponseEntity<?> updateOrderStatus(
    		
            @PathVariable Long orderId,
            @RequestParam String status,
            Authentication authentication) {

        String email = authentication.getName();

        User deliveryBoy = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Ensure order belongs to this delivery boy
        if (order.getDeliveryBoy() == null ||
            !order.getDeliveryBoy().getId().equals(deliveryBoy.getId())) {
            return ResponseEntity.status(403).body("You are not assigned to this order");
        }

        OrderStatus newStatus;

        try {
        	newStatus = OrderStatus.valueOf(status.toUpperCase());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status value");
        }

        // Allowed transitions
        OrderStatus currentStatus = order.getStatus();

        if (currentStatus == OrderStatus.ASSIGNED && newStatus == OrderStatus.PICKED ||
            currentStatus == OrderStatus.PICKED && newStatus == OrderStatus.OUT_FOR_DELIVERY ||
            currentStatus == OrderStatus.OUT_FOR_DELIVERY && newStatus == OrderStatus.DELIVERED) {

            order.setStatus(newStatus);
            orderRepository.save(order);
//            return ResponseEntity.ok("Status updated successfully");
            order.setStatus(newStatus);
            orderRepository.save(order);

            OrderResponseDTO responseDTO = new OrderResponseDTO(
                    order.getId(),
                    order.getPickupAddress(),
                    order.getPickupDate(),
                    order.getPickupTime(),
                    order.getStatus(),
                    deliveryBoy.getName()
            );

            return ResponseEntity.ok(
                    new ApiResponse<>("Order status updated successfully", responseDTO)
            );

        }

        return ResponseEntity.badRequest().body("Invalid status transition");
    }

}