package com.laundry.dto;

import lombok.Data;

@Data
public class OrderItemDTO {

    private String clothType;
    private Integer quantity;
    private Double price;

    public OrderItemDTO() {}

    public OrderItemDTO(String clothType, Integer quantity, Double price) {
        this.clothType = clothType;
        this.quantity = quantity;
        this.price = price;
    }
}