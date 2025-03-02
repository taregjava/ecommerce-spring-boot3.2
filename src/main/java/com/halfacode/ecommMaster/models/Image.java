package com.halfacode.ecommMaster.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private String name;

    @Column(nullable = false)
    private boolean isMain;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    // Getters and setters
}
