package com.backCommerce.service;

import com.backCommerce.dto.CategoryDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {

    ResponseEntity<List<CategoryDto>> getCategories();

    ResponseEntity<CategoryDto> getCategoryById(Long id);

    ResponseEntity<CategoryDto> createCategory(CategoryDto CategoryDto);

    ResponseEntity<Void> deleteCategory(Long id);

    ResponseEntity<CategoryDto> updateCategory(Long categoryId, CategoryDto category);

}
