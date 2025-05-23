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

    @Operation(summary = "Obtener todos los productos", description = "Devuelve una lista de todos los productos disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class),
                            examples = @ExampleObject(value = "[{\"id\":1, \"name\":\"Laptop\"}, {\"id\":2, \"name\":\"Smartphone\"}]")
                    ))
    })
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long typeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productService.getProduct(keyword, page, size, categoryId, typeId);
    }


    @Operation(summary = "Obtener un producto por ID", description = "Devuelve un producto basado en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto obtenido exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class),
                            examples = @ExampleObject(value = "{\"id\":1, \"name\":\"Laptop\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{productId}")
    public ResponseEntity getById(@PathVariable Long productId) {
        return productService.getProductById(productId);
    }



    @Operation(summary = "Crear un producto", description = "Crea un producto nuevo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class),
                            examples = @ExampleObject(value = "{\"id\":1, \"name\":\"Nuevo Producto\"}")
                    )),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> create(
    @RequestParam("product") String productJson,
    @RequestParam(value = "image", required = false) MultipartFile imageFile) throws JsonProcessingException {
        
            // Convertir JSON a objeto ProductDto
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDto product = objectMapper.readValue(productJson, ProductDto.class);
        
        return productService.createProduct(product, imageFile);
    }

    @Operation(summary = "Actualizar un producto por ID", description = "Actualiza un producto existente basado en su ID")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Product.class),
                        examples = @ExampleObject(value = "{\"id\":1, \"name\":\"Producto Actualizado\"}")
                )),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida")
})
@PutMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<ProductDto> updateProduct(
    @PathVariable Long productId,
    @RequestParam("product") String productJson, // Recibe el JSON como String
    @RequestParam(value = "image", required = false) MultipartFile imageFile) throws JsonProcessingException {

    // Convertir JSON a objeto ProductDto
    ObjectMapper objectMapper = new ObjectMapper();
    ProductDto product = objectMapper.readValue(productJson, ProductDto.class);

    return productService.updateProduct(productId, product, imageFile);
}
    @Operation(summary = "Eliminar un producto por ID", description = "Elimina un producto existente basado en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class),
                            examples = @ExampleObject(value = "{\"id\":1, \"name\":\"Laptop\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId);
    }


}