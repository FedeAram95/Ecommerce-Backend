package com.backCommerce.repository;

import com.backCommerce.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    Optional<ShoppingCart> findByUserId(Long userId);

    @Query("SELECT sc FROM ShoppingCart sc " + "WHERE sc.user.id = :userId AND sc.purchasedAt IS NOT NULL " + "ORDER BY sc.createdAt DESC")
    List<ShoppingCart> findByUserIdAndPurchasedAtIsNotNull(@Param("userId") Long userId);

    Optional<ShoppingCart> findByUserIdAndPurchasedAtIsNull(Long userId);

    @Query("SELECT SUM(si.quantity * p.price) AS total " +
           "FROM ShoppingCart sc " +
           "JOIN sc.shoppingItems si " +
           "JOIN si.product p " +
           "WHERE sc.user.id = :userId AND sc.purchasedAt IS NULL")
    Optional<Double> calculateTotalForActiveCart(@Param("userId") Long userId);

}
