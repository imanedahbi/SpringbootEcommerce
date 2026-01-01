package com.example.orderservice.repository;

import com.example.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // tu peux ajouter des méthodes custom si besoin, par exemple :
    // List<Order> findByUserId(Long userId);
}
