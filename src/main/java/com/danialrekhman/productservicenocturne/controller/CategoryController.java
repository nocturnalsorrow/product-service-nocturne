package com.danialrekhman.productservicenocturne.controller;

import com.danialrekhman.productservicenocturne.dto.CategoryCreateUpdateDTO;
import com.danialrekhman.productservicenocturne.dto.CategoryDTO;
import com.danialrekhman.productservicenocturne.model.Category;
import com.danialrekhman.productservicenocturne.service.CategoryService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDTO> getAllCategories() {

        return categoryService.getAllCategories()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/parent/{parentId}")
    public List<CategoryDTO> getSubcategories(@PathVariable Long parentId) {

        return categoryService.getSubcategories(parentId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {

        return categoryService.getCategoryById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/parents")
    public List<CategoryDTO> getAllParentCategories() {

        return categoryService.getAllParentCategories()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryCreateUpdateDTO dto) {

        Category created = categoryService.createCategory(fromDTO(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryCreateUpdateDTO dto) {

        return categoryService.updateCategory(id, fromDTO(dto))
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {

        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    private CategoryDTO toDTO(Category category) {

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .subcategories(category.getSubcategories().stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private Category fromDTO(CategoryCreateUpdateDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        if (dto.getParentId() != null) {
            // Можно загружать родителя из базы, чтобы установить объект, например:
            category.setParent(new Category());
            category.getParent().setId(dto.getParentId());
        }
        return category;
    }
}