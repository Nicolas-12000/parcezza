package com.parcezza.backend.service;

import com.parcezza.backend.dto.product.ProductVariantRequest;
import com.parcezza.backend.dto.product.ProductVariantResponse;
import java.util.List;

public interface ProductVariantService {
    ProductVariantResponse create(Long productId, ProductVariantRequest request);
    ProductVariantResponse update(Long productId, Long variantId, ProductVariantRequest request);
    List<ProductVariantResponse> listByProduct(Long productId);
    void delete(Long productId, Long variantId);
}
