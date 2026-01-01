package com.example.orderservice.service;

import com.example.orderservice.entity.CartItem;
import com.example.orderservice.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;

    public CartItem addToCart(Long userId, Long productId, Integer quantity) {

        CartItem existing = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);  // 🔥 INCRÉMENTATION
            return cartItemRepository.save(existing);
        }

        CartItem item = new CartItem();
        item.setUserId(userId);
        item.setProductId(productId);
        item.setQuantity(quantity);

        return cartItemRepository.save(item);
    }


    public CartItem updateQuantity(Long id, Integer quantity) {
        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setQuantity(quantity);  // ✔ mettre directement la quantité
        return cartItemRepository.save(item);
    }



    public List<CartItem> getCart(Long userId) {
        List<CartItem> cart = cartItemRepository.findByUserId(userId);
        System.out.println("🔹 CartService getCart pour userId=" + userId + " : " + cart);
        return cart != null ? cart : List.of(); // jamais null
    }


    public void deleteItem(Long id) {
        cartItemRepository.deleteById(id);
    }








    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
        cartItemRepository.flush(); // 🔥 force flush pour que la suppression soit immédiate
    }



}
