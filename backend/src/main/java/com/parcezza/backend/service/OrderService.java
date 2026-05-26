package com.parcezza.backend.service;

import com.parcezza.backend.dto.order.CheckoutRequest;
import com.parcezza.backend.dto.order.OrderResponse;
import java.util.List;

public interface OrderService {
    OrderResponse checkout(CheckoutRequest request);
    OrderResponse getById(Long orderId);
    List<OrderResponse> listMyOrders();
    OrderResponse cancel(Long orderId);
}
