package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryManagementService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NotificationService notificationService;

    // Check for products with low stock levels and notify admin
    @Scheduled(fixedRate = 3600000) // Runs every hour (3600000 milliseconds)
    public void checkLowStockLevels() {
        System.out.println("Starting checkLowStockLevels method.");

        List<Product> products = productRepository.findAll();
        System.out.println("Retrieved " + products.size() + " products from the repository.");

        products.forEach(product -> {
            System.out.println("Checking stock level for product: " + product.getName() + " with quantity: " + product.getStockQuantity());
            if (product.getStockQuantity() < 33) { // Low stock threshold
                String notificationMessage = "Low Stock Alert: Product '" + product.getName() +
                        "' (ID: " + product.getId() +
                        ") has only " + product.getStockQuantity() +
                        " units left in stock. Please restock soon.";
                notificationService.notifyAdmin(notificationMessage);
            }
        });

        System.out.println("Finished checkLowStockLevels method.");
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
