package org.badgers.buyerservice.service;

import org.badgers.buyerservice.entity.Product;
import org.badgers.buyerservice.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {

    ProductCategory createProductCategory(ProductCategory productCategory);

    ProductCategory getProductCategoryById(Long id);

    void deleteProductCategory(Long id);

    List<ProductCategory> getAllProductCategory();

    List<ProductCategory> getProductCategoryByProduct(Product product);

}