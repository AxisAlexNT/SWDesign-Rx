package ru.ifmo.rain.serdiukov.reactive.service;


import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import ru.ifmo.rain.serdiukov.reactive.domain.Product;
import ru.ifmo.rain.serdiukov.reactive.util.DBConnectionProvider;


public class ProductService implements EntityService<Product> {
    private final @NotNull @NonNull DBConnectionProvider dbConnectionProvider;
    private final @NotNull @NonNull Vertx vertx;

    public ProductService(final @NotNull @NonNull Vertx vertx, final @NotNull @NonNull DBConnectionProvider dbConnectionProvider) {
        this.dbConnectionProvider = dbConnectionProvider;
        this.vertx = vertx;
    }

    @Override
    public @NotNull @NonNull Multi<@NotNull @NonNull Product> getAll() {
        return dbConnectionProvider
                .getSessionFactory()
                .withSession(session ->
                        session
                                .createQuery("from Product", Product.class)
                                .getResultList()
                ).onItem().transformToMulti(Multi.createFrom()::iterable);
    }

    @Override
    public @NotNull @NonNull Uni<@NotNull @NonNull Product> save(@NotNull @NonNull Product entity) {
        return dbConnectionProvider
                .getSessionFactory()
                .withSession(session ->
                        session
                                .persist(entity)
                                .call(session::flush)
                                .replaceWith(entity)
                );
    }

    @Override
    public @NotNull @NonNull Uni<@NotNull @NonNull Product> findById(long id) {
        return dbConnectionProvider
                .getSessionFactory()
                .withSession(session ->
                        session
                                .find(Product.class, id)
                );
    }
}
