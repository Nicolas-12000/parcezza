package com.parcezza.backend.service;

import com.parcezza.backend.domain.Address;
import com.parcezza.backend.domain.Cart;
import com.parcezza.backend.domain.CartItem;
import com.parcezza.backend.domain.Order;
import com.parcezza.backend.domain.OrderItem;
import com.parcezza.backend.domain.Shipment;
import com.parcezza.backend.domain.enums.OrderStatus;
import com.parcezza.backend.domain.enums.ShipmentStatus;
import com.parcezza.backend.dto.order.CheckoutRequest;
import com.parcezza.backend.dto.order.OrderItemResponse;
import com.parcezza.backend.dto.order.OrderResponse;
import com.parcezza.backend.exception.BadRequestException;
import com.parcezza.backend.exception.ResourceNotFoundException;
import com.parcezza.backend.repository.AddressRepository;
import com.parcezza.backend.repository.CartItemRepository;
import com.parcezza.backend.repository.CartRepository;
import com.parcezza.backend.repository.OrderRepository;
import com.parcezza.backend.repository.ShipmentRepository;
import com.parcezza.backend.security.CurrentUserService;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final ShipmentRepository shipmentRepository;
    private final AddressRepository addressRepository;
    private final CurrentUserService currentUserService;
    private final InventoryService inventoryService;

    public OrderServiceImpl(CartRepository cartRepository,
                            CartItemRepository cartItemRepository,
                            OrderRepository orderRepository,
                            ShipmentRepository shipmentRepository,
                            AddressRepository addressRepository,
                            CurrentUserService currentUserService,
                            InventoryService inventoryService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.shipmentRepository = shipmentRepository;
        this.addressRepository = addressRepository;
        this.currentUserService = currentUserService;
        this.inventoryService = inventoryService;
    }

    @Override
    @Transactional
    public OrderResponse checkout(CheckoutRequest request) {
        Cart cart = cartRepository.findByUser(currentUserService.getCurrentUser())
            .orElseThrow(() -> new BadRequestException("Cart is empty"));
        List<CartItem> items = cartItemRepository.findByCart(cart);

        if (items.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        if (items.stream().anyMatch(item -> item.getReservedUntil() != null && item.getReservedUntil().isBefore(Instant.now()))) {
            for (CartItem item : items) {
                if (item.getReservedUntil() != null && item.getReservedUntil().isBefore(Instant.now())) {
                    inventoryService.release(item.getProduct(), item.getVariant(), item.getQuantity(), "CART", cart.getId());
                    cartItemRepository.delete(item);
                }
            }
            throw new BadRequestException("Cart has expired items");
        }

        Order order = new Order();
        order.setUser(currentUserService.getCurrentUser());
        order.setStatus(OrderStatus.CREATED);
        order.setCurrency(items.get(0).getCurrency());

        if (request != null && request.shippingAddressId() != null) {
            Address address = addressRepository.findById(request.shippingAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
            if (!Objects.equals(address.getUser().getId(), currentUserService.getCurrentUser().getId())) {
                throw new ResourceNotFoundException("Address not found");
            }
            order.setShippingAddress(address);
        }

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : items) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setVariant(cartItem.getVariant());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItem.setCurrency(cartItem.getCurrency());
            orderItem.setLineTotal(cartItem.getLineTotal());
            order.getItems().add(orderItem);
            total = total.add(cartItem.getLineTotal());
        }

        order.setTotalAmount(total);
        Order saved = orderRepository.save(order);

        for (CartItem cartItem : items) {
            inventoryService.consume(cartItem.getProduct(), cartItem.getVariant(), cartItem.getQuantity(), "ORDER", saved.getId());
        }

        Shipment shipment = new Shipment();
        shipment.setOrder(saved);
        shipment.setStatus(ShipmentStatus.PENDING);
        shipment.setTrackingCode("TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        shipmentRepository.save(shipment);

        cart.getItems().clear();
        cartRepository.save(cart);

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getById(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!Objects.equals(order.getUser().getId(), currentUserService.getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Order not found");
        }
        return toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> listMyOrders() {
        return orderRepository.findByUser(currentUserService.getCurrentUser()).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse cancel(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!Objects.equals(order.getUser().getId(), currentUserService.getCurrentUser().getId())) {
            throw new ResourceNotFoundException("Order not found");
        }

        if (order.getStatus() != OrderStatus.CREATED
            && order.getStatus() != OrderStatus.PAID
            && order.getStatus() != OrderStatus.PROCESSING) {
            throw new BadRequestException("Order cannot be cancelled");
        }

        order.getItems().forEach(item -> inventoryService.refund(
            item.getProduct(),
            item.getVariant(),
            item.getQuantity(),
            "ORDER",
            order.getId()
        ));

        order.setStatus(OrderStatus.CANCELLED);
        Shipment shipment = shipmentRepository.findByOrder(order).orElse(null);
        if (shipment != null) {
            shipment.setStatus(ShipmentStatus.CANCELLED);
            shipmentRepository.save(shipment);
        }

        return toResponse(orderRepository.save(order));
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
            .map(item -> new OrderItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getVariant() == null ? null : item.getVariant().getId(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getCurrency(),
                item.getLineTotal()
            ))
            .collect(Collectors.toList());

        Shipment shipment = shipmentRepository.findByOrder(order).orElse(null);
        ShipmentStatus shipmentStatus = shipment == null ? null : shipment.getStatus();
        String trackingCode = shipment == null ? null : shipment.getTrackingCode();

        return new OrderResponse(
            order.getId(),
            order.getStatus(),
            order.getTotalAmount(),
            order.getCurrency(),
            shipmentStatus,
            trackingCode,
            order.getCreatedAt(),
            items
        );
    }
}
