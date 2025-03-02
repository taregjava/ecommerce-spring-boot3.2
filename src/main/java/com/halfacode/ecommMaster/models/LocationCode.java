package com.halfacode.ecommMaster.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "tbl_location")
@Entity
@Data
public class LocationCode {

    @Id
    @Column(length = 12, nullable = false, unique = true)
    private String code;
    @Column(length = 128, nullable = false)
    private String cityName;
    @Column(length = 128)
    private String regionName;
    @Column(length = 64, nullable = false)
    private String countryName;
    @Column(length = 2, nullable = false)
    private String countryCode;
    private boolean enabled;
    private boolean trashed;



}
