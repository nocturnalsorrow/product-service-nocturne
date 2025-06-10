package com.danialrekhman.productservicenocturne.service;

import com.danialrekhman.productservicenocturne.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Category createCategory(Category category);

    Optional<Category> updateCategory(Long id, Category updatedCategory);

    void deleteCategory(Long id);

    Optional<Category> getCategoryById(Long id);

    List<Category> getAllCategories();

    List<Category> getSubcategories(Long parentId);

    List<Category> getAllParentCategories();
}