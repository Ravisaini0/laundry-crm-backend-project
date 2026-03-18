package com.laundry.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laundry.entity.User;
import com.laundry.model.UserRole;

public interface UserRepository extends JpaRepository <User,Long>{
	 boolean existsByEmail(String email);
	 Optional<User> findByEmail(String email);
	 long countByRole(com.laundry.model.UserRole role);
	 List<User> findByRole(UserRole role);
	 List<User> findByRoleAndAvailableTrue(String role);
}
