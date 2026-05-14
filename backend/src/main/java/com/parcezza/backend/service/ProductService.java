package com.parcezza.backend.service;

import com.parcezza.backend.dto.product.ProductResponse;
import com.parcezza.backend.dto.product.ProductUpsertRequest;
import java.util.List;

public interface ProductService {
    ProductResponse create(ProductUpsertRequest request);
    ProductResponse update(Long productId, ProductUpsertRequest request);
    ProductResponse getById(Long productId);
    List<ProductResponse> listAll();
    void delete(Long productId);
}
