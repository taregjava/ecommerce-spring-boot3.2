package com.halfacode.ecommMaster.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.Data;

@Data
@AllArgsConstructor
public class GeoLocationResponse {
    private String status;  // Must be "success" to get data
    private String country;
    private String city;
    private double lat;
    private double lon;
    private String zip;
    private String message; // To check errors
}
