package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.dto.ProductDTO;
import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.models.ShoppingCart;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.repositories.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository cartRepository;

    @Autowired
    private ProductService productService;

    public ShoppingCart getCartByUser(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            ShoppingCart newCart = new ShoppingCart();
            newCart.setUser(user); // Set the user for the new cart
            return newCart;
        });
    }

    public ShoppingCart addToCart(User user, Long productId, int quantity) {
        ShoppingCart cart = getCartByUser(user);  // Fetch or create a cart for the user
        Product product = productService.getProductEntityById(productId);  // Fetch the product
        cart.addItem(product, quantity);  // Add the product to the cart
        ShoppingCart updatedCart = cartRepository.save(cart);  // Save the updated cart
        System.out.println("Saved Cart Total Price: " + updatedCart.getTotalPrice());
        return updatedCart;
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
}
