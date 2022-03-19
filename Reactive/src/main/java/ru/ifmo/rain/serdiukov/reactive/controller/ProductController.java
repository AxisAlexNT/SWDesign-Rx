package ru.ifmo.rain.serdiukov.reactive.controller;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.RoutingContext;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ifmo.rain.serdiukov.reactive.controller.dto.AddProductResultDTO;
import ru.ifmo.rain.serdiukov.reactive.controller.dto.ProductInListDTO;
import ru.ifmo.rain.serdiukov.reactive.controller.dto.ProductListDTO;
import ru.ifmo.rain.serdiukov.reactive.controller.dto.ProductSearchResultDTO;
import ru.ifmo.rain.serdiukov.reactive.domain.Product;
import ru.ifmo.rain.serdiukov.reactive.service.CurrencyExchangeService;
import ru.ifmo.rain.serdiukov.reactive.service.ProductService;
import ru.ifmo.rain.serdiukov.reactive.service.UserService;

import java.util.List;
import java.util.Objects;

public class ProductController extends EntityController<Product> {
    private final @NotNull @NonNull Vertx vertx;
    private final @NotNull @NonNull ProductService productService;
    private final @NotNull @NonNull UserService userService;
    private final @NotNull @NonNull CurrencyExchangeService currencyExchangeService;

    public ProductController(final @NotNull @NonNull Vertx vertx, final @NotNull @NonNull ProductService productService, final @NotNull @NonNull UserService userService, final @NotNull @NonNull CurrencyExchangeService currencyExchangeService) {
        this.vertx = vertx;
        this.productService = productService;
        this.userService = userService;
        this.currencyExchangeService = currencyExchangeService;
    }

    @Override
    public void assignRoutes(@NotNull @NonNull Router router) {
        router.get("/api/v1/products/addProduct").respond(this::addProduct);
        router.get("/api/v1/products/product").respond(this::getProduct);
        router.get("/api/v1/products/rawListProducts").respond(this::rawListProducts);
        router.get("/api/v1/products/listProducts").respond(this::listProducts);
        router.get("/api/v1/products/productIds").respond(this::listProductIds);
    }

    @Override
    public @NonNull Class<Product> getClazz() {
        return Product.class;
    }


    public @NotNull @NonNull Uni<@NotNull @NonNull AddProductResultDTO> addProduct(final @NotNull @NonNull RoutingContext ctx) {
        return Uni.createFrom().item(
                        () -> {
                            final @NotNull @NonNull String name = getParamFromQueryContext("name", ctx);
                            final @NotNull @NonNull String description = getParamFromQueryContext("description", ctx);
                            final long priceRub = Long.parseLong(getParamFromQueryContext("priceRub", ctx));
                            final @NotNull @NonNull Product entity = new Product();
                            entity.setName(name);
                            entity.setDescription(description);
                            entity.setPriceRub(priceRub);
                            return entity;
                            //return productService.save(entity);
                        }
                ).onItem()
                .transformToUni(productService::save)
                .onItemOrFailure()
                .transformToUni((p, e) ->
                        Uni.createFrom().item(() ->
                                {
                                    if (e != null) {
                                        return
                                                AddProductResultDTO
                                                        .builder()
                                                        .error(true)
                                                        .product(null)
                                                        .errorMessage("Cannot add product: " + e.getMessage())
                                                        .build();
                                    } else {
                                        return
                                                AddProductResultDTO
                                                        .builder()
                                                        .error(false)
                                                        .product(p)
                                                        .errorMessage(null)
                                                        .build();
                                    }
                                }
                        )

                );
    }


    private @NotNull @NonNull Uni<@Nullable ProductSearchResultDTO> getProduct(
            final @NotNull @NonNull RoutingContext ctx) {
        return Uni
                .createFrom()
                .item(() -> Long.parseLong(getParamFromQueryContext("productId", ctx)))
                .onItem()
                .transformToUni(productId -> productService.findById(Objects.requireNonNull(productId, "Product ID was found in context and parsed, but null?")))
                .onItem()
                .transformToUni(product -> Uni
                        .createFrom()
                        .item(() -> Long.parseLong(getParamFromQueryContext("userId", ctx)))
                        .onItem()
                        .transformToUni(userId ->
                                userService
                                        .findById(userId)
                                        .onItem()
                                        .ifNotNull()
                                        .transformToUni(user ->
                                                Uni
                                                        .createFrom()
                                                        .item(() ->
                                                                ProductSearchResultDTO
                                                                        .builder()
                                                                        .productFound(true)
                                                                        .productId(product.getId())
                                                                        .name(product.getName())
                                                                        .description(product.getDescription())
                                                                        .price(currencyExchangeService.formatPrice(product.getPriceRub(), Objects.requireNonNull(user, "User was found but is null?").getPreferredCurrency()))
                                                                        .currency(Objects.requireNonNull(user, "User was found but is null?").getPreferredCurrency().name())
                                                                        .build()
                                                        )
                                        )
                                        .onItem()
                                        .ifNull()
                                        .failWith(() -> new RuntimeException("Cannot find user with given id"))
                        )
                )
                .onFailure()
                .recoverWithItem(f ->
                        ProductSearchResultDTO
                                .builder()
                                .errorMessage(f.getMessage())
                                .build()
                );
    }


    private @NotNull @NonNull Uni<@NotNull @NonNull List<@NotNull @NonNull Product>> rawListProducts(
            final @NotNull @NonNull RoutingContext ctx) {
        return productService.getAll().collect().asList();
    }

    private @NotNull @NonNull Uni<@NotNull @NonNull List<@NotNull @NonNull Long>> listProductIds(
            final @NotNull @NonNull RoutingContext ctx) {
        return productService.getAll().onItem().transform(Product::getId).collect().asList();
    }

    private @NotNull @NonNull Uni<@NotNull @NonNull ProductListDTO> listProducts(
            final @NotNull @NonNull RoutingContext ctx) {

        return Uni
                .createFrom()
                .item(() -> Long.parseLong(getParamFromQueryContext("userId", ctx)))
                .onItem()
                .transformToUni(userId ->
                        userService
                                .findById(userId)
                                .onItem()
                                .ifNotNull()
                                .transformToUni(user ->
                                        productService
                                                .getAll()
                                                .onItem()
                                                .transform(product ->
                                                        ProductInListDTO
                                                                .builder()
                                                                .id(product.getId())
                                                                .name(product.getName())
                                                                .description(product.getDescription())
                                                                .price(currencyExchangeService.formatPrice(product.getPriceRub(), Objects.requireNonNull(user, "User was found but is null?").getPreferredCurrency()))
                                                                .currency(Objects.requireNonNull(user, "User was found but is null?").getPreferredCurrency().name())
                                                                .build()
                                                )
                                                .collect()
                                                .asList()
                                )
                                .onItem()
                                .transform(productList ->
                                        ProductListDTO
                                                .builder()
                                                .products(productList)
                                                .isError(false)
                                                .errorMessage(null)
                                                .build()
                                )
                )
                .onFailure()
                .recoverWithItem(f ->
                        ProductListDTO
                                .builder()
                                .isError(true)
                                .errorMessage(f.getMessage())
                                .products(null)
                                .build()
                );
    }
}
