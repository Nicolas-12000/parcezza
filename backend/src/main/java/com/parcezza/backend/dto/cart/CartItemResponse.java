package com.parcezza.backend.dto.cart;

import java.math.BigDecimal;

public record CartItemResponse(
    Long id,
    Long productId,
    String productName,
    Long variantId,
    Integer quantity,
    BigDecimal unitPrice,
    String currency,
    BigDecimal lineTotal
) {
}
