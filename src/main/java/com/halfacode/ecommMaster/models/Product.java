package com.halfacode.ecommMaster.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;




@Entity
@Data
@NoArgsConstructor
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

    // Change FetchType to LAZY for ManyToOne relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;

    // Set OneToMany to LAZY as well
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @Transient // Mark as transient because it’s not a database column
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
    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + price + "}";
    }

}
