package com.halfacode.ecommMaster.models;



import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private String location;
    private String timestamp;

    @ManyToOne
    private User user;

    // Additional fields like payment method, order details, etc.
}
