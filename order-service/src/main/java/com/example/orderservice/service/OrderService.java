package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDTO;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.entity.Order;

public interface OrderService {

    Order createOrder(OrderRequest orderRequest);

    OrderDTO getOrderById(Long orderId);
    Order getOrderByIdEntity(Long orderId);

}
