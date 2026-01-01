package com.example.deliveryservice.service;

import com.example.deliveryservice.dto.DeliveryRequest;
import com.example.deliveryservice.entity.Delivery;

public interface DeliveryService {
    Delivery createDelivery(DeliveryRequest req);
    Delivery getDeliveryByOrderId(Long orderId);
}
