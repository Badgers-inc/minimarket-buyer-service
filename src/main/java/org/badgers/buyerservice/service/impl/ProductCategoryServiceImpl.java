package org.badgers.buyerservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badgers.buyerservice.entity.Product;
import org.badgers.buyerservice.entity.ProductCategory;
import org.badgers.buyerservice.repository.ProductCategoryRepository;
import org.badgers.buyerservice.service.ProductCategoryService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

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