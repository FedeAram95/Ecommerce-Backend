package com.backCommerce.controller;

import com.backCommerce.dto.CategoryDto;
import com.backCommerce.model.Category;
import com.backCommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "CategoryController", description = "Endpoints para la gestión de categorías")
@RestController
@RequestMapping(path = "/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Obtener todas las categorías", description = "Devuelve una lista de todas las categorías disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class),
                            examples = @ExampleObject(value = "[{\"id\":1, \"name\":\"Electronics\"}, {\"id\":2, \"name\":\"Books\"}]")
                    ))
    })
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll() {
        return categoryService.getCategories();
    }

    @Operation(summary = "Obtener una categoría por ID", description = "Devuelve una categoría basada en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class),
                            examples = @ExampleObject(value = "{\"id\":1, \"name\":\"Electronics\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getById(@PathVariable Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @Operation(summary = "Crear una categoría", description = "Crea una nueva categoría")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class),
                            examples = @ExampleObject(value = "{\"id\":1, \"name\":\"New Category\"}")
                    )),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<CategoryDto> saveCategory(@RequestBody CategoryDto category) {
        return categoryService.createCategory(category);
    }


    @Operation(summary = "Actualizar una categoría por ID", description = "Actualiza una categoría existente basada en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class),
                            examples = @ExampleObject(value = "{\"id\":1, \"name\":\"Updated Category\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDto category) {
        return categoryService.updateCategory(categoryId, category);
    }

    @Operation(summary = "Eliminar una categoría por ID", description = "Elimina una categoría existente basada en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        return categoryService.deleteCategory(categoryId);
    }
}