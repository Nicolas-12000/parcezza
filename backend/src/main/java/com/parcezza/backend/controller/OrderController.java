package com.parcezza.backend.controller;

import com.parcezza.backend.dto.order.CheckoutRequest;
import com.parcezza.backend.dto.order.OrderResponse;
import com.parcezza.backend.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(@Valid @RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(orderService.checkout(request));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> list() {
        return ResponseEntity.ok(orderService.listMyOrders());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> get(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getById(orderId));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancel(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancel(orderId));
    }
}
