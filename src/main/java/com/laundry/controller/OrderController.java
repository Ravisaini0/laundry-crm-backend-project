package com.laundry.controller;

import com.laundry.dto.ApiResponse;
import com.laundry.dto.OrderResponseDTO;
import com.laundry.entity.Order;
import com.laundry.entity.User;
import com.laundry.exception.ResourceNotFoundException;
import com.laundry.model.OrderStatus;
import com.laundry.repository.OrderRepository;
import com.laundry.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order,
                                         Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        order.setUser(user);
        order.setStatus(OrderStatus.REQUESTED);
        order.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order);

        OrderResponseDTO responseDTO = new OrderResponseDTO(
                order.getId(),
                order.getPickupAddress(),
                order.getPickupDate(),
                order.getPickupTime(),
                order.getStatus(),
                null
        );

        return ResponseEntity.ok(
                new ApiResponse<>("Order created successfully", responseDTO)
        );

    }

    @GetMapping
    public ResponseEntity<?> getMyOrders(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Order> orders = orderRepository.findByUser(user);

        List<OrderResponseDTO> response = orders.stream()
                .map(order -> new OrderResponseDTO(
                        order.getId(),
                        order.getPickupAddress(),
                        order.getPickupDate(),
                        order.getPickupTime(),
                        order.getStatus(),
                        order.getDeliveryBoy() != null ?
                                order.getDeliveryBoy().getName() : null
                ))
                .toList();

        return ResponseEntity.ok(
                new ApiResponse<>("Orders fetched successfully", response)
        );
    }

}
