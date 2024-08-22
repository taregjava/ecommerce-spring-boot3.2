package com.halfacode.ecommMaster.mapper;

import com.halfacode.ecommMaster.dto.CategoryDTO;
import com.halfacode.ecommMaster.models.Category;

public class CategoryMapper {

    private CategoryMapper(){
        // Private constructor to prevent instantiation
    }

    public static CategoryDTO toCategoryDTO(Category category) {
        if (category == null) {
            return null;
        }

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());

        return categoryDTO;
    }

    public static Category toCategory(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }

        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        return category;
    }
}
