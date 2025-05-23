package com.backCommerce.service;

import com.backCommerce.dto.ProductDto;
import com.backCommerce.dto.ShoppingCartDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    ResponseEntity<List<ProductDto>> getProduct(String keyword, int page, int size, Long categoryId, Long typeId);

    ResponseEntity getProductById(Long id);

    ResponseEntity<ProductDto> createProduct(ProductDto product, MultipartFile imageFile);

    ResponseEntity<String> deleteProduct(Long id);

    ResponseEntity<ProductDto> updateProduct(Long productId, ProductDto product, MultipartFile imageFile);

}