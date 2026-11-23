package com.example.BSTORE.repository;

import com.example.BSTORE.model.Product; // <--- MAKE SURE THIS MATCHES YOUR FOLDER
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // You can add custom query methods here if needed
}