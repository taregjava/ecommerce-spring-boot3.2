package com.halfacode.ecommMaster.repositories;

import com.halfacode.ecommMaster.dto.ProductDTO;
import com.halfacode.ecommMaster.models.BackOrder;
import com.halfacode.ecommMaster.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BackOrderRepository extends JpaRepository<BackOrder, Long> {
    List<BackOrder> findByProduct(ProductDTO product);
}
