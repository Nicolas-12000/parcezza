package com.parcezza.backend.dto.payment;

import com.parcezza.backend.domain.enums.PaymentStatus;
import java.math.BigDecimal;

public record PaymentResponse(
    Long id,
    Long orderId,
    PaymentStatus status,
    String provider,
    String providerRef,
    String cardLast4,
    BigDecimal amount,
    String currency
) {
}
