package org.badgers.buyerservice.repository;

import org.badgers.buyerservice.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    @Query(value = "select p from ProductCategory p JOIN p.products pp WHERE pp.id = :id")
    List<ProductCategory> findProductCategoriesByProducts(@Param("id") UUID id);
}
