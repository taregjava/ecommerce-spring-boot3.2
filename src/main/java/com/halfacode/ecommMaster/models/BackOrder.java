package com.halfacode.ecommMaster.models;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class BackOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime orderDate;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    private Integer quantity;

    // Getters and Setters
}
