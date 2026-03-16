package com.coplaca.apirest.entity;

public enum OrderStatus {
    PENDING,        // Waiting for confirmation
    CONFIRMED,      // Confirmed by customer
    ASSIGNED,       // Assigned to delivery agent
    ACCEPTED,       // Accepted by the delivery agent
    IN_TRANSIT,     // On the way
    DELIVERED,      // Delivered successfully
    CANCELLED       // Order cancelled
}
