package com.halfacode.ecommMaster.repositories;

import com.halfacode.ecommMaster.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
