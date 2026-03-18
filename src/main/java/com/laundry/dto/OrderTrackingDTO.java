package com.laundry.dto;

import com.laundry.model.OrderStatus;
import java.time.LocalDate;
import java.time.LocalTime;

public class OrderTrackingDTO {

    private Long orderId;
    private OrderStatus status;
    private String deliveryBoy;
    private LocalDate pickupDate;
    private LocalTime pickupTime;

    public OrderTrackingDTO(Long orderId,
                            OrderStatus status,
                            String deliveryBoy,
                            LocalDate pickupDate,
                            LocalTime pickupTime) {

        this.orderId = orderId;
        this.status = status;
        this.deliveryBoy = deliveryBoy;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
    }

    public Long getOrderId() { return orderId; }
    public OrderStatus getStatus() { return status; }
    public String getDeliveryBoy() { return deliveryBoy; }
    public LocalDate getPickupDate() { return pickupDate; }
    public LocalTime getPickupTime() { return pickupTime; }
}