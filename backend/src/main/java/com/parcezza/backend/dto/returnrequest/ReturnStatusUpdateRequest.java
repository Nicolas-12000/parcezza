package com.parcezza.backend.dto.returnrequest;

import com.parcezza.backend.domain.enums.ReturnStatus;
import jakarta.validation.constraints.NotNull;

public record ReturnStatusUpdateRequest(
    @NotNull ReturnStatus status,
    String note
) {
}
