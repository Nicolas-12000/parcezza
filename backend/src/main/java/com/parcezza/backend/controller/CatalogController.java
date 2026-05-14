package com.parcezza.backend.controller;

import com.parcezza.backend.dto.catalog.CatalogRequest;
import com.parcezza.backend.dto.catalog.CatalogResponse;
import com.parcezza.backend.service.CatalogService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catalogs")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public ResponseEntity<List<CatalogResponse>> list() {
        return ResponseEntity.ok(catalogService.listAll());
    }

    @GetMapping("/{catalogId}")
    public ResponseEntity<CatalogResponse> get(@PathVariable Long catalogId) {
        return ResponseEntity.ok(catalogService.getById(catalogId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CatalogResponse> create(@Valid @RequestBody CatalogRequest request) {
        return ResponseEntity.ok(catalogService.create(request));
    }

    @PutMapping("/{catalogId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CatalogResponse> update(@PathVariable Long catalogId,
                                                  @Valid @RequestBody CatalogRequest request) {
        return ResponseEntity.ok(catalogService.update(catalogId, request));
    }

    @DeleteMapping("/{catalogId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long catalogId) {
        catalogService.delete(catalogId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{catalogId}/products/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CatalogResponse> addProduct(@PathVariable Long catalogId, @PathVariable Long productId) {
        return ResponseEntity.ok(catalogService.addProduct(catalogId, productId));
    }

    @DeleteMapping("/{catalogId}/products/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CatalogResponse> removeProduct(@PathVariable Long catalogId, @PathVariable Long productId) {
        return ResponseEntity.ok(catalogService.removeProduct(catalogId, productId));
    }
}
