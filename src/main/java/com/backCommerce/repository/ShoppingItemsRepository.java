package com.backCommerce.repository;

import com.backCommerce.model.ShoppingItemId;
import com.backCommerce.model.ShoppingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;


@Repository
public interface ShoppingItemsRepository extends JpaRepository<ShoppingItem, ShoppingItemId> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM \"SHOPPING_ITEMS\" WHERE \"CART_ID\" = :cartId", nativeQuery = true)
    void deleteAllByShoppingCartId(@Param("cartId") Long cartId);

}
