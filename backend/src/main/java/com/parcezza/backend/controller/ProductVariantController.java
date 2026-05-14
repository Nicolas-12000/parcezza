package com.parcezza.backend.controller;

import com.parcezza.backend.dto.product.ProductVariantRequest;
import com.parcezza.backend.dto.product.ProductVariantResponse;
import com.parcezza.backend.service.ProductVariantService;
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
@RequestMapping("/api/products/{productId}/variants")
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    public ProductVariantController(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    @GetMapping
    public ResponseEntity<List<ProductVariantResponse>> list(@PathVariable Long productId) {
        return ResponseEntity.ok(productVariantService.listByProduct(productId));
    }

    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductVariantResponse> create(@PathVariable Long productId,
                                                        @Valid @RequestBody ProductVariantRequest request) {
        return ResponseEntity.ok(productVariantService.create(productId, request));
    }

    @PutMapping("/{variantId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductVariantResponse> update(@PathVariable Long productId,
                                                        @PathVariable Long variantId,
                                                        @Valid @RequestBody ProductVariantRequest request) {
        return ResponseEntity.ok(productVariantService.update(productId, variantId, request));
    }

    @DeleteMapping("/{variantId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> delete(@PathVariable Long productId, @PathVariable Long variantId) {
        productVariantService.delete(productId, variantId);
        return ResponseEntity.noContent().build();
    }
}
