package com.parcezza.backend.dto.product;

import java.math.BigDecimal;
import java.util.List;

public record ProductVariantResponse(
    Long id,
    String sku,
    BigDecimal priceOverride,
    Integer stock,
    List<VariantAttributeResponse> attributes
) {
}
