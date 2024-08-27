package com.halfacode.ecommMaster.repositories;
import com.halfacode.ecommMaster.models.Order;

import com.halfacode.ecommMaster.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    Optional<Order> findById(Long id);
}
