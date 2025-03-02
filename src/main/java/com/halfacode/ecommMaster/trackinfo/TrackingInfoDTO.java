package com.halfacode.ecommMaster.trackinfo;

import lombok.Data;

@Data
public class TrackingInfoDTO {
    private String currentLocation;
    private double distanceToCustomer;
    private String packageStatus;
    private String courier;
    private Long orderId;

    private String street;
    private String city;
    private String nearbyLandmark;
    private String postalCode;
}
