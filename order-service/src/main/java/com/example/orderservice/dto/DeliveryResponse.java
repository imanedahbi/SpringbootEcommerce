package com.example.orderservice.dto;

import lombok.Data;

@Data
public class DeliveryResponse {
    private Long orderId;  // correspond à l’ID de la commande
    private String status; // par exemple "CREATED"
    private String address;
}
