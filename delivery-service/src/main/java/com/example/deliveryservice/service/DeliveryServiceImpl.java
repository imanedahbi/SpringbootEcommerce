package com.example.deliveryservice.service;

import com.example.deliveryservice.entity.Delivery;
import com.example.deliveryservice.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.deliveryservice.dto.DeliveryRequest;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;


    @Override
    public Delivery createDelivery(DeliveryRequest req) {
        Delivery delivery = Delivery.builder()
                .orderId(req.getOrderId())
                .address(req.getAddress())
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .status("EN_COURS")
                .estimatedDate(LocalDateTime.now().plusDays(2))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return deliveryRepository.save(delivery);
    }


    @Override
    public Delivery getDeliveryByOrderId(Long orderId) {
        return deliveryRepository.findByOrderId(orderId);
    }
}
