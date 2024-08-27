package com.halfacode.ecommMaster.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    private Integer quantity;
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = product.getPrice() * quantity;
    }

    // Update total price when quantity is changed
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = this.product.getPrice() * quantity;
    }

    public Product getProduct() {
        return product;
    }

    // Getters and Setters are managed by Lombok's @Data annotation
}
