package com.example.deliveryservice.dto;

import lombok.Data;

@Data
public class DeliveryRequest {
    private Long orderId;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
}
