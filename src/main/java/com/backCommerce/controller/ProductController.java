package com.backCommerce.controller;

import com.backCommerce.dto.ProductDto;
import com.backCommerce.model.Product;
import com.backCommerce.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

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


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> create(
        @RequestPart("product") ProductDto productDto,
        @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) throws JsonProcessingException {
        return productService.createProduct(productDto, imageFile);
    }


	@PutMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ProductDto> updateProduct(
	    @PathVariable Long productId,
        @RequestPart("product") ProductDto productDto,
        @RequestPart(value = "image", required = false) MultipartFile imageFile) throws JsonProcessingException {	
	    return productService.updateProduct(productId, productDto, imageFile);
	}
    

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId);
    }


}