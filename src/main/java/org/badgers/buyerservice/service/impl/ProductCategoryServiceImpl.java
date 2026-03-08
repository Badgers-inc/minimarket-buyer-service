package org.badgers.buyerservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badgers.buyerservice.entity.Product;
import org.badgers.buyerservice.entity.ProductCategory;
import org.badgers.buyerservice.repository.ProductCategoryRepository;
import org.badgers.buyerservice.repository.ProductRepository;
import org.badgers.buyerservice.service.ProductCategoryService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;

    @Override
    public ProductCategory createProductCategory(ProductCategory productCategory) {
        log.debug("Start creating product category");
        if (productCategory.getDescription() == null || productCategory.getDescription().equals("")) {
            throw new IllegalArgumentException("Product category description is required");
        }
        if (productCategory.getCategoryCode() == null || productCategory.getCategoryCode().equals("")) {
            throw new IllegalArgumentException("Product category code is required");
        }
        productCategory.setDescription(productCategory.getDescription().trim());
        productCategory.setCategoryCode(productCategory.getCategoryCode().trim());
        ProductCategory savedProductCategory = productCategoryRepository.save(productCategory);
        log.debug("End creating product category");
        return savedProductCategory;
    }

    @Override
    public ProductCategory getProductCategoryById(Long id) {
        log.debug("Start getting product category with id {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Product category id is required");
        }
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product category with id " + id + " not found"));
        log.debug("End getting product category with id {}", id);
        return productCategory;
    }

    @Transactional
    @Override
    public ProductCategory addProductsToProductCategory(Long productCategoryId, List<UUID> products) {
        log.debug("Start adding products to category with id {}", productCategoryId);
        if (productCategoryId == null || productCategoryId == 0) {
            throw new IllegalArgumentException("Product category id is required");
        }
        ProductCategory productCategory = productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new EntityNotFoundException("Product category with id " + productCategoryId + " not found"));

        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Product category products is required");
        }
        if (productCategory.getActive() != true) {
            throw new IllegalArgumentException("Product category active is required");
        }
        List<Product> productList = productRepository.findAllById(products);
        if (productList.size() != products.size()) {
            List<UUID> foundIds = productList.stream().map(Product::getId).toList();
            List<UUID> notFoundsUUIDs = products.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();
            throw new EntityNotFoundException("Products category with id " + notFoundsUUIDs + " not found");
        }
        for (Product product : productList) {
            if (product.getActive() != true) {
                throw new IllegalArgumentException("Product category active is required");
            }
        }
        List<Product> currentListProducts = productCategory.getProducts();
        for (Product product : productList) {
            if (!currentListProducts.contains(product)) {
                currentListProducts.add(product);
            }
        }
        log.debug("End adding products to category with id {}", productCategoryId);
        return productCategory;
    }

    @Override
    public void deleteProductCategory(Long id) {
        log.debug("Start deleting product category with id {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Product category id is required");
        }
        try {
            productCategoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Product category with id " + id + " not found");
        }
        log.debug("End deleting product category with id {}", id);
    }

    @Override
    public List<ProductCategory> getAllProductCategory() {
        log.debug("Start getting all product category");
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        log.debug("End getting all product category");
        return productCategoryList;
    }

    @Override
    public List<ProductCategory> getProductCategoryByProduct(Product product) {
        log.debug("Start getting all product category for product {}", product);
        if (product == null) {
            throw new IllegalArgumentException("product is required");
        }
        if (product.getId() == null) {
            throw new IllegalArgumentException("product haven't relationship");
        }
        List<ProductCategory> productCategoryList = productCategoryRepository.findProductCategoriesByProducts(product.getId());
        log.debug("End getting all product category for product {}", product);
        return productCategoryList;
    }
}