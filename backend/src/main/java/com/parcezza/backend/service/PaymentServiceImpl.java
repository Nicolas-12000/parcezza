package com.parcezza.backend.service;

import com.parcezza.backend.domain.Order;
import com.parcezza.backend.domain.Payment;
import com.parcezza.backend.domain.enums.OrderStatus;
import com.parcezza.backend.domain.enums.PaymentStatus;
import com.parcezza.backend.dto.payment.PaymentRequest;
import com.parcezza.backend.dto.payment.PaymentResponse;
import com.parcezza.backend.exception.BadRequestException;
import com.parcezza.backend.exception.ResourceNotFoundException;
import com.parcezza.backend.repository.OrderRepository;
import com.parcezza.backend.repository.PaymentRepository;
import com.parcezza.backend.security.CurrentUserService;
import com.parcezza.backend.service.payment.CardValidator;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final CurrentUserService currentUserService;
    private final CardValidator cardValidator;
    private final InventoryService inventoryService;

    public PaymentServiceImpl(OrderRepository orderRepository,
                              PaymentRepository paymentRepository,
                              CurrentUserService currentUserService,
                              CardValidator cardValidator,
                              InventoryService inventoryService) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.currentUserService = currentUserService;
        this.cardValidator = cardValidator;
        this.inventoryService = inventoryService;
    }

    @Override
    @Transactional
    public PaymentResponse confirm(PaymentRequest request) {
        Order order = orderRepository.findById(request.orderId())
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!Objects.equals(order.getUser().getId(), currentUserService.getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Order not found");
        }

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new BadRequestException("Order cannot be paid");
        }

        boolean validCard = cardValidator.isValid(
            request.cardNumber(),
            request.expMonth(),
            request.expYear(),
            request.cvv()
        );

        String normalized = request.cardNumber().replaceAll("[^0-9]", "");
        String last4 = normalized.length() >= 4 ? normalized.substring(normalized.length() - 4) : normalized;

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setProvider(request.provider());
        payment.setProviderRef(request.providerRef());
        payment.setAmount(order.getTotalAmount());
        payment.setCurrency(order.getCurrency());
        payment.setCardLast4(last4);

        if (validCard) {
            payment.setStatus(PaymentStatus.SUCCESS);
            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }

        return toResponse(paymentRepository.save(payment));
    }

    @Override
    @Transactional
    public PaymentResponse refund(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.DELIVERED && order.getStatus() != OrderStatus.PAID) {
            throw new BadRequestException("Order cannot be refunded");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setProvider("REFUND");
        payment.setProviderRef("REFUND");
        payment.setAmount(order.getTotalAmount());
        payment.setCurrency(order.getCurrency());
        payment.setStatus(PaymentStatus.REFUNDED);

        order.getItems().forEach(item -> inventoryService.refund(
            item.getProduct(),
            item.getVariant(),
            item.getQuantity(),
            "ORDER",
            order.getId()
        ));

        order.setStatus(OrderStatus.REFUNDED);
        orderRepository.save(order);

        return toResponse(paymentRepository.save(payment));
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
            payment.getId(),
            payment.getOrder().getId(),
            payment.getStatus(),
            payment.getProvider(),
            payment.getProviderRef(),
            payment.getCardLast4(),
            payment.getAmount(),
            payment.getCurrency()
        );
    }
}
