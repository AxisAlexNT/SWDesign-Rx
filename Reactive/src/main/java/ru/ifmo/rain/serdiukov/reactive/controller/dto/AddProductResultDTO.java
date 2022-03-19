package ru.ifmo.rain.serdiukov.reactive.controller.dto;

import lombok.*;
import org.jetbrains.annotations.Nullable;
import ru.ifmo.rain.serdiukov.reactive.domain.Product;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class AddProductResultDTO {
    private boolean error;
    private @Nullable Product product;
    private @Nullable String errorMessage;
}
