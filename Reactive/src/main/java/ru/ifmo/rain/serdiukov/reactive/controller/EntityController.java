package ru.ifmo.rain.serdiukov.reactive.controller;

import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.RoutingContext;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public abstract class EntityController<T> {
    public abstract void assignRoutes(final @NotNull @NonNull Router router);

    public abstract @NotNull @NonNull Class<T> getClazz();

    protected @NotNull @NonNull String getParamFromQueryContext(final @NotNull @NonNull String parameterName, final @NotNull @NonNull RoutingContext ctx, final @NotNull @NonNull String errorMessage) {
        if (ctx.queryParam(parameterName).isEmpty()) {
            throw new RuntimeException(errorMessage);
        }
        return ctx.queryParam(parameterName).get(0);
    }

    protected @NotNull @NonNull String getParamFromQueryContext(final @NotNull @NonNull String parameterName, final @NotNull @NonNull RoutingContext ctx) {
        return getParamFromQueryContext(parameterName, ctx, String.format("Missing required query parameter: %s", parameterName));
    }
}
