package ru.ifmo.rain.serdiukov.reactive.http;


import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.http.HttpServer;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.handler.BodyHandler;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import ru.ifmo.rain.serdiukov.reactive.controller.EntityController;

@Slf4j
public class ReactiveHTTPServer {
    private final @NotNull @NonNull Vertx vertx;
    private final @NotNull @NonNull Router router;
    private final @NotNull @NonNull EntityController<?>[] entityControllers;
    private final int serverPort;

    public ReactiveHTTPServer(final @NotNull @NonNull Vertx vertx, int serverPort, final @NotNull @NonNull EntityController<?>... entityControllers) {
        this.vertx = vertx;
        this.router = Router.router(vertx);
        this.serverPort = serverPort;
        this.entityControllers = entityControllers;
    }

    public Uni<HttpServer> start() {
        return vertx.createHttpServer()
                .requestHandler(router::handle)
                .listen(serverPort)
                .onItem().invoke(() -> {
                    BodyHandler bodyHandler = BodyHandler.create();
                    router.post().handler(bodyHandler::handle);
                    // Register all controllers' request handlers:
                    for (final @NotNull @NonNull EntityController<?> entityService : entityControllers) {
                        entityService.assignRoutes(router);
                    }
                    log.info("HTTP server listening on port {}", serverPort);
                });
    }
}
