package com.parcezza.backend.service;

import com.parcezza.backend.dto.cart.CartItemRequest;
import com.parcezza.backend.dto.cart.CartResponse;

public interface CartService {
    CartResponse getMyCart();
    CartResponse addItem(CartItemRequest request);
    CartResponse updateItem(Long itemId, CartItemRequest request);
    CartResponse removeItem(Long itemId);
    CartResponse clear();
}
