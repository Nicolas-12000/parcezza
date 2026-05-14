package com.parcezza.backend.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VariantAttributeRequest(
    @NotBlank @Size(max = 100) String name,
    @NotBlank @Size(max = 100) String value
) {
}
