package com.danialrekhman.productservicenocturne.controller;

import com.danialrekhman.productservicenocturne.dto.ProductImageDTO;
import com.danialrekhman.productservicenocturne.model.ProductImage;
import com.danialrekhman.productservicenocturne.service.ProductImageService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductImageController {

    ProductImageService productImageService;

    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @PostMapping("/{productId}/images")
    public ResponseEntity<ProductImageDTO> addImageToProduct(
            @PathVariable Long productId,
            @RequestBody ProductImageDTO dto) {

        ProductImage image = new ProductImage();
        image.setImageUrl(dto.getImageUrl());
        ProductImage saved = productImageService.addImageToProduct(productId, image);
        return ResponseEntity.ok(toDTO(saved));
    }

    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<Void> deleteImage( @PathVariable Long imageId) {

        productImageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{productId}/images")
    public ResponseEntity<List<ProductImageDTO>> getImagesByProduct(@PathVariable Long productId) {

        return ResponseEntity.ok(
                productImageService.getImagesByProduct(productId)
                        .stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList()));
    }

    // Mapper
    private ProductImageDTO toDTO(ProductImage image) {
        return ProductImageDTO.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .build();
    }
}