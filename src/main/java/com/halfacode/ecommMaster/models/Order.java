package com.halfacode.ecommMaster.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CartItem> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    private LocalDateTime deliveryDate;
    private String trackingNumber;
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private TrackingInfo trackingInfo;
    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }

    public void addItem(CartItem item) {
        if (item.getOrder() != null && item.getOrder() != this) {
            throw new IllegalArgumentException("CartItem is already associated with another Order");
        }
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(CartItem item) {
        if (items.remove(item)) {
            item.setOrder(null);
        } else {
            throw new IllegalArgumentException("CartItem not found in this Order");
        }
    }
}
