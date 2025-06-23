package com.danialrekhman.productservicenocturne.repository;

import com.danialrekhman.productservicenocturne.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ProductRepository  extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long categoryId);

    boolean existsByName(String name);

    List<Product> findByNameContainingIgnoreCase(String name);
}