package com.example.deliveryservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private String status;
    private String address;
    private LocalDateTime estimatedDate;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
