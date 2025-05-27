package com.backCommerce.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.backCommerce.dto.ProductDto;

public interface ProductService {

    ResponseEntity<List<ProductDto>> getProduct(String keyword, int page, int size, Long categoryId, Long typeId);

    ResponseEntity getProductById(Long id);

    ResponseEntity<ProductDto> createProduct(ProductDto product);

    ResponseEntity<String> deleteProduct(Long id);

    ResponseEntity<ProductDto> updateProduct(Long productId, ProductDto product);

}