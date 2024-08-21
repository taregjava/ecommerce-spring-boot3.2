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

    // Getters and setters...
}

