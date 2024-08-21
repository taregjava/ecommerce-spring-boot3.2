package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.Category;
import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductSearchService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findProducts(String query, Category category, Double minPrice, Double maxPrice, Integer minRating) {
        List<Product> products = productRepository.findProducts(query, category, minPrice, maxPrice);

        if (minRating != null) {
            return products.stream()
                    .filter(p -> p.getAverageRating() >= minRating)
                    .collect(Collectors.toList());
        }

        return products;
    }
}
