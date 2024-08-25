package com.halfacode.ecommMaster.repositories;

import com.halfacode.ecommMaster.models.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Discount findByCode(String discountCode);
}
