package com.laundry.model;
 
public enum OrderStatus {

    REQUESTED,
    CONFIRMED,
    ASSIGNED,
    PICKED,
    WASHING,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED;

    public boolean canMoveTo(OrderStatus next) {

        return switch (this) {

            case REQUESTED ->
                    next == CONFIRMED || next == CANCELLED;

            case CONFIRMED ->
                    next == ASSIGNED || next == CANCELLED;

            case ASSIGNED ->
                    next == PICKED;

            case PICKED ->
                    next == WASHING;

            case WASHING ->
                    next == OUT_FOR_DELIVERY;

            case OUT_FOR_DELIVERY ->
                    next == DELIVERED;

            default -> false;
        };
    }

}