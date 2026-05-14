package com.parcezza.backend.controller;

import com.parcezza.backend.dto.payment.PaymentRequest;
import com.parcezza.backend.dto.payment.PaymentResponse;
import com.parcezza.backend.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/confirm")
    public ResponseEntity<PaymentResponse> confirm(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.confirm(request));
    }
}
