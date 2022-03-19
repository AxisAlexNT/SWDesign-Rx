package ru.ifmo.rain.serdiukov.reactive.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductListDTO {
    public boolean isError;
    public List<ProductInListDTO> products;
    public String errorMessage;
}
