package com.parcezza.backend.service;

import com.parcezza.backend.domain.Product;
import com.parcezza.backend.domain.ProductVariant;

public interface InventoryService {
    void reserve(Product product, ProductVariant variant, int quantity, String referenceType, Long referenceId);
    void release(Product product, ProductVariant variant, int quantity, String referenceType, Long referenceId);
    void consume(Product product, ProductVariant variant, int quantity, String referenceType, Long referenceId);
    void refund(Product product, ProductVariant variant, int quantity, String referenceType, Long referenceId);
}
