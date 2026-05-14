package com.parcezza.backend.dto.product;

import java.math.BigDecimal;

public record ProductResponse(
    Long id,
    String sku,
    String name,
    String description,
    BigDecimal basePrice,
    Integer stock,
    String currency,
    boolean active,
    Long sellerId
) {
}
