package com.halfacode.ecommMaster.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @OneToOne
    private User user;

    // Add, remove, clear items
    public void addItem(Product product, int quantity) {
        boolean updated = false;
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                updated = true;
                System.out.println("Updated Item: " + item.getProduct().getName() + " Quantity: " + item.getQuantity());
                System.out.println("Updated Total Price: " + item.getTotalPrice());
                break;
            }
        }
        if (!updated) {
            CartItem newItem = new CartItem(product, quantity);
            items.add(newItem);
            System.out.println("Added New Item: " + product.getName() + " Quantity: " + quantity);
            System.out.println("Total Price: " + newItem.getTotalPrice());
        }
        System.out.println("Cart Total Price Before Save: " + getTotalPrice());
    }

    public double getTotalPrice() {
        double total = items.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        System.out.println("Cart Total Price Calculated: " + total);
        return total;
    }


    public void removeItem(Long productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public void clearCart() {
        items.clear();
    }

   /* public double getTotalPrice() {
        double total = items.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        System.out.println("Cart Total Price: " + total);
        return total;
    }*/
}
