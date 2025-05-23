package com.backCommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backCommerce.model.Payment;
import com.backCommerce.model.ShoppingCart;
import com.backCommerce.model.enums.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaypalPaymentId(String paypalPaymentId);
    Payment findByShoppingCartAndStatus(ShoppingCart shoppingCart, PaymentStatus status);
}