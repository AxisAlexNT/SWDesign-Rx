package ru.ifmo.rain.serdiukov.reactive.service;


import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ifmo.rain.serdiukov.reactive.domain.User;
import ru.ifmo.rain.serdiukov.reactive.util.DBConnectionProvider;


public class UserService implements EntityService<User> {
    private final @NotNull @NonNull DBConnectionProvider dbConnectionProvider;
    private final @NotNull @NonNull Vertx vertx;

    public UserService(final @NotNull @NonNull Vertx vertx, final @NotNull @NonNull DBConnectionProvider dbConnectionProvider) {
        this.dbConnectionProvider = dbConnectionProvider;
        this.vertx = vertx;
    }

    @Override
    public @NotNull @NonNull Multi<@NotNull @NonNull User> getAll() {
        return dbConnectionProvider
                .getSessionFactory()
                .withSession(session ->
                        session
                                .createQuery("SELECT U from User U WHERE 1=1", User.class)
                                .getResultList()
                ).onItem().transformToMulti(Multi.createFrom()::iterable);
    }

    @Override
    public @NotNull @NonNull Uni<@NotNull @NonNull User> save(@NotNull @NonNull User entity) {
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
    public @NotNull @NonNull Uni<@Nullable User> findById(final long id) {
        return dbConnectionProvider
                .getSessionFactory()
                .withSession(session ->
                        session
                                .find(User.class, id)
                );
    }

    public @NotNull @NonNull Uni<@Nullable User> findByLogin(final @NotNull @NonNull String login) {
        return dbConnectionProvider
                .getSessionFactory()
                .withSession(session ->
                        session
                                .createQuery("SELECT U FROM User U WHERE U.login=:login", User.class)
                                .setParameter("login", login)
                                .getResultList()
                ).onItem().transform(l -> l.isEmpty() ? null : l.get(0));
    }
}
