package ru.ifmo.rain.serdiukov.reactive.controller.dto;

import lombok.*;
import org.jetbrains.annotations.Nullable;
import ru.ifmo.rain.serdiukov.reactive.domain.User;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class UserSearchResultDTO {
    private boolean userFound;
    private @Nullable User user;
    private @Nullable String errorMessage;
}
