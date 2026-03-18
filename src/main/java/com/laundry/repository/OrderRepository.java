package com.laundry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laundry.entity.LaundryShop;
import com.laundry.entity.Order;
import com.laundry.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OrderRepository extends JpaRepository<Order,Long>{

    List<Order> findByUser(User user);
    List<Order> findByDeliveryBoy(User deliveryBoy);
    List<Order> findByShop(LaundryShop shop);
    Page<Order> findByShop(LaundryShop shop, Pageable pageable);
}
