package com.laundry.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import com.laundry.model.SubscriptionPlan;

@Entity
@Table(name = "laundry_shops")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaundryShop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;

    private String phone;

    @Column(unique = true)
    private String email;
    @ManyToOne
    @JoinColumn(name="owner_id")
    private User owner;
    @Column
    private Double latitude;

    @Column
    private Double longitude;

    private boolean active = true;
    
    @Enumerated(EnumType.STRING)
    private SubscriptionPlan plan;

    private LocalDate subscriptionExpiry;
}