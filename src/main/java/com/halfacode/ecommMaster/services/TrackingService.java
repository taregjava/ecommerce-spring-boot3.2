package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.TrackingInfo;
import com.halfacode.ecommMaster.repositories.TrackingInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrackingService {

    @Autowired
    private TrackingInfoRepository trackingInfoRepository;

    public TrackingInfo getTrackingInfo(String orderNumber) {
        return trackingInfoRepository.findByOrderNumber(orderNumber);
    }

    public TrackingInfo saveTrackingInfo(TrackingInfo trackingInfo) {
        return trackingInfoRepository.save(trackingInfo);
    }

}
