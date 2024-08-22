package com.halfacode.ecommMaster.repositories;
import com.halfacode.ecommMaster.dto.ProductDTO;
import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct(ProductDTO product);
}
