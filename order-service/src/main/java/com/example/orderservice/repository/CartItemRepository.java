package com.example.orderservice.repository;

import com.example.orderservice.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUserId(Long userId);



    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem c WHERE c.userId = :userId")
    void deleteByUserId(Long userId);

    CartItem findByUserIdAndProductId(Long userId, Long productId);
}
