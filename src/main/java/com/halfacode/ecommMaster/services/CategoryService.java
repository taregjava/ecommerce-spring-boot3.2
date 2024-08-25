package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.dto.CategoryDTO;
import com.halfacode.ecommMaster.mapper.CategoryMapper;
import com.halfacode.ecommMaster.models.Category;
import com.halfacode.ecommMaster.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryMapper::toCategoryDTO)
                .collect(Collectors.toList());
    }

    public Optional<CategoryDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryMapper::toCategoryDTO);
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = CategoryMapper.toCategory(categoryDTO);
        return CategoryMapper.toCategoryDTO(categoryRepository.save(category));
    }

    public CategoryDTO updateCategory(Long id, CategoryDTO updatedCategoryDTO) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(updatedCategoryDTO.getName());
                    existingCategory.setDescription(updatedCategoryDTO.getDescription());
                    return CategoryMapper.toCategoryDTO(categoryRepository.save(existingCategory));
                })
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
