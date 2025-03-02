package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.Location;
import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.models.ProductInventory;
import com.halfacode.ecommMaster.repositories.ProductInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    @Autowired
    private ProductInventoryRepository productInventoryRepository;

    // Reduce stock for a product at a specific location
    public void reduceInventoryStock(Product product, Location location, int quantity) {
        ProductInventory inventory = productInventoryRepository
                .findByProductAndLocation(product, location)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for the specified product and location."));
        inventory.reduceStock(quantity);
        productInventoryRepository.save(inventory);
    }

    // Add inventory to a product at a specific location
    public ProductInventory addInventoryStock(Product product, Location location, int stockQuantity) {
        ProductInventory inventory = productInventoryRepository
                .findByProductAndLocation(product, location)
                .orElse(new ProductInventory(product, location, 0, false));
        inventory.setStockQuantity(inventory.getStockQuantity() + stockQuantity);
        inventory.setIsAvailable(inventory.getStockQuantity() > 0);
        return productInventoryRepository.save(inventory);
    }

    // Get inventory details for a product
    public List<ProductInventory> getInventoryForProduct(Product product) {
        return productInventoryRepository.findAvailableInventories(product);
    }

    public Location checkLocalStock(Long productId, String customerCountry) {
        Optional<ProductInventory> inventory = productInventoryRepository.findFirstAvailableInCountry(productId, customerCountry);

        if (inventory.isPresent()) {
            return inventory.get().getLocation(); // Return the location with available stock
        }

        return null; // Return null if no stock is available locally
    }

    public Location checkInternationalStock(Long productId, String customerCountry) {
        // Assuming it fetches inventories in countries other than `customerCountry`
        Optional<ProductInventory> inventory = productInventoryRepository.findInternationalStock(productId, customerCountry);

        return inventory.map(ProductInventory::getLocation).orElse(null);
    }

    // Deduct stock from a specific location

}
