package com.danialrekhman.productservicenocturne.service;

import com.danialrekhman.productservicenocturne.model.Category;
import com.danialrekhman.productservicenocturne.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(Category category) {
        // Безопасно работаем с subcategories, если они заданы
        if (category.getSubcategories() != null) {
            for (Category sub : category.getSubcategories()) {
                sub.setParent(category); // Важно!
            }
        }

        // Не передавать ID вручную
        if (category.getId() != null && categoryRepository.existsById(category.getId())) {
            throw new IllegalArgumentException("Category with this id already exists");
        }

        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> updateCategory(Long id, Category updatedCategory) {

        return categoryRepository.findById(id).map(existingCategory -> {
            // Проверка на дубликат имени, если имя меняется
            if (!existingCategory.getName().equals(updatedCategory.getName()) &&
                    categoryRepository.existsByName(updatedCategory.getName())) {
                throw new IllegalArgumentException("Category with this name already exists");
            }

            // Обновляем имя
            existingCategory.setName(updatedCategory.getName());

            // Обновляем родительскую категорию, если это не сам объект и не null
            if (updatedCategory.getParent() != null
                    && updatedCategory.getParent().getId() != null
                    && !updatedCategory.getParent().getId().equals(id)) {
                existingCategory.setParent(updatedCategory.getParent());
            }

            // Сохраняем изменения
            return categoryRepository.save(existingCategory);
        });
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public Optional<Category> getCategoryById(Long id) {

        if (!categoryRepository.existsById(id))
            throw new IllegalArgumentException("Category with this id does not exist");

        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> getAllCategories() {

        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getSubcategories(Long parentId) {

        if (!categoryRepository.existsById(parentId))
            throw new IllegalArgumentException("Category with this id does not exist");

        return categoryRepository.findAllByParentId(parentId);
    }

    @Override
    public List<Category> getAllParentCategories() {

        return categoryRepository.findAllByParentIsNull();
    }
}