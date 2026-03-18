package com.laundry.service;

import com.laundry.entity.Order;
import com.laundry.entity.Payment;
import com.laundry.model.PaymentStatus;
import com.laundry.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment createPayment(Double amount, com.laundry.entity.Order order) {

        Payment payment = Payment.builder()
                .amount(amount)
                .status(PaymentStatus.PENDING)  // For now simulate success
                .transactionId(UUID.randomUUID().toString())
                .paymentDate(LocalDateTime.now())
                .order(order)
                .build();

        return paymentRepository.save(payment);
    }
    public void refundPayment(Order order) {

        Payment payment = paymentRepository.findByOrder(order);

        if (payment == null) {
            throw new RuntimeException("Payment not found for this order");
        }

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new RuntimeException("Refund allowed only for SUCCESS payments");
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);
    }
}