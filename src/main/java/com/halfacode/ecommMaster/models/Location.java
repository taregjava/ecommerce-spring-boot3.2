package com.halfacode.ecommMaster.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String country;
    private String city;
    private String postalCode;
    private double latitude;
    private double longitude;
    @OneToMany(mappedBy = "location")
    private List<ProductInventory> inventories;


}
