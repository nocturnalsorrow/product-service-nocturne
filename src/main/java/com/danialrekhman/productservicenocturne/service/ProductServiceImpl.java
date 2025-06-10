package com.danialrekhman.productservicenocturne.service;

import com.danialrekhman.productservicenocturne.model.Product;
import com.danialrekhman.productservicenocturne.repository.CategoryRepository;
import com.danialrekhman.productservicenocturne.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;

    CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product createProduct(Product product) {

        return productRepository.save(product);
    }

    @Override
    public Optional<Product> updateProduct(Long id, Product updatedProduct) {

        return productRepository.findById(id).map(existingProduct -> {
            // Проверка на дубликат имени, если имя меняется
            if (!existingProduct.getName().equals(updatedProduct.getName()) &&
                    productRepository.existsByName(updatedProduct.getName())
            ) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product with this name already exists");
            }
            if (updatedProduct.getName() != null && !updatedProduct.getName().isBlank())
                existingProduct.setName(updatedProduct.getName());

            if (updatedProduct.getDescription() != null && !updatedProduct.getDescription().isBlank())
                existingProduct.setDescription(updatedProduct.getDescription());

            if (updatedProduct.getPrice() != null)
                existingProduct.setPrice(updatedProduct.getPrice());

            if (updatedProduct.getCategory() != null)
                existingProduct.setCategory(updatedProduct.getCategory());

            return productRepository.save(existingProduct);
        });
    }

    @Override
    public void deleteProduct(Long id) {

        if(!productRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        productRepository.deleteById(id);
    }

    @Override
    public Optional<Product> getProductById(Long id) {

        return Optional.ofNullable(productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")));
    }

    @Override
    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {

        if(!categoryRepository.existsById(categoryId))
            throw new IllegalArgumentException("Category with this id does not exist");

        return productRepository.findByCategory_Id(categoryId);
    }

    @Override
    public List<Product> searchProducts(String keyword) {

        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
}