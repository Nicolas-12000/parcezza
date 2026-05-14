package com.parcezza.backend.service;

import com.parcezza.backend.domain.Catalog;
import com.parcezza.backend.domain.Product;
import com.parcezza.backend.dto.catalog.CatalogRequest;
import com.parcezza.backend.dto.catalog.CatalogResponse;
import com.parcezza.backend.exception.DuplicateResourceException;
import com.parcezza.backend.exception.ResourceNotFoundException;
import com.parcezza.backend.repository.CatalogRepository;
import com.parcezza.backend.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;
    private final ProductRepository productRepository;

    public CatalogServiceImpl(CatalogRepository catalogRepository, ProductRepository productRepository) {
        this.catalogRepository = catalogRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public CatalogResponse create(CatalogRequest request) {
        if (catalogRepository.findBySlug(request.slug()).isPresent()) {
            throw new DuplicateResourceException("Catalog slug already exists");
        }

        Catalog catalog = new Catalog();
        catalog.setName(request.name());
        catalog.setSlug(request.slug());

        return toResponse(catalogRepository.save(catalog));
    }

    @Override
    @Transactional
    public CatalogResponse update(Long catalogId, CatalogRequest request) {
        Catalog catalog = catalogRepository.findById(catalogId)
            .orElseThrow(() -> new ResourceNotFoundException("Catalog not found"));

        catalogRepository.findBySlug(request.slug())
            .filter(existing -> !existing.getId().equals(catalogId))
            .ifPresent(existing -> {
                throw new DuplicateResourceException("Catalog slug already exists");
            });

        catalog.setName(request.name());
        catalog.setSlug(request.slug());

        return toResponse(catalogRepository.save(catalog));
    }

    @Override
    @Transactional(readOnly = true)
    public CatalogResponse getById(Long catalogId) {
        Catalog catalog = catalogRepository.findById(catalogId)
            .orElseThrow(() -> new ResourceNotFoundException("Catalog not found"));
        return toResponse(catalog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogResponse> listAll() {
        return catalogRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long catalogId) {
        Catalog catalog = catalogRepository.findById(catalogId)
            .orElseThrow(() -> new ResourceNotFoundException("Catalog not found"));
        catalogRepository.delete(catalog);
    }

    @Override
    @Transactional
    public CatalogResponse addProduct(Long catalogId, Long productId) {
        Catalog catalog = catalogRepository.findById(catalogId)
            .orElseThrow(() -> new ResourceNotFoundException("Catalog not found"));
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        boolean alreadyAdded = catalog.getProducts().stream()
            .anyMatch(existing -> existing.getId().equals(product.getId()));
        if (!alreadyAdded) {
            catalog.getProducts().add(product);
        }

        return toResponse(catalogRepository.save(catalog));
    }

    @Override
    @Transactional
    public CatalogResponse removeProduct(Long catalogId, Long productId) {
        Catalog catalog = catalogRepository.findById(catalogId)
            .orElseThrow(() -> new ResourceNotFoundException("Catalog not found"));
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        catalog.getProducts().remove(product);
        return toResponse(catalogRepository.save(catalog));
    }

    private CatalogResponse toResponse(Catalog catalog) {
        List<Long> productIds = catalog.getProducts().stream()
            .map(Product::getId)
            .collect(Collectors.toList());

        return new CatalogResponse(catalog.getId(), catalog.getName(), catalog.getSlug(), productIds);
    }
}
