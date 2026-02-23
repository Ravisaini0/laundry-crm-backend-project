package com.laundry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laundry.entity.Order;
import com.laundry.entity.User;

public interface OrderRepository extends JpaRepository<Order,Long>{

    List<Order> findByUser(User user);
    List<Order> findByDeliveryBoy(User deliveryBoy);

}
