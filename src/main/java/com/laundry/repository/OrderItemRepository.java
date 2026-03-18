package com.laundry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laundry.entity.Order;
import com.laundry.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);
}