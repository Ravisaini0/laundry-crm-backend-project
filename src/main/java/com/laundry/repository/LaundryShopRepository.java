package com.laundry.repository;



import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.laundry.entity.LaundryShop;

public interface LaundryShopRepository extends JpaRepository<LaundryShop, Long> {
	long countByActiveTrue();
	long countBySubscriptionExpiryBefore(java.time.LocalDate date);
	LaundryShop findFirstByActiveTrueAndSubscriptionExpiryAfter(LocalDate date);
	List<LaundryShop> findByActiveTrue();
}