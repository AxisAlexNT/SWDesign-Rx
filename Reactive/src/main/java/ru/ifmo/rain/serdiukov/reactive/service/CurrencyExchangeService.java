package ru.ifmo.rain.serdiukov.reactive.service;


import io.vertx.mutiny.core.Vertx;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import ru.ifmo.rain.serdiukov.reactive.domain.Currency;

public class CurrencyExchangeService {
    private final @NotNull @NonNull Vertx vertx;

    public CurrencyExchangeService(final @NotNull @NonNull Vertx vertx) {
        this.vertx = vertx;
    }

    public long convertPrice(final long priceRub, final @NotNull @NonNull Currency targetCurrency) {
        return switch (targetCurrency) {
            case RUB -> priceRub;
            case EUR -> priceRub / 100;
            case USD -> priceRub * 10 / 300;
        };
    }

    public @NotNull @NonNull String formatPrice(final long priceRub, final @NotNull @NonNull Currency targetCurrency) {
        final long price = convertPrice(priceRub, targetCurrency);
        return String.format("%d.%d", price / 100, price % 100);
    }
}
