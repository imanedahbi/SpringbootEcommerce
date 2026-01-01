package com.example.deliveryservice.controller;

import com.example.deliveryservice.dto.DeliveryRequest;
import com.example.deliveryservice.entity.Delivery;
import com.example.deliveryservice.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5500"})
 // Autoriser le front à appeler l'API
@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/create")
    public ResponseEntity<Delivery> createDelivery(@RequestBody DeliveryRequest req) {
        return ResponseEntity.ok(deliveryService.createDelivery(req));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Delivery> getDelivery(@PathVariable Long orderId) {
        Delivery delivery = deliveryService.getDeliveryByOrderId(orderId);
        // ⚠️ Gestion si aucune livraison n'est trouvée
        if (delivery == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(delivery);
    }
}
