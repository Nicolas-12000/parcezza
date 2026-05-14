package com.parcezza.backend.dto.shipment;

import com.parcezza.backend.domain.enums.ShipmentStatus;
import jakarta.validation.constraints.NotNull;

public record ShipmentStatusRequest(@NotNull ShipmentStatus status, String trackingCode) {
}
