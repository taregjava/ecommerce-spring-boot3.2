package com.halfacode.ecommMaster.dto;



import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private Boolean isAvailable;
    private double averageRating;
    private double basePrice;
    private int salesCount;
    private CategoryDTO category;
    private List<ReviewDTO> reviews;
}
