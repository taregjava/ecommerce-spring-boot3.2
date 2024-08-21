package com.halfacode.ecommMaster.models;

import jakarta.persistence.*;

@Entity
public class InventoryNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    private boolean notified;

    // Mark as notified
    public void markAsNotified() {
        this.notified = true;
    }
}
