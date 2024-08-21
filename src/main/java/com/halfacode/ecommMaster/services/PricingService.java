package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.models.User;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class PricingService {

    public double calculatePrice(Product product, User user) {
        double basePrice = product.getBasePrice();

        // Adjust price based on user location
        String location = user.getLocation();
        if ("Riyadh".equals(location)) {
            basePrice += basePrice * 0.1; // 10% markup
        }

        // Adjust price based on demand
        int demand = product.getDemandLevel();
        if (demand > 100) {
            basePrice += basePrice * 0.2; // 20% increase
        }

        // Time-based discounts
        LocalTime currentTime = LocalTime.now();
        if (currentTime.isAfter(LocalTime.of(20, 0)) && currentTime.isBefore(LocalTime.of(22, 0))) {
            basePrice -= basePrice * 0.05; // 5% evening discount
        }

        return basePrice;
    }
}

