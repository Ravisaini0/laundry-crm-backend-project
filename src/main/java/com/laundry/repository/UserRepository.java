package com.laundry.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laundry.entity.User;

public interface UserRepository extends JpaRepository <User,Long>{
	 boolean existsByEmail(String email);
	 Optional<User> findByEmail(String email);
}
