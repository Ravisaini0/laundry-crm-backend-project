
package com.laundry.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.laundry.model.OrderStatus;
import jakarta.validation.constraints.*;



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
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
    
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private LaundryShop shop;
    
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @PrePersist
    public void setDefaultValues() {
        if (this.status == null) {
            this.status = OrderStatus.REQUESTED;
        }
        this.createdAt = LocalDateTime.now();
    }
}
