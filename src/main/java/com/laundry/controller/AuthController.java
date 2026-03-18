package com.laundry.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.laundry.dto.LoginRequest;
import com.laundry.entity.LaundryShop;
import com.laundry.entity.User;
import com.laundry.exception.ResourceNotFoundException;
import com.laundry.model.UserRole;
import com.laundry.repository.LaundryShopRepository;
import com.laundry.repository.UserRepository;
import com.laundry.security.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final LaundryShopRepository laundryShopRepository;
	private final JwtUtil jwtUtil;
 
    
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody User user,
            @RequestParam Long shopId) {

        if (userRepository.existsByEmail(user.getEmail())) {
        	return ResponseEntity.ok("Email already registered!");
        }

        LaundryShop shop = laundryShopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found"));

        user.setShop(shop);
        user.setRole(UserRole.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/me")
    public User getCurrentUser(Authentication authentication) {
        return userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    
 


    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        	throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }


}
