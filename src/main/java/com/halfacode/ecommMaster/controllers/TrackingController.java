package com.halfacode.ecommMaster.controllers;

import com.halfacode.ecommMaster.models.TrackingInfo;
import com.halfacode.ecommMaster.services.TrackingService;
import com.halfacode.ecommMaster.trackinfo.TrackingInfoDTO;
import com.halfacode.ecommMaster.trackinfo.TrackingInfoResponseDTO;
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
    public ResponseEntity<TrackingInfoResponseDTO> saveTrackingInfo(@RequestBody TrackingInfoDTO trackingInfoDTO) {
        TrackingInfo savedInfo = trackingService.saveTrackingInfo(trackingInfoDTO);
        return ResponseEntity.ok(new TrackingInfoResponseDTO(savedInfo));
    }
}
