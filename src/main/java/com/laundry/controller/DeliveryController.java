package com.laundry.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.laundry.dto.ApiResponse;
import com.laundry.dto.OrderItemDTO;
import com.laundry.dto.OrderResponseDTO;
import com.laundry.entity.Order;
import com.laundry.entity.User;
import com.laundry.exception.ResourceNotFoundException;
import com.laundry.model.OrderStatus;
import com.laundry.repository.UserRepository;
import com.laundry.service.OrderService;
import com.laundry.service.WalletService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final OrderService orderService;
    private final UserRepository userRepository;
    private final WalletService walletService;   

    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyAssignedOrders(Authentication authentication) {

        User deliveryBoy = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<OrderResponseDTO> response = orderService
                .getOrdersByDeliveryBoy(deliveryBoy)
                .stream()
                .map(order -> new OrderResponseDTO(
                        order.getId(),
                        order.getPickupAddress(),
                        order.getPickupDate(),
                        order.getPickupTime(),
                        order.getStatus(),
                        deliveryBoy.getName(),
                        order.getItems() != null
                                ? order.getItems().stream()
                                .map(item -> new OrderItemDTO(
                                        item.getClothType(),
                                        item.getQuantity(),
                                        item.getPrice()
                                ))
                                .toList()
                                : List.of()
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

        User deliveryBoy = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Order order = orderService.getOrderById(orderId);

        if (order.getDeliveryBoy() == null ||
                !order.getDeliveryBoy().getId().equals(deliveryBoy.getId())) {
            return ResponseEntity.status(403).body("You are not assigned to this order");
        }

        OrderStatus newStatus;

        try {
            newStatus = OrderStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid status");
        }

        if (!order.getStatus().canMoveTo(newStatus)) {
            return ResponseEntity.badRequest().body("Invalid status transition");
        }
        
        

        order.setStatus(newStatus);
        // 🔥 Wallet Earnings Logic
        if (newStatus == OrderStatus.DELIVERED) {

            walletService.addMoney(order.getShop().getOwner(), 150.0);

            walletService.addMoney(order.getDeliveryBoy(), 20.0);
        }
        orderService.save(order);

        return ResponseEntity.ok(
                new ApiResponse<>("Order status updated", order.getStatus())
        );
    }
    
    //Delivery boy location marker
    
    @PutMapping("/update-location")
    public ResponseEntity<?> updateLocation(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            Authentication authentication){

        User deliveryBoy = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        deliveryBoy.setLatitude(latitude);
        deliveryBoy.setLongitude(longitude);

        userRepository.save(deliveryBoy);

        return ResponseEntity.ok("Location updated");

    }
}
