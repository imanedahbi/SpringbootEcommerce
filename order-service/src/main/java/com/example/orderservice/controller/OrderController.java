package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDTO;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.entity.Order;
import com.example.orderservice.service.CartService;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;  // ✅ Ajouter
    private final CartService cartService;          // ✅ Ajouter


    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        System.out.println("OrderRequest reçu : " + request);
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/confirm-delivery")
    public ResponseEntity<?> confirmDelivery(@RequestParam Long orderId, @RequestParam Long userId) {

        System.out.println("confirmDelivery called with orderId=" + orderId + " userId=" + userId);

        Order order = orderService.getOrderByIdEntity(orderId);
        order.setStatus("CONFIRMED");
        orderRepository.save(order);

        // Vider le panier côté serveur
        cartService.clearCart(userId); // ✅ transactionnelle
        System.out.println("Cart cleared for userId=" + userId);

        return ResponseEntity.ok(order);
    }


}
