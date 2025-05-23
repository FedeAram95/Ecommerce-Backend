package com.backCommerce.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;

    @NotNull(message = "Nombre de producto no puede estar vacío")
    @Size(min = 1, max = 255, message = "Debe tener entre 1 y 255 caracteres")
    private String name;

    @NotNull(message = "Descripción no puede estar vacía")
    private String description;

    @Min(value = 0, message = "Stock debe ser igual o mayor a 0")
    private int stock;

    @Min(value = 0, message = "Precio debe ser igual o mayor a 0")
    private int price;

    // @NotNull(message = "Type ID no puede ser nulo")
    private Long typeId;

    private Long categoryId; // Se setea automáticamente al hacer GET

    @NotNull(message = "Image no puede estar vacía")
    private String image;

    @NotNull(message = "Tags no pueden estar vacíos")
    private String tags;
}