package com.halfacode.ecommMaster.models;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class TrackingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private String location;
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "tracking_info_id")
    private TrackingInfo trackingInfo;

    // Getters and Setters
}
