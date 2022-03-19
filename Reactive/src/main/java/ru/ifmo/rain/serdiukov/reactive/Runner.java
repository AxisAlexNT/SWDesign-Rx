package ru.ifmo.rain.serdiukov.reactive;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.Vertx;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
public class Runner {
    public static void main(final String[] args) {
        // Prepare Docker container with database:
        final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11-alpine")
                .withDatabaseName("reactive")
                .withUsername("postgres")
                .withPassword("password");
        // Launch Docker container:
        postgreSQLContainer.start();

        // Create base vertex for Vert.X framework:
        final @NotNull @NonNull Vertx vertx = Vertx.vertx();

        // Configure deployment:
        final @NotNull @NonNull DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject()
                .put("pgPort", postgreSQLContainer.getMappedPort(5432)));

        // And deploy Vert.X using given parameters
        vertx.deployVerticle(MainVerticle::new, options).subscribe().with(
                ok -> {
                    log.info("Database started in Docker container started at port {}", options.getConfig().getInteger("pgPort"));
                },
                err -> log.error("Failed to deploy vert.x application", err));


    }
}
