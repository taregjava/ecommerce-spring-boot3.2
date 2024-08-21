package com.halfacode.ecommMaster.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDate;
    private Double totalAmount;
    private String status; // e.g., "Pending", "Processing", "Shipped", "Delivered"

    @OneToMany(cascade = CascadeType.ALL)
    private List<CartItem> items;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime deliveryDate;
    private String trackingNumber;

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }
}
