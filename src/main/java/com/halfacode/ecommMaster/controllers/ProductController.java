package com.halfacode.ecommMaster.controllers;

import com.halfacode.ecommMaster.dto.ProductDTO;
import com.halfacode.ecommMaster.services.ProductService;
import com.halfacode.ecommMaster.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Optional<ProductDTO> productDTO = Optional.ofNullable(productService.getProductById(id));
        return productDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

  /*  @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDTO> addProduct(
            @RequestPart("product") ProductDTO productDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            ProductDTO createdProduct = productService.addProduct(productDTO, images);
            return ResponseEntity.ok(createdProduct);
        } catch (RuntimeException | IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }*/
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) {
      try {
          ProductDTO createdProduct = productService.addProduct(productDTO);
          return ResponseEntity.ok(createdProduct);
      } catch (RuntimeException e) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
      }
  }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") ProductDTO updatedProductDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            ProductDTO product = productService.updateProduct(id, updatedProductDTO, images);
            return ResponseEntity.ok(product);
        } catch (RuntimeException | IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping(value = "/upload-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImages(
            @RequestParam("productId") Long productId,
            @RequestPart("images") List<MultipartFile> images) {
        try {
            productService.addImagesToProduct(productId, images);
            return ResponseEntity.ok("Images uploaded successfully!");
        } catch (RuntimeException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload images: " + e.getMessage());
        }
    }

}
