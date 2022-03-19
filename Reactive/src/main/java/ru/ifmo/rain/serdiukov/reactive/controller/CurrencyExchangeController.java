package ru.ifmo.rain.serdiukov.reactive.controller;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.RoutingContext;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import ru.ifmo.rain.serdiukov.reactive.domain.Currency;
import ru.ifmo.rain.serdiukov.reactive.service.CurrencyExchangeService;

public class CurrencyExchangeController extends EntityController<Currency> {
    private final @NotNull @NonNull Vertx vertx;
    private final @NotNull @NonNull CurrencyExchangeService currencyExchangeService;


    public CurrencyExchangeController(@NotNull @NonNull Vertx vertx, @NotNull @NonNull CurrencyExchangeService currencyExchangeService) {
        this.vertx = vertx;
        this.currencyExchangeService = currencyExchangeService;
    }


    @Override
    public void assignRoutes(@NotNull @NonNull Router router) {
        router.get("/api/v1/currency/rates").respond(this::showRates);
    }

    public @NotNull @NonNull Uni<@NotNull @NonNull String> showRates(final @NotNull @NonNull RoutingContext ctx) {
        return Uni
                .createFrom()
                .item(() ->
                        String.format(
                                "100.00 RUB = %s EUR = %s USD",
                                currencyExchangeService.formatPrice(10000, Currency.EUR),
                                currencyExchangeService.formatPrice(10000, Currency.USD))
                );
    }

    @Override
    public @NotNull @NonNull Class<Currency> getClazz() {
        return Currency.class;
    }
}
