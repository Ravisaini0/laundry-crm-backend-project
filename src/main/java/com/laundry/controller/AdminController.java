package com.laundry.controller;

import com.laundry.dto.AdminDashboardDTO;
import com.laundry.entity.LaundryShop;
import com.laundry.entity.Order;
import com.laundry.entity.User;
import com.laundry.exception.ResourceNotFoundException;
import com.laundry.model.OrderStatus;
import com.laundry.model.UserRole;
import com.laundry.repository.LaundryShopRepository;
import com.laundry.repository.OrderRepository;
import com.laundry.repository.PaymentRepository;
import com.laundry.repository.UserRepository;

import lombok.RequiredArgsConstructor;


import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final LaundryShopRepository laundryShopRepository;
    
    @GetMapping("/orders")
    public org.springframework.data.domain.Page<Order> getOrdersByShop(
            org.springframework.data.domain.Pageable pageable,
            Authentication authentication) {

        User admin = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return orderRepository.findByShop(admin.getShop(), pageable);
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
    @PostMapping("/create-shop")
    public LaundryShop createShop(@RequestBody LaundryShop shop) {
        return laundryShopRepository.save(shop);
    }

    @GetMapping("/dashboard")
    public AdminDashboardDTO getDashboard() {

        long totalOrders = orderRepository.count();

        Double revenue = paymentRepository.getTotalRevenueByStatus(
                com.laundry.model.PaymentStatus.SUCCESS);

        if (revenue == null) revenue = 0.0;

        long totalShops = laundryShopRepository.count();

        long expiredShops =
                laundryShopRepository.countBySubscriptionExpiryBefore(
                        java.time.LocalDate.now());

        long totalUsers =
                userRepository.countByRole(
                        com.laundry.model.UserRole.USER);

        return new AdminDashboardDTO(
                totalOrders,
                revenue,
                totalShops,
                expiredShops,
                totalUsers
        );
    }
}
