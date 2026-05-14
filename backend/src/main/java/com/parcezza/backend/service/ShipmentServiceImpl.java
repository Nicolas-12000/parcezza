package com.parcezza.backend.service;

import com.parcezza.backend.domain.Order;
import com.parcezza.backend.domain.Shipment;
import com.parcezza.backend.domain.enums.OrderStatus;
import com.parcezza.backend.domain.enums.ShipmentStatus;
import com.parcezza.backend.dto.shipment.ShipmentResponse;
import com.parcezza.backend.dto.shipment.ShipmentStatusRequest;
import com.parcezza.backend.exception.BadRequestException;
import com.parcezza.backend.exception.ResourceNotFoundException;
import com.parcezza.backend.repository.OrderRepository;
import com.parcezza.backend.repository.ShipmentRepository;
import com.parcezza.backend.security.CurrentUserService;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final OrderRepository orderRepository;
    private final CurrentUserService currentUserService;

    public ShipmentServiceImpl(ShipmentRepository shipmentRepository,
                               OrderRepository orderRepository,
                               CurrentUserService currentUserService) {
        this.shipmentRepository = shipmentRepository;
        this.orderRepository = orderRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentResponse getByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!Objects.equals(order.getUser().getId(), currentUserService.getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Shipment not found");
        }

        Shipment shipment = shipmentRepository.findByOrder(order)
            .orElseThrow(() -> new ResourceNotFoundException("Shipment not found"));
        return toResponse(shipment);
    }

    @Override
    @Transactional
    public ShipmentResponse updateStatus(Long shipmentId, ShipmentStatusRequest request) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Shipment not found"));
        ShipmentStatus current = shipment.getStatus();
        ShipmentStatus target = request.status();

        if (!isValidTransition(current, target)) {
            throw new BadRequestException("Invalid shipment status transition");
        }

        Order order = shipment.getOrder();
        if (!isOrderTransitionAllowed(order.getStatus(), target)) {
            throw new BadRequestException("Order status does not allow shipment update");
        }

        shipment.setStatus(target);
        if (request.trackingCode() != null && !request.trackingCode().isBlank()) {
            shipment.setTrackingCode(request.trackingCode());
        }

        switch (target) {
            case SHIPPED -> order.setStatus(OrderStatus.SHIPPED);
            case IN_TRANSIT -> order.setStatus(OrderStatus.PROCESSING);
            case DELIVERED -> order.setStatus(OrderStatus.DELIVERED);
            case CANCELLED -> order.setStatus(OrderStatus.CANCELLED);
            case RETURNED -> order.setStatus(OrderStatus.REFUNDED);
            default -> {
            }
        }

        return toResponse(shipmentRepository.save(shipment));
    }

    private boolean isValidTransition(ShipmentStatus current, ShipmentStatus target) {
        return switch (current) {
            case PENDING -> target == ShipmentStatus.SHIPPED || target == ShipmentStatus.CANCELLED;
            case SHIPPED -> target == ShipmentStatus.IN_TRANSIT || target == ShipmentStatus.CANCELLED;
            case IN_TRANSIT -> target == ShipmentStatus.DELIVERED;
            case DELIVERED -> target == ShipmentStatus.RETURNED;
            case RETURNED, CANCELLED -> false;
        };
    }

    private boolean isOrderTransitionAllowed(OrderStatus status, ShipmentStatus target) {
        return switch (target) {
            case SHIPPED, IN_TRANSIT -> status == OrderStatus.PAID || status == OrderStatus.PROCESSING;
            case DELIVERED -> status == OrderStatus.SHIPPED || status == OrderStatus.PROCESSING;
            case CANCELLED -> status == OrderStatus.CREATED || status == OrderStatus.PAID || status == OrderStatus.PROCESSING;
            case RETURNED -> status == OrderStatus.DELIVERED || status == OrderStatus.REFUNDED;
            default -> true;
        };
    }

    private ShipmentResponse toResponse(Shipment shipment) {
        return new ShipmentResponse(
            shipment.getId(),
            shipment.getOrder().getId(),
            shipment.getStatus(),
            shipment.getTrackingCode()
        );
    }
}
