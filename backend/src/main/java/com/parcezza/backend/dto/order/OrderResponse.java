package com.parcezza.backend.dto.order;

import com.parcezza.backend.domain.enums.OrderStatus;
import com.parcezza.backend.domain.enums.ShipmentStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
    Long id,
    OrderStatus status,
    BigDecimal totalAmount,
    String currency,
    ShipmentStatus shipmentStatus,
    String trackingCode,
    Instant createdAt,
    List<OrderItemResponse> items
) {
}
