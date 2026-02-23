package com.laundry.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.laundry.dto.LoginRequest;
import com.laundry.entity.User;
import com.laundry.exception.ResourceNotFoundException;
import com.laundry.model.UserRole;
import com.laundry.repository.UserRepository;
import com.laundry.security.JwtUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@Valid @RequestBody User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already registered!";
        }

        user.setRole(UserRole.USER);
        
     // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        
        userRepository.save(user);

        return "User registered successfully!";
    }
//    @PostMapping("/login")
//    public String login(@RequestBody User loginUser) {
//
//        User user = userRepository.findByEmail(loginUser.getEmail())
//                .orElse(null);
//
//        if (user == null) {
//            return "User not found!";
//        }
//
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        if (!encoder.matches(loginUser.getPassword(), user.getPassword())) {
//            return "Invalid password!";
//        }
//
//        // TODO: Generate JWT token here
//
//        // ✅ JWT generate
//        String token = JwtUtil.generateToken(user.getEmail());
//
//        return token;
////        return "Login successful!";
//        
//    }
    
    
 

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Invalid password");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }


}
