package com.ProductOrder.Events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponseEvent {
    private Long orderId;
    private Long productId;
    private String status; // "AVAILABLE" or "FAILURE"
    private String failureReason;
    private double price;
}