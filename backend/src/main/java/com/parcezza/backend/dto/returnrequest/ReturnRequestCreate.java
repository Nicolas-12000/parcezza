package com.parcezza.backend.dto.returnrequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReturnRequestCreate(
    @NotBlank @Size(max = 500) String reason
) {
}
