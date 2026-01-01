package com.ace.productservice.repository;

import com.ace.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Ici on peut ajouter des requêtes custom si nécessaire
}
