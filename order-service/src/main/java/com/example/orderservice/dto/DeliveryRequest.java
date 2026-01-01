package com.example.orderservice.dto;

import lombok.Data;

@Data
public class DeliveryRequest {
    private Long orderId;
    private Long userId;

    // Champs nécessaires pour le delivery-service
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
}
