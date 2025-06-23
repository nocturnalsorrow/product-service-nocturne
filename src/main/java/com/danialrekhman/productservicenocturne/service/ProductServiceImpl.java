package com.danialrekhman.productservicenocturne.service;

import com.danialrekhman.productservicenocturne.exception.CustomAccessDeniedException;
import com.danialrekhman.productservicenocturne.exception.DuplicateResourceException;
import com.danialrekhman.productservicenocturne.exception.ResourceNotFoundException;
import com.danialrekhman.productservicenocturne.model.Category;
import com.danialrekhman.productservicenocturne.model.Product;
import com.danialrekhman.productservicenocturne.repository.CategoryRepository;
import com.danialrekhman.productservicenocturne.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public Product createProduct(Product product, Authentication authentication) {
        if(!isAdmin(authentication))
            throw new CustomAccessDeniedException("Only admin can create product.");
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category category = categoryRepository.findById(product.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category with id " + product.getCategory().getId() + " not found for product creation."));
            product.setCategory(category);
        }
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct, Authentication authentication) {
        if(!isAdmin(authentication))
            throw new CustomAccessDeniedException("Only admin can update product.");
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found for update."));
        if (updatedProduct.getName() != null && !updatedProduct.getName().isBlank()) {
            if (!existingProduct.getName().equals(updatedProduct.getName()) && productRepository.existsByName(updatedProduct.getName()))
                throw new DuplicateResourceException("Product with name '" + updatedProduct.getName() + "' already exists..");
            existingProduct.setName(updatedProduct.getName());
        }
        if (updatedProduct.getDescription() != null) {
            if (updatedProduct.getDescription().isBlank())
                throw new IllegalArgumentException("Description cannot be empty or blank.");
            existingProduct.setDescription(updatedProduct.getDescription());
        }
        if (updatedProduct.getPrice() != null) {
            if (updatedProduct.getPrice().compareTo(BigDecimal.ZERO) < 0)
                throw new IllegalArgumentException("Price must be greater than zero.");
            existingProduct.setPrice(updatedProduct.getPrice());
        }
        if (updatedProduct.getCategory() != null && updatedProduct.getCategory().getId() != null) {
            Category category = categoryRepository.findById(updatedProduct.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category with id " + updatedProduct.getCategory().getId() + " not found for product update."));
            existingProduct.setCategory(category);
        }
        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long id, Authentication authentication) {
        if(!isAdmin(authentication))
            throw new CustomAccessDeniedException("Only admin can delete product.");
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found for deletion."));
        productRepository.delete(product);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found for retrieval."));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId))
            throw new ResourceNotFoundException("Category with id " + categoryId + " not found for product retrieval by category ID.");
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
    }
}
