package com.example.BSTORE.controller;

import com.example.BSTORE.model.Product;
import com.example.BSTORE.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    private boolean isAdmin(HttpSession session) {
        return session != null && session.getAttribute(AuthController.ADMIN_SESSION_KEY) != null;
    }

    // GET: Fetch all products (Public)
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // GET: Fetch single product by ID (Public)
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Create a new product (Admin Only)
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product, HttpSession session) {
        if (!isAdmin(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Admin authentication required"));
        }
        Product saved = productService.saveProduct(product);
        return ResponseEntity.ok(saved);
    }

    // PUT: Update an existing product (Admin Only)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product productDetails, HttpSession session) {
        if (!isAdmin(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Admin authentication required"));
        }
        return productService.getProductById(id)
                .map(product -> {
                    product.setName(productDetails.getName());
                    product.setPrice(productDetails.getPrice());
                    product.setCategory(productDetails.getCategory());
                    product.setImage(productDetails.getImage());
                    product.setBadge(productDetails.getBadge());
                    product.setSoldOut(productDetails.isSoldOut());
                    return ResponseEntity.ok((Object) productService.saveProduct(product));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE: Remove a product (Admin Only)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Admin authentication required"));
        }
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
