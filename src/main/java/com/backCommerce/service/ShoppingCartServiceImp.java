package com.backCommerce.service;

import com.backCommerce.dto.ShoppingCartDto;
import com.backCommerce.dto.ShoppingItemsDto;
import com.backCommerce.model.*;
import com.backCommerce.repository.ProductRepository;
import com.backCommerce.repository.ShoppingCartRepository;
import com.backCommerce.repository.ShoppingItemsRepository;
import com.backCommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImp implements ShoppingCartService {

    @Autowired
    ShoppingItemsRepository shoppingItemsRepository;

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public ResponseEntity<ShoppingCartDto> getShoppingCartByUserId(Long userId) {
        try {
            Optional<ShoppingCart> shoppingCartOptional =  shoppingCartRepository.findByUserIdAndPurchasedAtIsNull(userId);
            if (shoppingCartOptional.isPresent()) {

                ShoppingCart shoppingCart = shoppingCartOptional.get();
                List<ShoppingItem> items = shoppingCart.getShoppingItems();

                int totalPrice = getTotalPrice(items);

                ShoppingCartDto shoppingCartDto = convertToDTO(shoppingCart);
                shoppingCartDto.setTotalPrice(totalPrice);
                return new ResponseEntity<>(shoppingCartDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<ShoppingCartDto> createShoppingCart(ShoppingCartDto shoppingCartDto) {
        if (shoppingCartDto.getItems().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<ShoppingCart> existingCart = shoppingCartRepository
                .findByUserIdAndPurchasedAtIsNull(shoppingCartDto.getUserId());

        if (existingCart.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ShoppingCart shoppingCart = convertToEntity(shoppingCartDto);

        final ShoppingCart shoppingCartFinal = shoppingCartRepository.save(shoppingCart);

        List<ShoppingItem> items = shoppingCartDto.getItems().stream()
                .map(itemDto -> {
                    Product product = productRepository.findById(itemDto.getProductId()).orElse(null);
                    if (product == null) return null;

                    ShoppingItem item = new ShoppingItem();
                    item.setId(new ShoppingItemId(shoppingCartFinal.getId(), product.getId()));
                    item.setProduct(product);
                    item.setShoppingCart(shoppingCartFinal);
                    item.setQuantity(itemDto.getQuantity());
                    return item;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        shoppingCartFinal.setShoppingItems(items);
        shoppingItemsRepository.saveAll(shoppingCartFinal.getShoppingItems());
        shoppingCartDto = convertToDTO(shoppingCart);
        shoppingCartDto.setTotalPrice(getTotalPrice(items));
        return new ResponseEntity<>(shoppingCartDto, HttpStatus.CREATED);
    }

    @Override
    @Transactional
    public ResponseEntity<String> cancelShoppingCart(Long id) {
        try {
            // Buscar el carrito
            shoppingCartRepository.deleteById(id);
            return new ResponseEntity<>("Carrito no encontrado", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<ShoppingCartDto> updateShoppingCartProduct(Long shoppingCartId, ShoppingCartDto shoppingCartDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(shoppingCartId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito de compras no encontrado: ID " + shoppingCartId));

        if (shoppingCartDto.getItems() == null || shoppingCartDto.getItems().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Convertir nuevos ítems y asociarlos
        List<ShoppingItem> items = shoppingCartDto.getItems().stream()
                .map(itemDto -> {
                    Product product = productRepository.findById(itemDto.getProductId()).orElse(null);
                    if (product == null) return null;

                    ShoppingItem item = new ShoppingItem();
                    item.setId(new ShoppingItemId(shoppingCart.getId(), product.getId()));
                    item.setProduct(product);
                    item.setShoppingCart(shoppingCart);
                    item.setQuantity(itemDto.getQuantity());
                    return item;
                })
                .filter(Objects::nonNull)
                .toList();

        shoppingCart.getShoppingItems().clear();
        shoppingCart.getShoppingItems().addAll(items);

        shoppingCartRepository.save(shoppingCart);
        shoppingCartDto = convertToDTO(shoppingCart);
        shoppingCartDto.setTotalPrice(getTotalPrice(items));
        return new ResponseEntity<>(shoppingCartDto, HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<ShoppingCartDto> completePurchaseByShoppingCartId(Long shoppingCartId) {
        Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findById(shoppingCartId);

        if (shoppingCartOptional.isPresent()) {
            ShoppingCart shoppingCart = shoppingCartOptional.get();

            List<ShoppingItem> items = shoppingCart.getShoppingItems();

            if (items == null || items.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            shoppingCart.setPurchasedAt(LocalDateTime.now());

            shoppingCartRepository.save(shoppingCart);

            ShoppingCartDto shoppingCartDto = convertToDTO(shoppingCart);
            shoppingCartDto.setTotalPrice(getTotalPrice(items));

            return new ResponseEntity<>(shoppingCartDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private static int getTotalPrice(List<ShoppingItem> items) {
        int totalPrice = 0;
        for (ShoppingItem item : items) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();
            totalPrice += quantity * product.getPrice();
        }
        return totalPrice;
    }


    @Override
    public ResponseEntity<List<ShoppingCartDto>> getHistoryByUserId(Long userId) {
        try {
            List<ShoppingCart> cartHistory = shoppingCartRepository.findByUserIdAndPurchasedAtIsNotNull(userId);
            List<ShoppingCartDto> historyDtoList = cartHistory.stream()
                    .map(this::convertToDTO)
                    .toList();
            return new ResponseEntity<List<ShoppingCartDto>>(historyDtoList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ShoppingCart convertToEntity(ShoppingCartDto shoppingCartDto) {


        User user = userRepository.findById(shoppingCartDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setCreatedAt(LocalDateTime.now());
       return shoppingCart;
    }

    private ShoppingCartDto convertToDTO(ShoppingCart shoppingCart) {
        ShoppingCartDto dto = new ShoppingCartDto();
        dto.setId(shoppingCart.getId());
        dto.setUserId(shoppingCart.getUser().getId());
        dto.setCreatedAt(shoppingCart.getCreatedAt());
        dto.setPurchasedAt(shoppingCart.getPurchasedAt());

        List<ShoppingItemsDto> itemsDto = shoppingCart.getShoppingItems().stream()
                .map(item -> new ShoppingItemsDto(item.getProduct().getId(), item.getQuantity()))
                .collect(Collectors.toList());

        dto.setItems(itemsDto);
        return dto;
    }
}