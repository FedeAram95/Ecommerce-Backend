package com.backCommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backCommerce.dto.ProductDto;
import com.backCommerce.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "ProductController", description = "Endpoints para la gestión de productos")
@RestController
@RequestMapping(path = "/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long typeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productService.getProduct(keyword, page, size, categoryId, typeId);
    }


    @GetMapping("/{productId}")
    public ResponseEntity getById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }


    @PostMapping()
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto productDto) throws JsonProcessingException {
        return productService.createProduct(productDto);
    }


	@PutMapping(value = "/{productId}")
	public ResponseEntity<ProductDto> updateProduct(@PathVariable Long productId,
        @RequestBody ProductDto productDto) throws JsonProcessingException {	
	    return productService.updateProduct(productId, productDto);
	}
    

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId);
    }


}