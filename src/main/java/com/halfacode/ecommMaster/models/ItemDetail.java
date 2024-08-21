package com.halfacode.ecommMaster.models;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Entity
@Data
public class ItemDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "tracking_info_id")
    private TrackingInfo trackingInfo;

    // Getters and Setters
}
