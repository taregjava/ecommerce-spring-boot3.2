package com.halfacode.ecommMaster.response;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class ExternalApiTrackingResponse {

    private String currentLocation;
    private double distanceToCustomer;
    private String packageStatus;
    private String orderNumber;
    private String courier;
    private List<ItemDetailResponse> items;
    private String street;
    private String city;
    private String nearbyLandmark;
    private String postalCode;
    private List<TrackingEventResponse> trackingHistory;

    // Getters and Setters

    @Data
    public static class ItemDetailResponse {
        private String itemName;
        private int quantity;
        // Getters and Setters
    }

    @Data
    public static class TrackingEventResponse {
        private String status;
        private String location;
        private Date timestamp;
        // Getters and Setters
    }
}
