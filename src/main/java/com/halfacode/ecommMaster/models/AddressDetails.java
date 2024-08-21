package com.halfacode.ecommMaster.models;

import jakarta.persistence.Embeddable;
import lombok.Data;


@Embeddable
@Data
public class AddressDetails {

    private String street;
    private String city;
    private String nearbyLandmark;
    private String postalCode;

    // Getters and Setters
}

