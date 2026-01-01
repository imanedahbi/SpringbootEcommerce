package com.example.orderservice.controller;

import com.example.orderservice.entity.CartItem;
import com.example.orderservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8082"}) // ← AJOUTEZ CETTE LIGNE
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> request) {
        // Ajoutez des logs pour déboguer
        System.out.println("🎯 POST /cart/add appelé !");
        System.out.println("📦 Données reçues: userId=" + request.get("userId")
                + ", productId=" + request.get("productId")
                + ", quantity=" + request.get("quantity"));

        Long userId = Long.valueOf(request.get("userId").toString());
        Long productId = Long.valueOf(request.get("productId").toString());
        Integer quantity = Integer.valueOf(request.get("quantity").toString());

        CartItem item = cartService.addToCart(userId, productId, quantity);

        System.out.println("✅ Item créé: " + item);
        return ResponseEntity.ok(item);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateQuantity(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {

        Integer quantity = Integer.valueOf(request.get("quantity").toString());
        CartItem item = cartService.updateQuantity(id, quantity);

        return ResponseEntity.ok(item);
    }





    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getCart(@PathVariable Long userId) {
        List<CartItem> cart = cartService.getCart(userId);

        // ⚡ Jamais null
        if (cart == null || cart.isEmpty()) {
            System.out.println("ℹ️ Panier vide pour userId=" + userId);
            return ResponseEntity.ok(List.of());
        }

        System.out.println("🛒 CartItems pour userId=" + userId + " : " + cart);
        return ResponseEntity.ok(cart);
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long id) {
        cartService.deleteItem(id);
        return ResponseEntity.ok().build();
    }








    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<?> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }



}
