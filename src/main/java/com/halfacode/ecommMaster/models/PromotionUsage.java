package com.halfacode.ecommMaster.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class PromotionUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Promotion promotion;

    @ManyToOne
    private User user;

    private LocalDateTime usageDate;

    private double discountApplied;
}
