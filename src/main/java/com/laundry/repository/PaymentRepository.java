package com.laundry.repository;

import com.laundry.entity.Order;
import com.laundry.entity.Payment;
import com.laundry.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = :status")
    Double getTotalRevenueByStatus(PaymentStatus status);
    Payment findByOrder(Order order);
}