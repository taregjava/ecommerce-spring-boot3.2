package com.halfacode.ecommMaster.trackinfo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderResponseDTO {
    private Long id;
    private LocalDateTime orderDate;
    private double totalAmount;
    private String status;

    // Constructor
    public OrderResponseDTO(Long id, LocalDateTime orderDate, double totalAmount, String status) {
        this.id = id;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters and Setters...
}