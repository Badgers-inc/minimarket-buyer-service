package org.badgers.buyerservice.service;

import org.badgers.buyerservice.entity.Product;
import org.badgers.buyerservice.entity.ProductCategory;

import java.util.List;
import java.util.UUID;

public interface ProductCategoryService {

    ProductCategory createProductCategory(ProductCategory productCategory);

    ProductCategory getProductCategoryById(Long id);

    void deleteProductCategory(Long id);

    List<ProductCategory> getAllProductCategory();

    List<ProductCategory> getProductCategoryByProduct(Product product);

    ProductCategory addProductsToProductCategory(Long productCategoryId, List<UUID> products);

}