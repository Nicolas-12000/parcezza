package com.parcezza.backend.dto.returnrequest;

import com.parcezza.backend.domain.enums.ReturnStatus;
import java.time.Instant;

public record ReturnResponse(
    Long id,
    Long orderId,
    ReturnStatus status,
    String reason,
    String note,
    Instant createdAt
) {
}
