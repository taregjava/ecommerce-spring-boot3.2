package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryManagementService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NotificationService notificationService;

    // Check for products with low stock levels and notify admin
    public void checkLowStockLevels() {
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            if (product.getStockQuantity() < 10) { // Low stock threshold
                notificationService.notifyAdmin("Low stock alert for product: " + product.getName());
            }
        }
    }

    // Reduce the stock of a product when an order is placed
    public void reduceStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        product.reduceStock(quantity);
        productRepository.save(product);

        if (product.getStockQuantity() < 10) {
            notificationService.notifyAdmin("Low stock alert for product: " + product.getName());
        }
    }
}
