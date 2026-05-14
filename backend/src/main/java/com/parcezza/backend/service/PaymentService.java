package com.parcezza.backend.service;

import com.parcezza.backend.dto.payment.PaymentRequest;
import com.parcezza.backend.dto.payment.PaymentResponse;

public interface PaymentService {
    PaymentResponse confirm(PaymentRequest request);
    PaymentResponse refund(Long orderId);
}
