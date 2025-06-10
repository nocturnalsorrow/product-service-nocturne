package com.danialrekhman.productservicenocturne.service;

import com.danialrekhman.productservicenocturne.model.ProductImage;

import java.util.List;

public interface ProductImageService {

    ProductImage addImageToProduct(Long productId, ProductImage image);

    void deleteImage(Long imageId);

    List<ProductImage> getImagesByProduct(Long productId);
}