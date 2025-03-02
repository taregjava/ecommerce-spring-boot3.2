package com.halfacode.ecommMaster.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @OneToOne
    private User user;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    private double shippingCost;
    private double totalPrice;
    // Add, remove, clear items
    public void addItem(Product product, int quantity) {
        CartItem item = items.stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElseGet(() -> {
                    CartItem newItem = new CartItem(product, 0);
                    items.add(newItem);
                    return newItem;
                });
        item.setQuantity(item.getQuantity() + quantity);
        updateTotalPrice(); // Ensure total price updates
    }



    public double getTotalPrice() {
        double total = calculateItemsTotalPrice() + shippingCost;
        System.out.println("Cart Total Price Calculated: " + total);
        return total;
    }





    public void removeItem(Long productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public void clearCart() {
        items.clear();
    }
    public void updateTotalPrice() {
        this.totalPrice = items.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }


    private double calculateTotalPrice() {
        // Calculate total price logic (including items and shipping)
        return this.shippingCost + calculateItemsTotalPrice();  // Assuming there's a method for items' total price
    }

    private double calculateItemsTotalPrice() {
        return items.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

   /* public double getTotalPrice() {
        double total = items.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        System.out.println("Cart Total Price: " + total);
        return total;
    }*/
}
