package com.example.deliveryservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeliveryDTO {
    private Long orderId;
    private String status;
    private String address;
    private LocalDateTime estimatedDate;
}
