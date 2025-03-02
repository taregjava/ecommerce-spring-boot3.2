package com.halfacode.ecommMaster.repositories;

import com.halfacode.ecommMaster.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Object> findByName(String categoryName);
}
