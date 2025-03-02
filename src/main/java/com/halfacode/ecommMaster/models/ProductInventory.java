package com.halfacode.ecommMaster.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    private Integer stockQuantity;
    private Boolean isAvailable;

    public ProductInventory(Product product, Location location, Integer stockQuantity, Boolean isAvailable) {
        this.product = product;
        this.location = location;
        this.stockQuantity = stockQuantity;
        this.isAvailable = isAvailable;
    }

    public void reduceStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to reduce must be greater than 0.");
        }
        if (this.stockQuantity >= quantity) {
            this.stockQuantity -= quantity;
            if (this.stockQuantity == 0) {
                this.isAvailable = false;
            }
        } else {
            throw new IllegalStateException(
                    "Insufficient stock for location: " + (location != null ? location.getName() : "unknown")
            );
        }
    }


    public int getStockQuantity() {
        return stockQuantity;
    }
}

