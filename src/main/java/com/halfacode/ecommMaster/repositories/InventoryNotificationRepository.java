package com.halfacode.ecommMaster.repositories;

import com.halfacode.ecommMaster.models.InventoryNotification;
import com.halfacode.ecommMaster.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryNotificationRepository extends JpaRepository<InventoryNotification,Long> {
    List<InventoryNotification> findByProductAndNotifiedFalse(Product product);
}
