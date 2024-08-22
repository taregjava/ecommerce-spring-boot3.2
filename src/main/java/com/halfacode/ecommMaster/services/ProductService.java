package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.dto.CategoryDTO;
import com.halfacode.ecommMaster.dto.ProductDTO;
import com.halfacode.ecommMaster.mapper.CategoryMapper;
import com.halfacode.ecommMaster.mapper.ProductMapper;
import com.halfacode.ecommMaster.models.Category;
import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> {
                    ProductDTO dto = ProductMapper.toProductDTO(product);
                    // Handle null category
                    if (dto.getCategory() == null) {
                        dto.setCategory(new CategoryDTO(0L, "Uncategorized", ""));
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(ProductMapper::toProductDTO)
                .orElse(null);
    }

    public ProductDTO addProduct(ProductDTO productDTO) {
        Product product = ProductMapper.toProduct(productDTO);
        return ProductMapper.toProductDTO(productRepository.save(product));
    }

    public ProductDTO updateProduct(Long id, ProductDTO updatedProductDTO) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProductDTO.getName());
                    product.setDescription(updatedProductDTO.getDescription());
                    product.setPrice(updatedProductDTO.getPrice());
                    product.setStockQuantity(updatedProductDTO.getStockQuantity());
                    product.setIsAvailable(updatedProductDTO.getIsAvailable());

                    // Convert CategoryDTO to Category and set it
                    Category category = CategoryMapper.toCategory(updatedProductDTO.getCategory());
                    product.setCategory(category);

                    return ProductMapper.toProductDTO(productRepository.save(product));
                })
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }


    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void updateStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);
    }
}
