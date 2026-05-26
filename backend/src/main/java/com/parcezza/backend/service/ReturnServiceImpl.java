package com.parcezza.backend.service;

import com.parcezza.backend.domain.Order;
import com.parcezza.backend.domain.ReturnRequest;
import com.parcezza.backend.domain.enums.OrderStatus;
import com.parcezza.backend.domain.enums.ReturnStatus;
import com.parcezza.backend.dto.returnrequest.ReturnRequestCreate;
import com.parcezza.backend.dto.returnrequest.ReturnResponse;
import com.parcezza.backend.dto.returnrequest.ReturnStatusUpdateRequest;
import com.parcezza.backend.exception.BadRequestException;
import com.parcezza.backend.exception.ResourceNotFoundException;
import com.parcezza.backend.repository.OrderRepository;
import com.parcezza.backend.repository.ReturnRequestRepository;
import com.parcezza.backend.security.CurrentUserService;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReturnServiceImpl implements ReturnService {

    private final OrderRepository orderRepository;
    private final ReturnRequestRepository returnRepository;
    private final PaymentService paymentService;
    private final CurrentUserService currentUserService;

    public ReturnServiceImpl(OrderRepository orderRepository,
                             ReturnRequestRepository returnRepository,
                             PaymentService paymentService,
                             CurrentUserService currentUserService) {
        this.orderRepository = orderRepository;
        this.returnRepository = returnRepository;
        this.paymentService = paymentService;
        this.currentUserService = currentUserService;
    }

    @Override
    @Transactional
    public ReturnResponse requestReturn(Long orderId, ReturnRequestCreate request) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!Objects.equals(order.getUser().getId(), currentUserService.getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Order not found");
        }

        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new BadRequestException("Order cannot be returned yet");
        }

        if (returnRepository.findByOrder(order).isPresent()) {
            throw new BadRequestException("Return already requested");
        }

        ReturnRequest entity = new ReturnRequest();
        entity.setOrder(order);
        entity.setStatus(ReturnStatus.REQUESTED);
        entity.setReason(request.reason());

        return toResponse(returnRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public ReturnResponse getByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!Objects.equals(order.getUser().getId(), currentUserService.getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Return not found");
        }
        ReturnRequest entity = returnRepository.findByOrder(order)
            .orElseThrow(() -> new ResourceNotFoundException("Return not found"));
        return toResponse(entity);
    }

        @Override
        @Transactional(readOnly = true)
        public java.util.List<ReturnResponse> listAll() {
            return returnRepository.findAll().stream().map(this::toResponse).toList();
        }

    @Override
    @Transactional
    public ReturnResponse updateStatus(Long returnId, ReturnStatusUpdateRequest request) {
        ReturnRequest entity = returnRepository.findById(returnId)
            .orElseThrow(() -> new ResourceNotFoundException("Return not found"));

        ReturnStatus current = entity.getStatus();
        ReturnStatus target = request.status();

        if (!isValidTransition(current, target)) {
            throw new BadRequestException("Invalid return status transition");
        }

        entity.setStatus(target);
        if (request.note() != null && !request.note().isBlank()) {
            entity.setNote(request.note());
        }

        if (target == ReturnStatus.REFUNDED) {
            paymentService.refund(entity.getOrder().getId());
        }

        return toResponse(returnRepository.save(entity));
    }

    private boolean isValidTransition(ReturnStatus current, ReturnStatus target) {
        return switch (current) {
            case REQUESTED -> target == ReturnStatus.APPROVED || target == ReturnStatus.REJECTED;
            case APPROVED -> target == ReturnStatus.RECEIVED;
            case RECEIVED -> target == ReturnStatus.REFUNDED;
            default -> false;
        };
    }

    private ReturnResponse toResponse(ReturnRequest entity) {
        return new ReturnResponse(
            entity.getId(),
            entity.getOrder().getId(),
            entity.getStatus(),
            entity.getReason(),
            entity.getNote(),
            entity.getCreatedAt()
        );
    }
}
