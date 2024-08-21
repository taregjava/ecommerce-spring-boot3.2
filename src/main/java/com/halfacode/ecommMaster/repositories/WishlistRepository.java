package com.halfacode.ecommMaster.repositories;

import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.models.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist,Long> {
    Optional<Wishlist> findByUser(User user);
}
