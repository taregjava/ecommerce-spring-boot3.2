package com.halfacode.ecommMaster.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private Boolean isAvailable;
    private double basePrice;
    private int salesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnore // Avoid circular reference
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // Avoid circular reference
    private List<Review> reviews = new ArrayList<>();

    @Transient
    private double averageRating;

    public Product(String name, String description, Double price, Integer stockQuantity, Boolean isAvailable, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.isAvailable = isAvailable;
        this.category = category;
    }

    @PostLoad
    private void calculateAverageRating() {
        this.averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    public void reduceStock(int quantity) {
        if (this.stockQuantity >= quantity) {
            this.stockQuantity -= quantity;
            if (this.stockQuantity == 0) {
                this.isAvailable = false;
            }
        } else {
            throw new IllegalStateException("Insufficient stock for product: " + name);
        }
    }

    public int getDemandLevel() {
        if (this.stockQuantity < 50) {
            return 100; // High demand if stock is low
        } else if (this.stockQuantity < 200) {
            return 50; // Moderate demand
        } else {
            return 10; // Low demand
        }
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.setProduct(this);
    }

    public void removeReview(Review review) {
        reviews.remove(review);
        review.setProduct(null);
    }
}
