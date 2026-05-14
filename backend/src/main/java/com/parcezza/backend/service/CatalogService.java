package com.parcezza.backend.service;

import com.parcezza.backend.dto.catalog.CatalogRequest;
import com.parcezza.backend.dto.catalog.CatalogResponse;
import java.util.List;

public interface CatalogService {
    CatalogResponse create(CatalogRequest request);
    CatalogResponse update(Long catalogId, CatalogRequest request);
    CatalogResponse getById(Long catalogId);
    List<CatalogResponse> listAll();
    void delete(Long catalogId);
    CatalogResponse addProduct(Long catalogId, Long productId);
    CatalogResponse removeProduct(Long catalogId, Long productId);
}
