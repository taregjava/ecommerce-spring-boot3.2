package com.halfacode.ecommMaster.repositories;

import com.halfacode.ecommMaster.models.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Promotion findByCodeAndActiveTrue(String code);
    /**
     * Finds a promotion by its code.
     * @param code the promotion code
     * @return the Promotion if found, otherwise null
     */

    Promotion findByCode(String code);
}
