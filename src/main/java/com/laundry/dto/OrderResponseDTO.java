package com.laundry.dto;

import com.laundry.model.OrderStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class OrderResponseDTO {

    private Long id;
    private String pickupAddress;
    private LocalDate pickupDate;
    private LocalTime pickupTime;
    private OrderStatus status;
    private String deliveryBoyName;
    private List<OrderItemDTO> items;

    public OrderResponseDTO(Long id,
                            String pickupAddress,
                            LocalDate pickupDate,
                            LocalTime pickupTime,
                            OrderStatus status,
                            String deliveryBoyName,
                            List<OrderItemDTO> items) {

        this.id = id;
        this.pickupAddress = pickupAddress;
        this.pickupDate = pickupDate;
        this.pickupTime = pickupTime;
        this.status = status;
        this.deliveryBoyName = deliveryBoyName;
        this.items = items;
    }

    public Long getId() { return id; }
    public String getPickupAddress() { return pickupAddress; }
    public LocalDate getPickupDate() { return pickupDate; }
    public LocalTime getPickupTime() { return pickupTime; }
    public OrderStatus getStatus() { return status; }
    public String getDeliveryBoyName() { return deliveryBoyName; }
    public List<OrderItemDTO> getItems() { return items; }
}