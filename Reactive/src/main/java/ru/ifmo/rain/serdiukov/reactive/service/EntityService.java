package ru.ifmo.rain.serdiukov.reactive.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public interface EntityService<T> {
    //    Uni<@NotNull @NonNull List<@NotNull @NonNull T>> getAll();
    @NotNull @NonNull Multi<@NotNull @NonNull T> getAll();

    @NotNull @NonNull Uni<@NotNull @NonNull T> save(final @NotNull @NonNull T entity);

    default @NotNull @NonNull Multi<@NotNull @NonNull T> save(final @NotNull @NonNull Multi<@NotNull @NonNull T> entities) {
        return entities.call(this::save);
    }

    @NotNull @NonNull Uni<@NotNull @NonNull T> findById(final long id);
}
