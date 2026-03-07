package org.badgers.buyerservice.repository;

import org.badgers.buyerservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    boolean existsByArticleNumber(String articleNumber);

    @Query("SELECT p FROM Product p JOIN p.productCategory pc WHERE pc.id = :categoryId")
    List<Product> findProductsByProductCategoryId(@Param("categoryId") Long categoryId);

    Product findProductByArticleNumber(String articleNumber);
}