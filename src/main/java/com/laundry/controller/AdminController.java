package com.laundry.controller;

import com.laundry.entity.Order;
import com.laundry.entity.User;
import com.laundry.exception.ResourceNotFoundException;
import com.laundry.model.OrderStatus;
import com.laundry.model.UserRole;
import com.laundry.repository.OrderRepository;
import com.laundry.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @PutMapping("/orders/{id}")
    public Order updateStatus(@PathVariable Long id,
                              @RequestParam String status) {

        Order order = orderRepository.findById(id)
                .orElseThrow();

        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        order.setStatus(orderStatus);

        return orderRepository.save(order);
    }
    @PutMapping("/assign/{orderId}/{deliveryId}")
    public ResponseEntity<?> assignOrder(
            @PathVariable Long orderId,
            @PathVariable Long deliveryId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        User deliveryBoy = userRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery user not found"));

        if (deliveryBoy.getRole() != UserRole.DELIVERY) {
            return ResponseEntity.badRequest().body("User is not a delivery boy");
        }

        order.setDeliveryBoy(deliveryBoy);
        order.setStatus(OrderStatus.ASSIGNED);

        orderRepository.save(order);

        return ResponseEntity.ok("Order assigned successfully");
    }

}
