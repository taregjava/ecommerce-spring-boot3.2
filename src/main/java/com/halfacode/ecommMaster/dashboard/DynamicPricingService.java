package com.halfacode.ecommMaster.dashboard;

import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DynamicPricingService {

    @Autowired
    private ProductRepository productRepository;

    public void adjustPricesBasedOnDemand() {
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            int demand = product.getSalesCount(); // Example metric for demand
            double basePrice = product.getBasePrice();

            double newPrice = basePrice * (1 + calculateDemandFactor(demand));
            product.setPrice(newPrice);
            productRepository.save(product);
        }
    }

    private double calculateDemandFactor(int demand) {
        if (demand > 1000) {
            return 0.20; // Increase price by 20%
        } else if (demand > 500) {
            return 0.10; // Increase price by 10%
        } else if (demand < 100) {
            return -0.10; // Decrease price by 10%
        }
        return 0.0; // No change
    }
}

