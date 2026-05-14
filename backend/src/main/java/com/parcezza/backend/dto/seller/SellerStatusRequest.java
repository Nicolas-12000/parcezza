package com.parcezza.backend.dto.seller;

import com.parcezza.backend.domain.enums.SellerStatus;
import jakarta.validation.constraints.NotNull;

public record SellerStatusRequest(@NotNull SellerStatus status) {
}
