package com.parcezza.backend.dto.order;

import java.math.BigDecimal;

public record OrderItemResponse(
    Long id,
    Long productId,
    Long variantId,
    Integer quantity,
    BigDecimal unitPrice,
    String currency,
    BigDecimal lineTotal
) {
}
