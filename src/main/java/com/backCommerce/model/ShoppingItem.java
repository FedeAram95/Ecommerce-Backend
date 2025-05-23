package com.backCommerce.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "SHOPPING_ITEMS")
public class ShoppingItem {

    @EmbeddedId
    private ShoppingItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CART_ID")
    @MapsId("shoppingCartId")
    private ShoppingCart shoppingCart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    @MapsId("productId")
    private Product product;

    @Column(name = "QUANTITY", nullable = false)
    private int quantity;
}

