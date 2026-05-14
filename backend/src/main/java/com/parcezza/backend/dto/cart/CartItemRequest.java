package com.parcezza.backend.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemRequest(
    @NotNull Long productId,
    Long variantId,
    @NotNull @Positive Integer quantity
) {
}
