package com.halfacode.ecommMaster.models;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToMany
    private List<Product> products;

    // Methods to add, remove, and retrieve products from wishlist...
}
