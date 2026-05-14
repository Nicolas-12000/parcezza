package com.parcezza.backend.service;

import com.parcezza.backend.dto.shipment.ShipmentResponse;
import com.parcezza.backend.dto.shipment.ShipmentStatusRequest;

public interface ShipmentService {
    ShipmentResponse getByOrder(Long orderId);
    ShipmentResponse updateStatus(Long shipmentId, ShipmentStatusRequest request);
}
