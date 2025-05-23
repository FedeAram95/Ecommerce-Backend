package com.backCommerce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypesDto {

    @NotNull(message = "El ID no puede estar vacío")
    private Long id;

    @NotNull(message = "La descripción no puede estar vacía")
    @Size(min = 1, max = 255, message = "La descripción debe tener entre 1 y 255 caracteres")
    private String description;

    @NotNull(message = "El ID de la categoría no puede estar vacío")
    private Long categoryId;

}   