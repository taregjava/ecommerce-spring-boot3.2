package com.halfacode.ecommMaster.services;

import org.springframework.stereotype.Service;

@Service
public class DistanceCalculator {

    public double calculateDistance(String currentLocation, String customerAddress) {
        // Implement the logic to calculate distance between the two locations
        // For simplicity, let's assume a dummy implementation for now
        // In a real scenario, you might use a third-party API like Google Maps API

        // Example dummy calculation (replace with actual logic)
        return Math.random() * 100; // Returns a random distance between 0 and 100 km
    }
}
