package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.models.Wishlist;
import com.halfacode.ecommMaster.repositories.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    public Wishlist addToWishlist(User user, Product product) {
        Wishlist wishlist = wishlistRepository.findByUser(user).orElse(new Wishlist());
        wishlist.getProducts().add(product);
        return wishlistRepository.save(wishlist);
    }

    public Wishlist removeFromWishlist(User user, Product product) {
        Wishlist wishlist = wishlistRepository.findByUser(user).orElse(new Wishlist());
        wishlist.getProducts().remove(product);
        return wishlistRepository.save(wishlist);
    }

    public List<Product> getWishlistProducts(User user) {
        return wishlistRepository.findByUser(user).map(Wishlist::getProducts).orElse(Collections.emptyList());
    }
}
