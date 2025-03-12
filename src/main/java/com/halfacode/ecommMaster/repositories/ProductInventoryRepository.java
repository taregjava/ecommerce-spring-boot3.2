package com.halfacode.ecommMaster.repositories;

import com.halfacode.ecommMaster.models.Location;
import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.models.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {

    Optional<ProductInventory> findByProductAndLocation(Product product, Location location);

    @Query("SELECT p FROM ProductInventory p WHERE p.product.id = :productId AND p.stockQuantity > 0 AND p.location.country = :country")
    Optional<ProductInventory> findFirstAvailableInCountry(@Param("productId") Long productId, @Param("country") String country);

    @Query("SELECT p FROM ProductInventory p WHERE p.product = :product AND p.stockQuantity > 0")
    List<ProductInventory> findAvailableInventories(@Param("product") Product product);

    @Query("SELECT pi FROM ProductInventory pi WHERE pi.location.id = :locationId AND pi.product.id = :productId")
    Optional<ProductInventory> findByLocationAndProduct(@Param("locationId") Long locationId, @Param("productId") Long productId);

    @Query("SELECT pi FROM ProductInventory pi WHERE pi.product.id = :productId AND pi.location.country <> :customerCountry AND pi.stockQuantity > 0")
    Optional<ProductInventory> findInternationalStock(@Param("productId") Long productId, @Param("customerCountry") String customerCountry);
    @Query("SELECT COALESCE(SUM(pi.stockQuantity), 0) FROM ProductInventory pi WHERE pi.product.id = :productId")
    Integer getTotalStockForProduct(@Param("productId") Long productId);
}

