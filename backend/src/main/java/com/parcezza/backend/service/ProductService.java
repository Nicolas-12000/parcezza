package com.parcezza.backend.service;

import com.parcezza.backend.dto.product.ProductResponse;
import com.parcezza.backend.dto.product.ProductUpsertRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponse create(ProductUpsertRequest request);
    ProductResponse update(Long productId, ProductUpsertRequest request);
    ProductResponse getById(Long productId);
    Page<ProductResponse> listAll(String query, String collectionSlug, Pageable pageable);
    void delete(Long productId);
}
