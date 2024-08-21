package com.halfacode.ecommMaster.controllers;

import com.halfacode.ecommMaster.models.TrackingInfo;
import com.halfacode.ecommMaster.services.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tracking")
public class TrackingController {

    @Autowired
    private TrackingService trackingService;

    @GetMapping("/{orderNumber}")
    public ResponseEntity<TrackingInfo> getTrackingInfo(@PathVariable String orderNumber) {
        TrackingInfo trackingInfo = trackingService.getTrackingInfo(orderNumber);
        return ResponseEntity.ok(trackingInfo);
    }

    @PostMapping
    public ResponseEntity<TrackingInfo> saveTrackingInfo(@RequestBody TrackingInfo trackingInfo) {
        TrackingInfo savedInfo = trackingService.saveTrackingInfo(trackingInfo);
        return ResponseEntity.ok(savedInfo);
    }
}
