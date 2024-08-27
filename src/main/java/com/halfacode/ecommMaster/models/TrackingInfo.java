package com.halfacode.ecommMaster.models;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class TrackingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String currentLocation;
    private double distanceToCustomer;
    private String packageStatus;
    private String orderNumber;
    private String courier;

    @OneToMany(mappedBy = "trackingInfo", cascade = CascadeType.ALL)
    private List<ItemDetail> items;

    @Embedded
    private AddressDetails addressDetails;

    @OneToMany(mappedBy = "trackingInfo", cascade = CascadeType.ALL)
    private List<TrackingEvent> trackingHistory;
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;  // Reference to the Order entity
    // Getters and Setters
}
