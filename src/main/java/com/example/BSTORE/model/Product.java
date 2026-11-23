package com.example.BSTORE.model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private Double price;

    @Column(length = 1000) // Allow longer URLs for images
    private String image;

    private String badge; // "New", "Sale", or null

    @Column(name = "sold_out")
    private boolean soldOut;

    // Default Constructor
    public Product() {
    }

    // Constructor with fields
    public Product(String name, String category, Double price, String image, String badge, boolean soldOut) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.image = image;
        this.badge = badge;
        this.soldOut = soldOut;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public boolean isSoldOut() {
        return soldOut;
    }

    public void setSoldOut(boolean soldOut) {
        this.soldOut = soldOut;
    }
}