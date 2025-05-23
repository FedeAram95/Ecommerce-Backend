package com.backCommerce.dto;

import com.backCommerce.model.RoleType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NotNull(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 1, max = 255, message = "El nombre de usuario debe tener entre 1 y 255 caracteres")
    private String username;

    @NotNull(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotNull(message = "Los roles no pueden estar vacíos")
    private List<RoleType> roles;

    @PastOrPresent(message = "La fecha de creación no puede ser en el futuro")
    private LocalDateTime creationDate;
}
