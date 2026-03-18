package com.laundry.controller;

import com.laundry.dto.ApiResponse;
import com.laundry.dto.OrderItemDTO;
import com.laundry.dto.OrderResponseDTO;
import com.laundry.entity.Order;
import com.laundry.entity.User;
import com.laundry.exception.ResourceNotFoundException;
import com.laundry.model.OrderStatus;
import com.laundry.repository.UserRepository;
import com.laundry.service.OrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ShopController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    // Shop orders
    @GetMapping("/orders")
    public ResponseEntity<?> getShopOrders(Authentication authentication) {

        User shopUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Shop user not found"));

        List<OrderResponseDTO> response = orderService
                .getOrdersByShop(shopUser.getShop())
                .stream()
                .map(order -> new OrderResponseDTO(
                        order.getId(),
                        order.getPickupAddress(),
                        order.getPickupDate(),
                        order.getPickupTime(),
                        order.getStatus(),
                        null,
                        order.getItems().stream()
                                .map(item -> new OrderItemDTO(
                                        item.getClothType(),
                                        item.getQuantity(),
                                        item.getPrice()
                                ))
                                .toList()
                ))
                .toList();

        return ResponseEntity.ok(
                new ApiResponse<>("Shop orders fetched", response)
        );
    }

    // Update washing status
    @PutMapping("/update-status/{orderId}")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status,
            Authentication authentication) {

        User shopUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Shop user not found"));

        Order order = orderService.getOrderById(orderId);

        // 🔒 Security check
        if (!order.getShop().getId().equals(shopUser.getShop().getId())) {
            return ResponseEntity.status(403)
                    .body("You are not allowed to update this order");
        }

        OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());

        order.setStatus(newStatus);

        orderService.save(order);

        return ResponseEntity.ok(
                new ApiResponse<>("Order status updated", newStatus)
        );
    }
}
