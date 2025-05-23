package com.backCommerce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @NotNull(message = "username es requerido")
    @NotBlank(message = "username no puede estar vacío")
    @Size(min = 1, max = 255, message = "username debe tener 1-255 caracteres")
    String username;

    @NotNull(message = "password es requerido")
    @NotBlank(message = "password no puede estar vacío")
    @Size(min = 6, message = "password debe tener mínimo 6 caracteres")
    String password;
}