package com.backCommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import com.backCommerce.model.enums.PaymentStatus;

@Entity
@Getter
@Setter
@Table(name = "PAYMENTS", schema = "PAYMENTS")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PAYPAL_PAYMENT_ID", unique = true, nullable = false)
    private String paypalPaymentId;
    @ManyToOne
    @JoinColumn(name = "SHOPPING_CART_ID", nullable = false)
    private ShoppingCart shoppingCart;

    @Column(name = "AMOUNT", nullable = false)
    private Double amount;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
