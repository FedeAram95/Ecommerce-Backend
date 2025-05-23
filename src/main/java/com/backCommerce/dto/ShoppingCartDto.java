package com.backCommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDto {

    private Long id;

    @NotNull(message = "El ID del usuario no puede estar vacío")
    private Long userId;

    @NotEmpty(message = "El carrito debe contener al menos un producto")
    private List<ShoppingItemsDto> items;

    @PastOrPresent(message = "La fecha de creación no puede ser en el futuro")
    private LocalDateTime createdAt;
    
    @PastOrPresent(message = "La fecha de eliminacion no puede ser en el futuro")
    private LocalDateTime purchasedAt;

    private int totalPrice;

}
