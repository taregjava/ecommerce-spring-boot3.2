package com.halfacode.ecommMaster.mapper;

import com.halfacode.ecommMaster.dto.CategoryDTO;
import com.halfacode.ecommMaster.dto.ProductDTO;
import com.halfacode.ecommMaster.models.Category;
import com.halfacode.ecommMaster.models.Product;

public class ProductMapper {

    private ProductMapper() {
        // Private constructor to prevent instantiation
    }

    public static ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .isAvailable(product.getIsAvailable())
                .averageRating(product.getAverageRating())
                .basePrice(product.getBasePrice())
                .salesCount(product.getSalesCount())
                .category(product.getCategory() != null ?
                        new CategoryDTO(product.getCategory().getName(), product.getCategory().getDescription()) : null)
                .build();
    }

    public static Product toEntity(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }

        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity());
        product.setIsAvailable(productDTO.getIsAvailable());
        product.setBasePrice(productDTO.getBasePrice());
        product.setSalesCount(productDTO.getSalesCount());

        CategoryDTO categoryDTO = productDTO.getCategory();
        if (categoryDTO != null) {
            Category category = new Category(categoryDTO.getName(), categoryDTO.getDescription());
            product.setCategory(category);
        }

        return product;
    }

    public static ProductDTO toCartDTO(Product product) {
        if (product == null) {
            return null;
        }

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .isAvailable(product.getIsAvailable())
                .averageRating(product.getAverageRating())
                .basePrice(product.getBasePrice())
                .salesCount(product.getSalesCount())
                .category(product.getCategory() != null ?
                        new CategoryDTO(product.getCategory().getName(), product.getCategory().getDescription()) : null)
                .build();
    }


}
