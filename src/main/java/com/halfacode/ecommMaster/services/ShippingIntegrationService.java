package com.halfacode.ecommMaster.services;

import org.springframework.stereotype.Service;

@Service
public class ShippingIntegrationService {

    public double calculateShippingCost(String destination, double weight) {
        // Example: Integrate with a shipping API
        // For simplicity, we use a fixed rate here
        return weight * 10; // $10 per kg
    }
}

