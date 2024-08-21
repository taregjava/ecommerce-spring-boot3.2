package com.halfacode.ecommMaster.repositories;

import com.halfacode.ecommMaster.models.ShoppingCart;
import com.halfacode.ecommMaster.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUser(User user);
}
