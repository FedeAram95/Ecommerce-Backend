package com.backCommerce.service;

import com.backCommerce.dto.ShoppingCartDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShoppingCartService {

    ResponseEntity<ShoppingCartDto> getShoppingCartByUserId(Long id);

    ResponseEntity<ShoppingCartDto> createShoppingCart(ShoppingCartDto shoppingCartDto);

    ResponseEntity<String> cancelShoppingCart(Long id);

    ResponseEntity<ShoppingCartDto> updateShoppingCartProduct(Long shoppingCartId, ShoppingCartDto shoppingCartDto);

    ResponseEntity<ShoppingCartDto> completePurchaseByShoppingCartId(Long shoppingCartId);

    ResponseEntity<List<ShoppingCartDto>> getHistoryByUserId(Long userId);

}