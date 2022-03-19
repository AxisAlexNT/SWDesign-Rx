package ru.ifmo.rain.serdiukov.reactive.controller;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.RoutingContext;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ifmo.rain.serdiukov.reactive.controller.dto.UserRegisterResultDTO;
import ru.ifmo.rain.serdiukov.reactive.controller.dto.UserSearchResultDTO;
import ru.ifmo.rain.serdiukov.reactive.domain.Currency;
import ru.ifmo.rain.serdiukov.reactive.domain.User;
import ru.ifmo.rain.serdiukov.reactive.service.UserService;

import java.util.List;

public class UserController extends EntityController<User> {
    private final @NotNull @NonNull Vertx vertx;
    private final @NotNull @NonNull UserService userService;

    public UserController(@NotNull @NonNull Vertx vertx, @NotNull @NonNull UserService userService) {
        this.vertx = vertx;
        this.userService = userService;
    }

    @Override
    public void assignRoutes(@NotNull @NonNull Router router) {
        router.get("/api/v1/users/addUser").respond(this::addUser);
        router.get("/api/v1/users/user").respond(this::getUser);
        router.get("/api/v1/users/listUsers").respond(this::listUsers);
        router.get("/api/v1/users/userIds").respond(this::userIds);
    }

    @Override
    public @NonNull Class<User> getClazz() {
        return User.class;
    }

    public @NotNull @NonNull Uni<@NotNull @NonNull UserRegisterResultDTO> addUser(final @NotNull @NonNull RoutingContext ctx) {
        return Uni
                .createFrom()
                .item(() -> getParamFromQueryContext("login", ctx))
                .onItemOrFailure()
                .transformToUni((login, f) ->
                {
                    if (login != null) {
                        return userService
                                .findByLogin(login)
                                .onItem()
                                .ifNotNull()
                                .transformToUni(existingUser -> Uni.createFrom().item(() -> {
                                    assert (existingUser != null) : "ifNotNull triggered while user was null?";
                                    ctx.response().setStatusCode(400);
                                    return UserRegisterResultDTO
                                            .builder()
                                            .error(true)
                                            .registeredUser(null)
                                            .errorMessage("Login " + login + " is already occupied by a user with id=" + existingUser.getId())
                                            .build();
                                }))
                                .onItem().ifNull()
                                .switchTo(() ->
                                {
                                    final @NotNull @NonNull String name = getParamFromQueryContext("name", ctx);
                                    final @NotNull @NonNull Currency currency = Currency.valueOf(getParamFromQueryContext("preferredCurrency", ctx));
                                    final @NotNull @NonNull User entity = User
                                            .builder()
                                            .login(login)
                                            .name(name)
                                            .preferredCurrency(currency)
                                            .build();
                                    return userService.save(entity).onItemOrFailure().transformToUni((u, e) -> {
                                        if (e == null) {
                                            ctx.response().setStatusCode(200);
                                            return Uni.createFrom().item(() -> UserRegisterResultDTO
                                                    .builder()
                                                    .error(false)
                                                    .registeredUser(u)
                                                    .errorMessage(null)
                                                    .build());
                                        } else {
                                            ctx.response().setStatusCode(500);
                                            return Uni.createFrom().item(() -> UserRegisterResultDTO
                                                    .builder()
                                                    .error(true)
                                                    .registeredUser(null)
                                                    .errorMessage("Failed to register user: " + e.getMessage())
                                                    .build());
                                        }
                                    });
                                }).onFailure()
                                .recoverWithItem(e ->
                                        UserRegisterResultDTO
                                                .builder()
                                                .error(true)
                                                .registeredUser(null)
                                                .errorMessage("Failed to register user: " + e.getMessage())
                                                .build()
                                );
                    } else {
                        ctx.response().setStatusCode(400);
                        return Uni.createFrom().item(() -> UserRegisterResultDTO
                                .builder()
                                .error(true)
                                .registeredUser(null)
                                .errorMessage("Missing required argument: login")
                                .build());
                    }
                });

    }

    private @NotNull @NonNull Uni<@Nullable UserSearchResultDTO> getUser(final @NotNull @NonNull RoutingContext ctx) {
        return Uni
                .createFrom()
                .item(() -> Long.parseLong(getParamFromQueryContext("userId", ctx)))
                .onItemOrFailure()
                .transformToUni((id, f) -> {
                    if (f != null) {
                        return Uni.createFrom().item(() ->
                                UserSearchResultDTO
                                        .builder()
                                        .userFound(false)
                                        .user(null)
                                        .errorMessage("Cannot find user: " + f.getMessage())
                                        .build()
                        );
                    } else {
                        return userService
                                .findById(id)
                                .onItemOrFailure()
                                .transformToUni((u, e) ->
                                        Uni.createFrom().item(() -> {
                                                    if (e != null) {
                                                        ctx.response().setStatusCode(500);
                                                        return UserSearchResultDTO
                                                                .builder()
                                                                .userFound(false)
                                                                .user(null)
                                                                .errorMessage(e.getMessage())
                                                                .build();
                                                    } else {
                                                        if (u == null) {
                                                            ctx.response().setStatusCode(404);
                                                            return UserSearchResultDTO
                                                                    .builder()
                                                                    .userFound(false)
                                                                    .user(null)
                                                                    .errorMessage("Cannot find user with id=" + id)
                                                                    .build();
                                                        } else {
                                                            ctx.response().setStatusCode(200);
                                                            return UserSearchResultDTO
                                                                    .builder()
                                                                    .userFound(true)
                                                                    .user(u)
                                                                    .errorMessage(null)
                                                                    .build();
                                                        }
                                                    }
                                                }
                                        )
                                );
                    }
                });
    }

    private @NotNull @NonNull Uni<@NotNull @NonNull List<@NotNull @NonNull User>> listUsers(final @NotNull @NonNull RoutingContext ctx) {
        return userService.getAll().collect().asList();
    }

    private @NotNull @NonNull Uni<@NotNull @NonNull List<@NotNull @NonNull Long>> userIds(final @NotNull @NonNull RoutingContext ctx) {
        return userService.getAll().onItem().transform(User::getId).collect().asList();
    }
}
