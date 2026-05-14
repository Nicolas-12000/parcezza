package com.parcezza.backend.dto.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public record ProductVariantRequest(
    @Size(max = 100) String sku,
    @PositiveOrZero BigDecimal priceOverride,
    @PositiveOrZero Integer stock,
    @Valid List<VariantAttributeRequest> attributes
) {
}
