package com.halfacode.ecommMaster.models;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String contactInfo;

    private double commissionRate;

    @OneToMany()
    private List<Product> products;

    // Getters and setters...
}

