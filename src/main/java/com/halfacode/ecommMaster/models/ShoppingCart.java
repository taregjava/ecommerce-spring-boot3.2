package com.halfacode.ecommMaster.models;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
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
    private double subtotal;
    private double tax;
    private double discount;

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
        updateTotalPrice();
    }

    public void removeItem(Long productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
        updateTotalPrice();
    }

    public void clearCart() {
        items.clear();
        updateTotalPrice();
    }

    public void updateTotalPrice() {
        double updatedSubtotal = items.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        this.subtotal = updatedSubtotal;
        this.tax = this.subtotal * 0.1;
        this.shippingCost = (this.shippingCost == 0) ? 20.0 : this.shippingCost; // ✅ Ensure shipping cost is set
        this.discount = calculateDiscount();
        this.totalPrice = this.subtotal + this.shippingCost + this.tax - this.discount;
    }

    public double calculateDiscount() {
        return this.subtotal > 100 ? this.subtotal * 0.05 : 0.0; // Example: 5% discount if subtotal > 100
    }

    public double getTotalPrice() {
        updateTotalPrice();
        return this.totalPrice;
    }
}
