package com.danialrekhman.productservicenocturne.controller;

import com.danialrekhman.productservicenocturne.dto.ProductCreateUpdateDTO;
import com.danialrekhman.productservicenocturne.dto.ProductDTO;
import com.danialrekhman.productservicenocturne.dto.ProductImageDTO;
import com.danialrekhman.productservicenocturne.model.Category;
import com.danialrekhman.productservicenocturne.model.Product;
import com.danialrekhman.productservicenocturne.service.ProductService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {

        return productService.getProductById(productId)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<ProductDTO>> searchProducts(@PathVariable String keyword) {
        
        return ResponseEntity.ok(
                productService.searchProducts(keyword)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList()));
    }
    
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        
        return ResponseEntity.ok(
                productService.getAllProducts()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList()));

    }
    
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        
        return ResponseEntity.ok(
                productService.getProductsByCategory(categoryId)
                        .stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductCreateUpdateDTO dto) {

        Product product = productService.createProduct(fromCreateUpdateDTO(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(product));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductCreateUpdateDTO dto) {

        return productService.updateProduct(productId, fromCreateUpdateDTO(dto))
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {

        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    //Mappers

    private ProductDTO toDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .images(product.getImages().stream()
                        .map(img -> ProductImageDTO.builder()
                                .id(img.getId())
                                .imageUrl(img.getImageUrl())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private Product fromCreateUpdateDTO(ProductCreateUpdateDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());

        Category category = new Category();
        category.setId(dto.getCategoryId());
        product.setCategory(category);

        return product;
    }
}