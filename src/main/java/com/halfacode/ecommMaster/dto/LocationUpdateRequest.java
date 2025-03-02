package com.halfacode.ecommMaster.dto;

import lombok.Data;

@Data
public class LocationUpdateRequest {
    private Long userId;
    private String country;
    private String city;
    private double latitude;
    private double longitude;

    // Getters and Setters
}

