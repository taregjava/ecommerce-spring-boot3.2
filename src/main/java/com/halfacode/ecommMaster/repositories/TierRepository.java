package com.halfacode.ecommMaster.repositories;

import com.halfacode.ecommMaster.models.Tier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TierRepository extends JpaRepository<Tier, Long> {
    Optional<Tier> findByName(String name);
}