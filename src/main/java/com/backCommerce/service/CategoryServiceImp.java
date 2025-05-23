package com.backCommerce.service;

import com.backCommerce.dto.CategoryDto;
import com.backCommerce.model.Category;
import com.backCommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<List<CategoryDto>> getCategories() {
        try {
            List<CategoryDto> categories = categoryRepository.findAll()
                    .stream()
                    .map(this::convertToDTO)
                    .toList();
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            // Manejo de excepciones
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<CategoryDto> getCategoryById(Long id) {
        try {
            CategoryDto categoryDto = categoryRepository.findById(id)
                    .map(this::convertToDTO)
                    .orElse(null);
            if (categoryDto != null) {
                return new ResponseEntity<>(categoryDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Manejo de excepciones
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<CategoryDto> createCategory(CategoryDto categoryDto) {
        try {
            Category category = convertToEntity(categoryDto);
            category = categoryRepository.save(category);
            categoryDto = convertToDTO(category);
            return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
        } catch (Exception e) {
            // Manejo de excepciones
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> deleteCategory(Long id) {
        try {
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isPresent()) {
                categoryRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Manejo de excepciones
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<CategoryDto> updateCategory(Long categoryId, CategoryDto categoryDto) {
        try {
            Category category = convertToEntity(categoryDto);
            Optional<Category> existingCategory = categoryRepository.findById(categoryId);
            if (existingCategory.isPresent()) {
                Category updatedCategory = existingCategory.get();
                updatedCategory.setDescription(category.getDescription());
                categoryRepository.save(updatedCategory);
                return new ResponseEntity<>(convertToDTO(updatedCategory), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Manejo de excepciones
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private CategoryDto convertToDTO(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setDescription(category.getDescription());

        return categoryDto;
    }

    private Category convertToEntity(CategoryDto categoryDto) {
        Category category = new Category();

        category.setId(categoryDto.getId());
        category.setDescription(categoryDto.getDescription());

        return category;
    }
}
