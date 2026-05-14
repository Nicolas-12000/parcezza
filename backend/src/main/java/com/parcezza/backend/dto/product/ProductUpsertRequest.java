package com.parcezza.backend.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductUpsertRequest(
    @NotBlank @Size(max = 100) String sku,
    @NotBlank @Size(max = 255) String name,
    @Size(max = 2000) String description,
    @NotNull @PositiveOrZero BigDecimal basePrice,
    @NotBlank @Size(max = 10) String currency,
    @NotNull @PositiveOrZero Integer stock,
    boolean active
) {
}
