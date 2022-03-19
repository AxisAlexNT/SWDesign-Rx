package ru.ifmo.rain.serdiukov.reactive.util;


import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.reactive.mutiny.Mutiny;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Persistence;
import java.util.Map;

@Slf4j
public class DBConnectionProvider {
    private final @NotNull @NonNull Vertx vertx;
    @Getter(AccessLevel.PUBLIC)
    private Mutiny.SessionFactory sessionFactory;


    public DBConnectionProvider(final @NotNull @NonNull Vertx vertx) {
        this.vertx = vertx;
    }

    public Uni<Void> start() {
        Uni<Void> startHibernate = Uni.createFrom().deferred(() -> {
            var pgPort = vertx.getOrCreateContext().config().getInteger("pgPort", 5432);
            var props = Map.of(
                    "javax.persistence.jdbc.url", "jdbc:postgresql://localhost:" + pgPort + "/postgres"
            );

            sessionFactory = Persistence
                    .createEntityManagerFactory("reactive-unit", props)
                    .unwrap(Mutiny.SessionFactory.class);

            return Uni.createFrom().voidItem();
        });

        return vertx.executeBlocking(startHibernate)
                .onItem()
                .invoke(() ->
                        log.info("Hibernate Reactive is ready")
                );
    }
}
