package com.halfacode.ecommMaster.models;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    private LocalDateTime viewTime;

    private String action; // e.g., "VIEW", "PURCHASE"

    // Getters and Setters
}
