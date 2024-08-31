package com.halfacode.ecommMaster.models;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private double discountPercentage;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean active;

    @ManyToMany
    private List<Product> applicableProducts;

    @ElementCollection
    @CollectionTable(name = "promotion_tiers", joinColumns = @JoinColumn(name = "promotion_id"))
    @Column(name = "tier")
    private List<String> applicableTiers; // List of tiers this promotion applies to

    // Method to check if a promotion is active
    public boolean isActive(LocalDateTime now) {
        return active && now.isAfter(startDate) && now.isBefore(endDate);
    }

    // Getters and setters...
}
