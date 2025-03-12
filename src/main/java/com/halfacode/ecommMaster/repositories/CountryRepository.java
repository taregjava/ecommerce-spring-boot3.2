package com.halfacode.ecommMaster.repositories;

import com.halfacode.ecommMaster.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByName(String name);
}

