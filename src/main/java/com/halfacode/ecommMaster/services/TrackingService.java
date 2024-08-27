package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.Order;
import com.halfacode.ecommMaster.models.TrackingInfo;
import com.halfacode.ecommMaster.repositories.OrderRepository;
import com.halfacode.ecommMaster.repositories.TrackingInfoRepository;
import com.halfacode.ecommMaster.trackinfo.TrackingInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackingService {

    @Autowired
    private TrackingInfoRepository trackingInfoRepository;

    @Autowired
    private OrderRepository orderRepository;

    public TrackingInfo getTrackingInfo(String orderNumber) {
        return trackingInfoRepository.findByOrderNumber(orderNumber);
    }

    public TrackingInfo saveTrackingInfo(TrackingInfoDTO trackingInfoDTO) {
        // Step 1: Retrieve the Order using the order ID from the DTO
        Order order = orderRepository.findById(trackingInfoDTO.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found for ID: " + trackingInfoDTO.getOrderId()));

        // Step 2: Create a new TrackingInfo object and set the fields
        TrackingInfo trackingInfo = new TrackingInfo();
        trackingInfo.setCurrentLocation(trackingInfoDTO.getCurrentLocation());
        trackingInfo.setDistanceToCustomer(trackingInfoDTO.getDistanceToCustomer());
        trackingInfo.setPackageStatus(trackingInfoDTO.getPackageStatus());
        trackingInfo.setCourier(trackingInfoDTO.getCourier());
        trackingInfo.setOrder(order);

        // Step 3: Save the TrackingInfo in the database
        TrackingInfo savedInfo = trackingInfoRepository.save(trackingInfo);

        // Step 4: Optionally update the Order status based on the tracking info
        order.setStatus(trackingInfo.getPackageStatus());
        orderRepository.save(order);  // Save the updated order status

        // Step 5: Return the saved TrackingInfo
        return savedInfo;
    }
}
