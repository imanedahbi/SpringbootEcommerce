package com.example.orderservice.service;

import com.example.orderservice.dto.*;
import com.example.orderservice.entity.CartItem;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderItem;
import com.example.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String PRODUCT_SERVICE_URL = "http://localhost:8083/products";



    private final CartService cartService;

    @Override
    @Transactional
    public Order createOrder(OrderRequest request) {
        List<CartItem> cartItems = cartService.getCart(request.getUserId());
        if(cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Le panier est vide !");
        }

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setStatus("PENDING"); // commande créée mais pas encore confirmée
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        AtomicReference<Double> total = new AtomicReference<>(0.0);

        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    ProductDTO product = restTemplate.getForObject(
                            PRODUCT_SERVICE_URL + "/" + cartItem.getProductId(),
                            ProductDTO.class
                    );
                    if(product == null || product.getPrice() == null){
                        throw new RuntimeException("Produit introuvable : " + cartItem.getProductId());
                    }
                    OrderItem item = new OrderItem();
                    item.setProductId(cartItem.getProductId());
                    item.setQuantity(cartItem.getQuantity());
                    item.setPrice(product.getPrice());
                    item.setSubtotal(product.getPrice() * cartItem.getQuantity());
                    item.setOrder(order);

                    total.set(total.get() + item.getSubtotal());
                    return item;
                }).collect(Collectors.toList());

        order.setItems(orderItems);
        order.setTotalAmount(total.get());

        return orderRepository.save(order); // ✅ juste sauvegarder l’ordre
    }











    @Override
    public Order getOrderByIdEntity(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }



    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        List<OrderItemDTO> items = order.getItems().stream().map(item -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setProductId(item.getProductId());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPrice(item.getPrice());

            // Appel HTTP pour récupérer le produit
            try {
                ProductDTO product = restTemplate.getForObject(
                        PRODUCT_SERVICE_URL + "/" + item.getProductId(),
                        ProductDTO.class
                );
                itemDTO.setProductName(product != null ? product.getName() : "Produit inconnu");
            } catch (Exception e) {
                itemDTO.setProductName("Produit inconnu");
            }

            return itemDTO;
        }).collect(Collectors.toList());

        dto.setItems(items);

        return dto;
    }
}
