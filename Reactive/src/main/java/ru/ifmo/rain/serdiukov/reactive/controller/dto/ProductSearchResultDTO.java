package ru.ifmo.rain.serdiukov.reactive.controller.dto;

import lombok.*;
import org.jetbrains.annotations.Nullable;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
public class ProductSearchResultDTO {
    private boolean productFound;
    public long productId;
    public @Nullable String name;
    public @Nullable String description;
    public @Nullable String price;
    public @Nullable String currency;
    private @Nullable String errorMessage;
}
