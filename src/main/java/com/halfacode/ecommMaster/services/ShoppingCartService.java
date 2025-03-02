package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.dto.ProductDTO;
import com.halfacode.ecommMaster.dto.ShoppingCartDTO;
import com.halfacode.ecommMaster.mapper.ShoppingCartMapper;
import com.halfacode.ecommMaster.models.*;
import com.halfacode.ecommMaster.repositories.ProductInventoryRepository;
import com.halfacode.ecommMaster.repositories.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository cartRepository;

    @Autowired
    private ProductService productService;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductInventoryRepository productInventoryRepository;
    @Autowired
    private InventoryService inventoryService;
    public ShoppingCart getCartByUser(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            ShoppingCart newCart = new ShoppingCart();
            newCart.setUser(user); // Set the user for the new cart
            return newCart;
        });
    }
   /* public ShoppingCartDTO addToCart(Long userId, Long productId, int quantity) {
        // Fetch the user
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        // Fetch the product
        ProductDTO product = productService.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }

        // Check product inventory
        if (product.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
        }

        // Fetch or create a new cart for the user
        ShoppingCart shoppingCart = cartRepository.findByUser(user).orElseGet(() -> {
            ShoppingCart newCart = new ShoppingCart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        // Add product to the cart
        CartItem cartItem = shoppingCart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setProduct(product);
                    newItem.setQuantity(0); // Initialize quantity
                    newItem.setCart(shoppingCart);
                    shoppingCart.getItems().add(newItem);
                    return newItem;
                });

        // Update quantity and price
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setPrice(product.getPrice() * cartItem.getQuantity());

        // Deduct product stock
        product.setStock(product.getStock() - quantity);

        // Save the updated cart
        cartRepository.save(shoppingCart);

        // Map to DTO and return
        return shoppingCartMapper.toDTO(shoppingCart);
    }
*/

    public ShoppingCart addToCart(User user, Long productId, int quantity) {
        ShoppingCart cart = getCartByUser(user);  // Fetch or create a cart for the user
        Product product = productService.getProductEntityById(productId);  // Fetch the product
        cart.addItem(product, quantity);  // Add the product to the cart

        // Update total price
        cart.updateTotalPrice();

        ShoppingCart updatedCart = cartRepository.save(cart);  // Save the updated cart
        return updatedCart;
    }



    @Transactional
    public ShoppingCart addToCartWithShipping(User user, Long productId, int quantity, String customerCountry) {
        // Fetch or create user's cart
        ShoppingCart cart = getCartByUser(user);

        // Fetch the product entity
        Product product = productService.getProductEntityById(productId);

        // Declare the shipping cost variable
        double shippingCost = 0;

        // Check local stock first
        Location stockLocation = inventoryService.checkLocalStock(productId, customerCountry);

        // If local stock is available
        if (stockLocation != null) {
            ProductInventory productInventory = stockLocation.getInventories().stream()
                    .filter(inventory -> inventory.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElse(null);

            if (productInventory != null && productInventory.getStockQuantity() >= quantity) {
                // Reduce the local stock
                inventoryService.reduceInventoryStock(product, stockLocation, quantity);
                shippingCost = calculateShippingCost("local", 0, false); // No distance for local shipping
            } else {
                // If local stock is insufficient, check international stock
                Location internationalStock = inventoryService.checkInternationalStock(productId, customerCountry);

                if (internationalStock == null) {
                    throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
                }

                ProductInventory internationalProductInventory = internationalStock.getInventories().stream()
                        .filter(inventory -> inventory.getProduct().getId().equals(productId))
                        .findFirst()
                        .orElse(null);

                if (internationalProductInventory == null || internationalProductInventory.getStockQuantity() < quantity) {
                    throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
                }

                // Reduce the international stock
                inventoryService.reduceInventoryStock(product, internationalStock, quantity);
                double distance = calculateDistance(customerCountry, internationalStock.getCountry());
                shippingCost = calculateShippingCost("international", distance, true);
            }
        } else {
            throw new IllegalArgumentException("No stock available in the local location for product: " + product.getName());
        }

        // Add the product to the cart
        cart.addItem(product, quantity);

        // Update the shipping cost
        cart.setShippingCost(shippingCost);

        // Update the total price (ensure this method performs necessary calculations and persists the update)
        cart.updateTotalPrice();

        // Save and return the updated cart
        return cartRepository.save(cart);
    }





    public ShoppingCart removeFromCart(User user, Long productId) {
        ShoppingCart cart = getCartByUser(user);
        cart.removeItem(productId);
        return cartRepository.save(cart);
    }

    public ShoppingCart clearCart(User user) {
        ShoppingCart cart = getCartByUser(user);
        cart.clearCart();
        return cartRepository.save(cart);
    }
    public double calculateShippingCost(String type, double distance, boolean includesCustoms) {
        double baseCost = type.equals("local") ? 10.0 : 20.0; // Base cost
        double distanceCost = type.equals("international") ? distance * 0.5 : 0.0;
        double customsFee = includesCustoms ? 15.0 : 0.0;

        return baseCost + distanceCost + customsFee;
    }

    public double calculateDistance(String fromCountry, String toCountry) {
        // Simplified distance logic (e.g., predefined distances or API integration)
        if (fromCountry.equals("UAE") && toCountry.equals("Sudan")) return 3000.0;
        if (fromCountry.equals("Saudi Arabia") && toCountry.equals("Sudan")) return 2500.0;

        return 5000.0; // Default distance
    }

}
