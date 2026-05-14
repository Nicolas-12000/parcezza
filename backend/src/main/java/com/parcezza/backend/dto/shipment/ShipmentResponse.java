package com.parcezza.backend.dto.shipment;

import com.parcezza.backend.domain.enums.ShipmentStatus;

public record ShipmentResponse(
    Long id,
    Long orderId,
    ShipmentStatus status,
    String trackingCode
) {
}
