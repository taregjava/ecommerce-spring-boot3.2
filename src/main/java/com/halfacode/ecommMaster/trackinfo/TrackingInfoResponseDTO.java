package com.halfacode.ecommMaster.trackinfo;

import com.halfacode.ecommMaster.models.TrackingInfo;
import lombok.Data;

@Data
public class TrackingInfoResponseDTO {
    private Long id;
    private String currentLocation;
    private double distanceToCustomer;
    private String packageStatus;
    private String courier;
    private OrderResponseDTO order;

    // Constructor
    public TrackingInfoResponseDTO(TrackingInfo trackingInfo) {
        this.id = trackingInfo.getId();
        this.currentLocation = trackingInfo.getCurrentLocation();
        this.distanceToCustomer = trackingInfo.getDistanceToCustomer();
        this.packageStatus = trackingInfo.getPackageStatus();
        this.courier = trackingInfo.getCourier();
        this.order = new OrderResponseDTO(
                trackingInfo.getOrder().getId(),
                trackingInfo.getOrder().getOrderDate(),
                trackingInfo.getOrder().getTotalAmount(),
                trackingInfo.getOrder().getStatus()
        );
    }

    // Getters and Setters...
}
