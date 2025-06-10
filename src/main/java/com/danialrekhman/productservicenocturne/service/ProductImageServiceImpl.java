package com.danialrekhman.productservicenocturne.service;

import com.danialrekhman.productservicenocturne.model.Product;
import com.danialrekhman.productservicenocturne.model.ProductImage;
import com.danialrekhman.productservicenocturne.repository.ProductImageRepository;
import com.danialrekhman.productservicenocturne.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductImageServiceImpl implements ProductImageService {

    ProductImageRepository productImageRepository;

    ProductRepository productRepository;

    public ProductImageServiceImpl(ProductImageRepository productImageRepository, ProductRepository productRepository) {
        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ProductImage addImageToProduct(Long productId, ProductImage image) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        image.setProduct(product);               // привязка изображения к продукту
        product.getImages().add(image);          // добавляем изображение в список
        productRepository.save(product);         // можно сохранить продукт — каскад сохранит и изображение

        return image;
    }

    @Override
    public void deleteImage(Long imageId) {

        if(!productImageRepository.existsById(imageId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");

        productImageRepository.deleteById(imageId);
    }

    @Override
    public List<ProductImage> getImagesByProduct(Long productId) {

        if(!productRepository.existsById(productId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");

        return productImageRepository.findProductImagesByProduct_Id(productId);
    }
}