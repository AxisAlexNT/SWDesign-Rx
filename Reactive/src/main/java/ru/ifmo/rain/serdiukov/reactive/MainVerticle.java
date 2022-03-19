package ru.ifmo.rain.serdiukov.reactive;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import ru.ifmo.rain.serdiukov.reactive.controller.CurrencyExchangeController;
import ru.ifmo.rain.serdiukov.reactive.controller.ProductController;
import ru.ifmo.rain.serdiukov.reactive.controller.UserController;
import ru.ifmo.rain.serdiukov.reactive.http.ReactiveHTTPServer;
import ru.ifmo.rain.serdiukov.reactive.service.CurrencyExchangeService;
import ru.ifmo.rain.serdiukov.reactive.service.ProductService;
import ru.ifmo.rain.serdiukov.reactive.service.UserService;
import ru.ifmo.rain.serdiukov.reactive.util.DBConnectionProvider;

@Slf4j
public class MainVerticle extends AbstractVerticle {

    @Override
    public Uni<Void> asyncStart() {
        // Manual dependency injection is quite boeing, but I do not want to add more advanced dependency management libraries for this project

        // Database connection provider:
        final @NotNull @NonNull DBConnectionProvider dbConnectionProvider = new DBConnectionProvider(vertx);

        // Services (in this project they act both like Spring's services and repositories)
        final @NotNull @NonNull UserService userService = new UserService(vertx, dbConnectionProvider);
        final @NotNull @NonNull ProductService productService = new ProductService(vertx, dbConnectionProvider);
        final @NotNull @NonNull CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService(vertx);

        // Controllers, who are responsible for processing requests:
        final @NotNull @NonNull UserController userController = new UserController(vertx, userService);
        final @NotNull @NonNull ProductController productController = new ProductController(vertx, productService, userService, currencyExchangeService);
        final @NotNull @NonNull CurrencyExchangeController currencyExchangeController = new CurrencyExchangeController(vertx, currencyExchangeService);

        // HTTP server with internal router that routes requests for the controllers
        final @NotNull @NonNull ReactiveHTTPServer reactiveHTTPServer = new ReactiveHTTPServer(vertx, 8080, userController, productController, currencyExchangeController);

        // Asynchronous start means both DB connection provider and HTTP server have started
        return Uni.combine().all().unis(dbConnectionProvider.start(), reactiveHTTPServer.start()).discardItems();
    }
}
