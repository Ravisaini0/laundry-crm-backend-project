package com.laundry.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class OrderRequestDTO {

    private String pickupAddress;
    private LocalDate pickupDate;
    private LocalTime pickupTime;

    private List<OrderItemDTO> items;
    private Double latitude;
    private Double longitude;
}
