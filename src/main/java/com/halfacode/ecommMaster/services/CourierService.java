package com.halfacode.ecommMaster.services;

import org.springframework.stereotype.Service;

@Service
public class CourierService {

    public String getAssignedCourier(Long orderId) {
        // Implement the logic to retrieve the assigned courier for the order
        // For simplicity, returning a dummy courier name

        // Example dummy courier (replace with actual logic)
        return "FedEx";
    }
}
