package org.badgers.buyerservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badgers.buyerservice.entity.Product;
import org.badgers.buyerservice.entity.ProductCategory;
import org.badgers.buyerservice.repository.ProductCategoryRepository;
import org.badgers.buyerservice.repository.ProductRepository;
import org.badgers.buyerservice.service.ProductService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    @Transactional
    public Product createProduct(Product product) {
        log.debug("create product with name: {}, ", product.getProductName());
        Product trimedProduct = trimmedProduct(product);
        validateProduct(trimedProduct);
        Product saved = productRepository.save(trimedProduct);
        log.debug("created product with name: {}, ", saved.getProductName());
        return saved;
    }

    @Override
    @Transactional
    public Product updateProduct(UUID id, Product product) {
        log.debug("update product with name: {}, ", product.getProductName());
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        validateProductforUpdate(product);
        Product found = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        found.setProductName(trimString(product.getProductName()));
        found.setDescription(trimString(product.getDescription()));
        found.setArticleNumber(trimString(product.getArticleNumber()));
        log.debug("updated product with name: {}, ", found.getProductName());
        return found;
    }

    @Override
    @Transactional
    public void deleteProduct(UUID id) {
        log.debug("Deleting product with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        try {
            productRepository.deleteById(id);
            log.debug("Successfully deleted product with id: {}", id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Product with id: " + id + " does not exist");
        }
    }

    @Override
    public List<Product> getAllProducts() {
        log.debug("start getting all products");
        List<Product> allProducts = productRepository.findAll();
        log.debug("end getting all products");
        return allProducts;
    }

    @Override
    public Product getProductById(UUID productId) {
        log.debug("start getting product with id: {}, ", productId);
        if (productId == null) {
            throw new IllegalArgumentException("id is null");
        }
        Product found = productRepository.findById(productId)
                .orElseThrow(EntityNotFoundException::new);
        log.debug("get product with id: {}, ", found.getProductName());
        return found;
    }

    @Override
    public List<Product> getAllProductsByCategory(ProductCategory productCategory) {
        log.debug("start getting all products by category: {}, ", productCategory);
        if (productCategory == null) {
            throw new IllegalArgumentException("productCategory is null");
        }
        if (productCategory.getId() == null) {
            throw new IllegalArgumentException("productCategory id is null");
        }
        List<Product> products = productRepository.findProductsByProductCategoryId(productCategory.getId());
        log.debug("end getting all products by category id: {}, ", productCategory.getId());
        return products;
    }

    @Override
    @Transactional
    public Product addProductCategoriesToProduct(UUID productId, List<Long> categoriesIds) {
        log.debug("start adding products to categories: {}, ", categoriesIds);
        if (productId == null) {
            throw new IllegalArgumentException("productId is null");
        }
        if (categoriesIds == null || categoriesIds.isEmpty()) {
            throw new IllegalArgumentException("categoriesIds is null or empty");
        }
        Product found = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with id: " + productId + " does not exist"));
        List<ProductCategory> categories = productCategoryRepository.findAllById(categoriesIds);
        if (categories.size() != categoriesIds.size()) {
            List<Long> foundIds = categories.stream().map(ProductCategory::getId).toList();
            List<Long> notFoundIds = categoriesIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();
            throw new EntityNotFoundException("Categories with id " + notFoundIds + " does not exist");
        }
        for (ProductCategory category : categories) {
            if (category.getActive() != true) {
                throw new IllegalArgumentException("category active is not true");
            }
        }
        List<ProductCategory> existingProductCategories = found.getProductCategory();
        for (ProductCategory category : categories) {
            if (!existingProductCategories.contains(category)) {
                existingProductCategories.add(category);
            }
        }
        log.debug("end adding products to categories: {}, ", categoriesIds);
        return found;
    }

    @Override
    public Product getProductByArticleNumber(String articleNumber) {
        log.debug("start getting product by article number: {}, ", articleNumber);
        if (articleNumber == null) {
            throw new IllegalArgumentException("article number is null");
        }
        String trimmedArticleNumber = trimString(articleNumber);
        Product product = productRepository.findProductByArticleNumber(trimmedArticleNumber);
        if (product == null) {
            throw new EntityNotFoundException("product with article number: " + trimmedArticleNumber + " does not exist");
        }
        log.debug("end getting product by article number: {}, ", trimmedArticleNumber);
        return product;
    }

    private void validateProduct(Product product) {
        if (product.getProductName() == null || product.getProductName().isEmpty()) {
            throw new IllegalArgumentException("product name isn't valid");
        }
        if (product.getDescription() == null || product.getDescription().isEmpty()) {
            throw new IllegalArgumentException("product description isn't valid");
        }
        if (product.getArticleNumber() == null || product.getArticleNumber().isEmpty()) {
            throw new IllegalArgumentException("product article number isn't valid");
        }
        if (existsProductByArticleNumber(trimString(product.getArticleNumber()))) {
            throw new IllegalArgumentException("product already exists with article number: " + product.getArticleNumber());
        }
    }

    private void validateProductforUpdate(Product product) {
        if (product.getProductName() == null || product.getProductName().isEmpty()) {
            throw new IllegalArgumentException("product name isn't valid");
        }
        if (product.getDescription() == null || product.getDescription().isEmpty()) {
            throw new IllegalArgumentException("product description isn't valid");
        }
        if (product.getArticleNumber() == null || product.getArticleNumber().isEmpty()) {
            throw new IllegalArgumentException("product article number isn't valid");
        }
    }

    private boolean existsProductByArticleNumber(String articleNumber) {
        return productRepository.existsByArticleNumber(articleNumber);
    }

    private Product trimmedProduct(Product product) {
        product.setProductName(trimString(product.getProductName()));
        product.setDescription(trimString(product.getDescription()));
        product.setArticleNumber(trimString(product.getArticleNumber()));
        return product;
    }

    private String trimString(String string) {
        return string.trim();
    }
}