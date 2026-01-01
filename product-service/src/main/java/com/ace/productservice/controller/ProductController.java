package com.ace.productservice.controller;

import com.ace.productservice.model.Product;
import com.ace.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

import java.io.File;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Value("${product.images.dir}")
    private String imagesDir;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
        return ResponseEntity.ok(productService.addProduct(product));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(Map.of("message", "Produit supprimé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("message", "Produit non trouvé"));
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            File directory = new File(imagesDir);
            if (!directory.exists()) directory.mkdirs();

            String filePath = imagesDir + File.separator + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            // URL accessible depuis le navigateur
            String imageUrl = "/images/" + file.getOriginalFilename();
            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Erreur lors de l'upload de l'image"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        try {
            Product updated = productService.updateProduct(id, product);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body(null));
    }

}
