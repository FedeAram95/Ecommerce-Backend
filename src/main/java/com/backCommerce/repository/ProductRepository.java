package com.backCommerce.repository;

import com.backCommerce.model.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT * FROM \"PRODUCTS\".\"PRODUCT\" WHERE to_tsvector(\"TAGS\") @@ to_tsquery(:keyword)", nativeQuery = true)
    List<Product> findByKeyword(@Param("keyword") String keyword);

}