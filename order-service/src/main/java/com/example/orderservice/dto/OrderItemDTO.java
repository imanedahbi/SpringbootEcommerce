package com.example.orderservice.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long productId;
    private String productName; // <-- ici le nom du produit
    private Integer quantity;
    private Double price;
}
