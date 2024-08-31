package com.halfacode.ecommMaster.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Tier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int conversionRate;

    public Tier(String name, int conversionRate) {
        this.name = name;
        this.conversionRate = conversionRate;
    }

    // Getters and Setters
}


