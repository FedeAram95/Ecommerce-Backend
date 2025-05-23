package com.backCommerce.controller;

import com.backCommerce.dto.LoginRequestDto;
import com.backCommerce.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "AuthController", description = "Endpoints for authentication")
@RestController
@RequestMapping("/auth") // Bajo el path /auth se encuentran los endpoints de autenticación
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Login endpoint", description = "Autentica usuarios y devuelve información del usuario junto a su rol")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        return authService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }
}