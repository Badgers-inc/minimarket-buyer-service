package org.badgers.buyerservice.service;

import org.badgers.buyerservice.entity.Product;
import org.badgers.buyerservice.entity.ProductCategory;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    Product createProduct(Product product);

    Product updateProduct(UUID id, Product product);

    void deleteProduct(UUID id);

    List<Product> getAllProducts();

    Product getProductById(UUID productId);

    List<Product> getAllProductsByCategory(ProductCategory productCategory);

    Product getProductByArticleNumber(String articleNumber);

    Product addProductCategoriesToProduct(UUID productId, List<Long> categories);

}