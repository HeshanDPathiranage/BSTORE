package com.example.BSTORE.service;

import com.example.BSTORE.model.Product; // Import Product model
import com.example.BSTORE.repository.ProductRepository; // <--- ADD THIS LINE
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Get All Products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get Product by ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Save or Update Product
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Delete Product
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}