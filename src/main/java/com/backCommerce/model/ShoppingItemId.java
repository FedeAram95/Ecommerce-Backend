package com.backCommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingItemId implements Serializable {

    @Column(name = "CART_ID")
    private Long shoppingCartId;

    @Column(name = "PRODUCT_ID")
    private Long productId;
}