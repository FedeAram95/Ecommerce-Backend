package com.backCommerce.controller;

import com.backCommerce.dto.ShoppingCartDto;
import com.backCommerce.model.ShoppingCart;
import com.backCommerce.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ShoppingCartController", description = "Endpoints para la gestión de carritos")
@RestController
@RequestMapping(path = "/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Operation(summary = "Obtener un carrito por ID de usuario", description = "Devuelve un carrito basado en el ID del usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito obtenido exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ShoppingCart.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    })
    @Transactional
    @GetMapping("/user/{userId}")
    public ResponseEntity<ShoppingCartDto> getByUserId(@PathVariable Long userId) {
        return shoppingCartService.getShoppingCartByUserId(userId);
    }

    @Operation(summary = "Crear un carrito", description = "Crea un carrito nuevo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Carrito creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ShoppingCart.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<ShoppingCartDto> createShoppingCart(@RequestBody ShoppingCartDto shoppingCartDto) {
        return shoppingCartService.createShoppingCart(shoppingCartDto);
    }

    @Operation(summary = "Actualizar un carrito por ID", description = "Actualiza un carrito existente basado en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ShoppingCart.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PutMapping("/{shoppingCartId}")
    public ResponseEntity<ShoppingCartDto> updateShoppingCart(@PathVariable Long shoppingCartId, @RequestBody ShoppingCartDto shoppingCartDto) {
        return shoppingCartService.updateShoppingCartProduct(shoppingCartId, shoppingCartDto);
    }

    @Operation(summary = "Eliminar un carrito por ID", description = "Elimina un carrito existente basado en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito eliminado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ShoppingCart.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    })
    @DeleteMapping("/{shoppingCartId}")
    public ResponseEntity<String> cancelShoppingCart(@PathVariable Long shoppingCartId) {
        return shoppingCartService.cancelShoppingCart(shoppingCartId);
    }

    @Operation(summary = "Completar compra de carrito", description = "Marca el carrito como comprado y actualiza el estado del carrito a completo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compra completada exitosamente",
                    content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado o no abierto")
    })
    @PutMapping("/complete-purchase/{shoppingCartId}")
    public ResponseEntity<ShoppingCartDto> completePurchase(@PathVariable Long shoppingCartId) {
        // Simplemente delega la respuesta al servicio
        return shoppingCartService.completePurchaseByShoppingCartId(shoppingCartId);
    }


    @GetMapping("/history/{userId}")
    public ResponseEntity<List<ShoppingCartDto>> getHistoryByUserId(@PathVariable Long userId) {
        return shoppingCartService.getHistoryByUserId(userId);
    }

}