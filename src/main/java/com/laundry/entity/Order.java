//package com.laundry.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDate;
//
//@Entity
//@Table(name = "orders")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Order {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String pickupAddress;
//    private LocalDate pickupDate;
//    private String pickupTime;
//    private String status;
//
//    private String userEmail;
//
//    // getters & setters
//}
package com.laundry.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.laundry.model.OrderStatus;
import jakarta.validation.constraints.*;


//@Entity
//@Table(name = "orders")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Order {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "pickup_address", nullable = false)
//    private String pickupAddress;
//
//    @Column(name = "pickup_date", nullable = false)
//    private LocalDate pickupDate;
//
//    @Column(name = "pickup_time", nullable = false)
//    private String pickupTime;
//
//    @Enumerated(EnumType.STRING)
//    private OrderStatus status;
//
//
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    // 🔥 IMPORTANT RELATION
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//}
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pickup_address", nullable = false)
    @NotBlank(message = "Pickup address is required")
    private String pickupAddress;

    @Column(name = "pickup_date", nullable = false)
    @NotNull(message = "Pickup date is required")
    private LocalDate pickupDate;

    @Column(name = "pickup_time", nullable = false)
    @NotNull(message = "Pickup time is required")
    private LocalTime pickupTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "delivery_boy_id")
    private User deliveryBoy;

    @PrePersist
    public void setDefaultValues() {
        if (this.status == null) {
            this.status = OrderStatus.REQUESTED;
        }
        this.createdAt = LocalDateTime.now();
    }
}
