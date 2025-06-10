package com.danialrekhman.productservicenocturne.service;

import com.danialrekhman.productservicenocturne.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

        Product createProduct(Product product);

        Optional<Product> updateProduct(Long id, Product updatedProduct);

        void deleteProduct(Long id);

        Optional<Product> getProductById(Long id);

        List<Product> getAllProducts();

        List<Product> getProductsByCategory(Long categoryId);

        List<Product> searchProducts(String keyword); // поиск по имени/описанию

}