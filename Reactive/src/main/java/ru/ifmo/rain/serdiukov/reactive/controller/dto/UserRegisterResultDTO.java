package ru.ifmo.rain.serdiukov.reactive.controller.dto;

import lombok.*;
import org.jetbrains.annotations.Nullable;
import ru.ifmo.rain.serdiukov.reactive.domain.User;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class UserRegisterResultDTO {
    private boolean error;
    private @Nullable User registeredUser;
    private @Nullable String errorMessage;
}
