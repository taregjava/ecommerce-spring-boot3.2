package com.halfacode.ecommMaster.controllers;
import com.halfacode.ecommMaster.models.Location;
import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.models.ProductInventory;
import com.halfacode.ecommMaster.repositories.LocationRepository;
import com.halfacode.ecommMaster.repositories.ProductRepository;
import com.halfacode.ecommMaster.services.InventoryManagementService;
import com.halfacode.ecommMaster.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryManagementService inventoryManagementService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private LocationRepository locationRepository;
    @PostMapping("/add")
    public ResponseEntity<String> addStock(@RequestParam Long productId, @RequestParam Long locationId, @RequestParam int stockQuantity) {
        // Fetch product and location
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found with ID: " + locationId));

        // Add stock to inventory
        inventoryService.addInventoryStock(product, location, stockQuantity);
        return ResponseEntity.ok("Stock added successfully.");
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductInventory>> getProductInventory(@PathVariable Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
        List<ProductInventory> inventory = inventoryService.getInventoryForProduct(product);
        return ResponseEntity.ok(inventory);
    }
    // Endpoint to manually check low stock levels
    @GetMapping("/check-low-stock")
    public ResponseEntity<String> checkLowStockLevels() {
        inventoryManagementService.checkLowStockLevels();
        return ResponseEntity.ok("Low stock levels checked and notifications sent if necessary.");
    }

    // Endpoint to reduce stock when an order is placed
    @PostMapping("/reduce-stock")
    public ResponseEntity<String> reduceStock(@RequestParam Long productId, @RequestParam int quantity) {
        inventoryManagementService.reduceStock(productId, quantity);
        return ResponseEntity.ok("Stock reduced for product ID: " + productId);
    }
}
