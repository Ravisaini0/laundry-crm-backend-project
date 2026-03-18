package com.laundry.controller;

import com.laundry.entity.Order;
import com.laundry.entity.Payment;
import com.laundry.model.OrderStatus;
import com.laundry.model.PaymentStatus;
import com.laundry.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentRepository paymentRepository;

    @PutMapping("/complete/{paymentId}")
    @Transactional
    public ResponseEntity<?> completePayment(@PathVariable Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            return ResponseEntity.badRequest()
                    .body("Payment already processed");
        }

        // 1️⃣ Payment SUCCESS
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now());

        // 2️⃣ Order CONFIRMED
        Order order = payment.getOrder();
        order.setStatus(OrderStatus.CONFIRMED);

        paymentRepository.save(payment);

        return ResponseEntity.ok("Payment successful & Order confirmed");
    }
    }