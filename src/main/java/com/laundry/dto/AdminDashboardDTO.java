package com.laundry.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class AdminDashboardDTO {

    private long totalOrders;
    private double totalRevenue;
    private long totalShops;
    private long expiredShops;
    private long totalUsers;
}