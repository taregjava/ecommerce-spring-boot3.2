package com.halfacode.ecommMaster.controllers;
import com.halfacode.ecommMaster.services.InventoryManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryManagementService inventoryManagementService;

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
