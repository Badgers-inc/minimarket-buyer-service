package org.badgers.buyerservice.service.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badgers.buyerservice.dto.BuyerRequestDto;
import org.badgers.buyerservice.dto.BuyerResponseDto;
import org.badgers.buyerservice.entity.*;
import org.badgers.buyerservice.mapper.BuyerMapper;
import org.badgers.buyerservice.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("test")
public class TestDataInitializer implements CommandLineRunner {

    private final BuyerMapper buyerMapper;
    private final BuyerService buyerService;
    private final CartService cartService;
    private final CartOfferService cartOfferService;
    private final OfferService offerService;
    private final OrdersService ordersService;
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final SellerService sellerService;

    @Override
    @Transactional
    public void run(String... args) {
        log.debug("==========================================");
        log.debug("STARTING TEST DATA INITIALIZATION");
        log.debug("==========================================");
        try {
            createProductCategories();
            log.debug("Step 1 completed: {} categories created", productCategoryService.getAllProductCategory().size());

            createSellers();
            log.debug("Step 2 completed: {} sellers created", sellerService.getAllSellers().size());

            createProducts();
            log.debug("Step 3 completed: {} products created", productService.getAllProducts().size());

            createBuyers();
            log.debug("Step 4 completed: {} buyers created", buyerService.findAll().size());

            createOffers();
            log.debug("Step 5 completed: {} offers created", offerService.getAllOffers().size());

            createCarts();
            log.debug("Step 6 completed: {} carts created", cartService.findAll().size());

            createCartOffers();
            log.debug("Step 7 completed");

            createOrders();
            log.debug("Step 8 completed: {} orders created", ordersService.getAllOrders().size());

            log.debug("==========================================");
            log.debug("TEST DATA INITIALIZATION COMPLETED SUCCESSFULLY");
            log.debug("==========================================");

        } catch (Exception e) {
            log.error("ERROR during test data initialization: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void createProductCategories() {
        log.debug("Creating product categories from TZ specification...");

        String[][] categories = {
                {"Пищевые товары", "FOOD_PRODUCTS"},
                {"Непищевые товары", "NON-FOOD_PRODUCTS"},
                {"Товары с возрастным ограничением", "FOR_ADULTS"},
                {"Электротехника", "ELECTRICAL_APPLIANCE"}};

        for (String[] cat : categories) {
            ProductCategory category = new ProductCategory();
            category.setDescription(cat[0]);
            category.setCategoryCode(cat[1]);
            category.setActive(true);

            ProductCategory saved = productCategoryService.createProductCategory(category);
            log.debug("Created category: id={}, description='{}', code='{}'", saved.getId(),
                    saved.getDescription(), saved.getCategoryCode());
        }
    }

    private void createSellers() {
        log.debug("Creating sellers...");

        String[] sellerNames = {
                "ООО Рога и Копыта", "ИП Иванов", "Техномир",
                "Продуктовая лавка", "ООО Алкогольная лавка"};

        for (String name : sellerNames) {
            Seller seller = new Seller();
            seller.setName(name);

            Seller saved = sellerService.createSeller(seller);
            log.debug("Created seller: id={}, name='{}'", saved.getId(), saved.getName());
        }
    }

    private void createProducts() {
        log.debug("Creating products...");

        List<ProductCategory> categories = productCategoryService.getAllProductCategory();

        ProductCategory food = findCategoryByCode(categories, "FOOD_PRODUCTS");
        ProductCategory nonFood = findCategoryByCode(categories, "NON-FOOD_PRODUCTS");
        ProductCategory adult = findCategoryByCode(categories, "FOR_ADULTS");
        ProductCategory electronics = findCategoryByCode(categories, "ELECTRICAL_APPLIANCE");

        createSingleProduct("Молоко", "Свежее молоко 3.2% жирности, 1 литр",
                "ART-001", true, List.of(food));

        createSingleProduct("Смартфон", "Смартфон с 6.5 дюймовым экраном, 128GB памяти",
                "ART-002", true, List.of(electronics));

        createSingleProduct("Вино", "Красное сухое вино, 0.75л", "ART-003",
                true, List.of(food, adult));

        createSingleProduct("Ноутбук", "Ноутбук для работы и игр, 16GB RAM, 512GB SSD",
                "ART-004", true, List.of(electronics));

        createSingleProduct("Книга", "Роман в твердом переплете", "ART-005",
                true, List.of(nonFood));
    }

    private Product createSingleProduct(String name, String description, String article, Boolean active,
                                        List<ProductCategory> categories) {
        Product product = new Product();
        product.setProductName(name);
        product.setDescription(description);
        product.setArticleNumber(article);
        product.setActive(active);
        product.setProductCategory(categories);

        Product saved = productService.createProduct(product);
        log.debug("Created product: id={}, name='{}', article='{}'", saved.getId(),
                saved.getProductName(), saved.getArticleNumber());
        return saved;
    }

    private void createBuyers() {
        log.debug("Creating buyers...");

        Object[][] buyers = {
                {"Иван", "Петров", "Сергеевич", LocalDate.of(1990, 5, 15)},
                {"Мария", "Иванова", "Александровна", LocalDate.of(1985, 10, 20)},
                {"Алексей", "Сидоров", null, LocalDate.of(1995, 3, 8)},
                {"Елена", "Козлова", "Дмитриевна", LocalDate.of(1988, 7, 12)},
                {"Дмитрий", "Смирнов", "Андреевич", LocalDate.of(1992, 1, 25)}};

        for (Object[] buyerData : buyers) {
            BuyerRequestDto buyer = new BuyerRequestDto(
                    (String) buyerData[0],
                    (String) buyerData[1],
                    (String) buyerData[2],
                    (LocalDate) buyerData[3]);
            BuyerResponseDto saved = buyerService.save(buyer);
            log.debug("Created buyer: id={}, name='{} {}'", saved.uuid(), saved.firstName(), saved.lastName());
        }
    }

    private void createOffers() {
        log.debug("Creating offers...");

        List<Seller> sellers = sellerService.getAllSellers();
        List<Product> products = productService.getAllProducts();

        Seller foodSeller = findSellerByName(sellers, "Продуктовая лавка");
        Seller techSeller = findSellerByName(sellers, "Техномир");
        Seller ivanovSeller = findSellerByName(sellers, "ИП Иванов");
        Seller alcoholSeller = findSellerByName(sellers, "ООО Алкогольная лавка");

        Product milk = findProductByArticle(products, "ART-001");
        Product phone = findProductByArticle(products, "ART-002");
        Product wine = findProductByArticle(products, "ART-003");
        Product laptop = findProductByArticle(products, "ART-004");
        Product book = findProductByArticle(products, "ART-005");

        createSingleOffer(milk, foodSeller, new BigDecimal("89.99"), 100);

        createSingleOffer(milk, ivanovSeller, new BigDecimal("85.50"), 30);

        createSingleOffer(phone, techSeller, new BigDecimal("29999.99"), 15);

        createSingleOffer(wine, alcoholSeller, new BigDecimal("599.50"), 50);

        createSingleOffer(laptop, techSeller, new BigDecimal("75999.00"), 5);

        createSingleOffer(book, ivanovSeller, new BigDecimal("499.99"), 200);
    }

    private Offer createSingleOffer(Product product, Seller seller, BigDecimal price, int stock) {
        Offer offer = new Offer();
        offer.setProduct(product);
        offer.setSeller(seller);
        offer.setPrice(price);
        offer.setUnitsInStock(stock);
        offer.setActive(true);

        Offer saved = offerService.createOffer(offer);
        log.debug("Created offer: id={}, product='{}', seller='{}', price={}, stock={}",
                saved.getId(), saved.getProduct().getProductName(),
                saved.getSeller().getName(), saved.getPrice(), saved.getUnitsInStock());
        return saved;
    }

    private void createCarts() {
        log.debug("Creating carts...");

        List<BuyerResponseDto> buyers = buyerService.findAll();

        for (BuyerResponseDto buyer : buyers) {
            Cart cart = cartService.create(buyerMapper.buyerResponseDtoToBuyer(buyer));
            log.debug("Created cart: id={} for buyer: {} {}",
                    cart.getId(), buyer.firstName(), buyer.lastName());
        }
    }

    private void createCartOffers() {
        log.debug("Adding offers to carts...");

        List<BuyerResponseDto> buyers = buyerService.findAll();
        List<Offer> offers = offerService.getAllOffers();

        if (buyers.isEmpty() || offers.isEmpty()) {
            log.debug("No buyers or offers to create cart offers");
            return;
        }

        if (buyers.size() > 0) {
            Cart cart1 = cartService.findByBuyerId(buyers.get(0).uuid());
            Offer milkOffer = findCheapestOfferForProduct(offers, "ART-001");

            if (milkOffer != null) {
                createSingleCartOffer(cart1, milkOffer, 2);
            }
        }

        if (buyers.size() > 1) {
            Cart cart2 = cartService.findByBuyerId(buyers.get(1).uuid());
            Offer phoneOffer = findFirstOfferForProduct(offers, "ART-002");

            if (phoneOffer != null) {
                createSingleCartOffer(cart2, phoneOffer, 1);
            }
        }

        if (buyers.size() > 2) {
            Cart cart3 = cartService.findByBuyerId(buyers.get(2).uuid());
            Offer wineOffer = findFirstOfferForProduct(offers, "ART-003");

            if (wineOffer != null) {
                createSingleCartOffer(cart3, wineOffer, 3);
            }
        }

        if (buyers.size() > 3) {
            Cart cart4 = cartService.findByBuyerId(buyers.get(3).uuid());
            Offer bookOffer = findFirstOfferForProduct(offers, "ART-005");

            if (bookOffer != null) {
                createSingleCartOffer(cart4, bookOffer, 1);
            }
        }
    }

    private CartOffer createSingleCartOffer(Cart cart, Offer offer, int quantity) {
        CartOffer cartOffer = new CartOffer();
        cartOffer.setCart(cart);
        cartOffer.setOffer(offer);
        cartOffer.setQuantity(quantity);
        cartOffer.setPrice(offer.getPrice());
        cartOffer.setActive(true);

        CartOffer saved = cartOfferService.createCartOffer(cartOffer);
        log.debug("Added to cart {}: {} x {}, price={}", saved.getCart().getId(),
                saved.getOffer().getProduct().getProductName(), saved.getQuantity(), saved.getPrice());
        return saved;
    }

    private void createOrders() {
        log.debug("Creating orders...");

        for (int i = 0; i < 3; i++) {
            Orders order = new Orders();
            order.setCompleted(i == 0);
            Orders saved = ordersService.createOrder(order);
            log.debug("Created order: id={}, completed={}",
                    saved.getId(), saved.isCompleted());
        }

        log.debug("Note: Orders are not linked to CartOffer in this implementation");
    }

    private ProductCategory findCategoryByCode(List<ProductCategory> categories, String code) {
        return categories.stream().filter(c -> code.equals(c.getCategoryCode())).findFirst()
                .orElseThrow(() -> new RuntimeException("Category not found: " + code));
    }

    private Seller findSellerByName(List<Seller> sellers, String name) {
        return sellers.stream().filter(s -> name.equals(s.getName())).findFirst()
                .orElseThrow(() -> new RuntimeException("Seller not found: " + name));
    }

    private Product findProductByArticle(List<Product> products, String article) {
        return products.stream().filter(p -> article.equals(p.getArticleNumber()))
                .findFirst().orElseThrow(() -> new RuntimeException("Product not found: " + article));
    }

    private Offer findCheapestOfferForProduct(List<Offer> offers, String article) {
        return offers.stream().filter(o -> article.equals(o.getProduct().getArticleNumber()))
                .min((o1, o2) -> o1.getPrice().compareTo(o2.getPrice())).orElse(null);
    }

    private Offer findFirstOfferForProduct(List<Offer> offers, String article) {
        return offers.stream().filter(o -> article.equals(o.getProduct().getArticleNumber()))
                .findFirst().orElse(null);
    }
}