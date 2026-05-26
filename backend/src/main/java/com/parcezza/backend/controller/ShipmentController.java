package com.parcezza.backend.controller;

import com.parcezza.backend.dto.shipment.ShipmentResponse;
import com.parcezza.backend.dto.shipment.ShipmentStatusRequest;
import com.parcezza.backend.service.ShipmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ShipmentResponse> getByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(shipmentService.getByOrder(orderId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<java.util.List<ShipmentResponse>> listAll() {
        return ResponseEntity.ok(shipmentService.listAll());
    }

    @PatchMapping("/{shipmentId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShipmentResponse> updateStatus(@PathVariable Long shipmentId,
                                                         @Valid @RequestBody ShipmentStatusRequest request) {
        return ResponseEntity.ok(shipmentService.updateStatus(shipmentId, request));
    }
}
