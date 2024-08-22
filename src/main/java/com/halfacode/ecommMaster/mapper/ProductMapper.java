package com.halfacode.ecommMaster.mapper;

import com.halfacode.ecommMaster.dto.ProductDTO;
import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.dto.CategoryDTO;
import com.halfacode.ecommMaster.models.Category;


public class ProductMapper {

    private ProductMapper() {
        // Private constructor to prevent instantiation
    }

    public static ProductDTO toProductDTO(Product product) {
        if (product == null) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setStockQuantity(product.getStockQuantity());
        productDTO.setIsAvailable(product.getIsAvailable());
        productDTO.setAverageRating(product.getAverageRating());
        productDTO.setBasePrice(product.getBasePrice());
        productDTO.setSalesCount(product.getSalesCount());

        // Convert Category to CategoryDTO
        Category category = product.getCategory();
        if (category != null) {
            CategoryDTO categoryDTO = new CategoryDTO(category.getName(), category.getDescription());
            productDTO.setCategory(categoryDTO);
        }

        // Assuming you have a method to map List<Review> to List<ReviewDTO>
       // productDTO.setReviews(ReviewMapper.toReviewDTOs(product.getReviews()));

        return productDTO;
    }

    public static Product toProduct(ProductDTO productDTO) {
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

        // Convert CategoryDTO to Category
        CategoryDTO categoryDTO = productDTO.getCategory();
        if (categoryDTO != null) {
            Category category = new Category(categoryDTO.getName(), categoryDTO.getDescription());
            product.setCategory(category);
        }

        // Assuming you have a method to map List<ReviewDTO> to List<Review>
      //  product.setReviews(ReviewMapper.toReviews(productDTO.getReviews()));

        return product;
    }
}
