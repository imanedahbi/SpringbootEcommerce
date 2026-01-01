package com.ace.productservice.service;

import com.ace.productservice.model.Product;
import com.ace.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Ajouter un produit
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // Modifier un produit
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        existing.setName(updatedProduct.getName());
        existing.setCategory(updatedProduct.getCategory());
        existing.setPrice(updatedProduct.getPrice());
        existing.setDescription(updatedProduct.getDescription());
        existing.setImage(updatedProduct.getImage()); // si tu veux permettre de changer l'image
        return productRepository.save(existing);
    }

    // Supprimer un produit
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Lister tous les produits
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Chercher un produit par id
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
}
