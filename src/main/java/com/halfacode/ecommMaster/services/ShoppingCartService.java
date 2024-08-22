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
        return cartRepository.findByUser(user).orElse(new ShoppingCart());
    }

    public ShoppingCart addToCart(User user, Long productId, int quantity) {
        ShoppingCart cart = getCartByUser(user);
        ProductDTO product = productService.getProductById(productId);
              //  .orElseThrow(() -> new RuntimeException("Product not found"));

        //cart.addItem(product, quantity);
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
}
