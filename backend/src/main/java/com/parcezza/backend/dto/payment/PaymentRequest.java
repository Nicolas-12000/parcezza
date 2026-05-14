package com.parcezza.backend.dto.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PaymentRequest(
    @NotNull Long orderId,
    @NotBlank String provider,
    String providerRef,
    @NotBlank @Size(min = 13, max = 19) String cardNumber,
    @NotBlank String cardHolder,
    @NotNull Integer expMonth,
    @NotNull Integer expYear,
    @NotBlank @Size(min = 3, max = 4) String cvv
) {
}
