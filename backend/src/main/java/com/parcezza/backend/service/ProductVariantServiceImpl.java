package com.parcezza.backend.service;

import com.parcezza.backend.domain.Product;
import com.parcezza.backend.domain.ProductVariant;
import com.parcezza.backend.domain.VariantAttribute;
import com.parcezza.backend.dto.product.ProductVariantRequest;
import com.parcezza.backend.dto.product.ProductVariantResponse;
import com.parcezza.backend.dto.product.VariantAttributeRequest;
import com.parcezza.backend.dto.product.VariantAttributeResponse;
import com.parcezza.backend.exception.BadRequestException;
import com.parcezza.backend.exception.ResourceNotFoundException;
import com.parcezza.backend.repository.ProductRepository;
import com.parcezza.backend.repository.ProductVariantRepository;
import com.parcezza.backend.security.CurrentUserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final CurrentUserService currentUserService;

    public ProductVariantServiceImpl(ProductRepository productRepository,
                                     ProductVariantRepository variantRepository,
                                     CurrentUserService currentUserService) {
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    @Transactional
    public ProductVariantResponse create(Long productId, ProductVariantRequest request) {
        Product product = getOwnedProduct(productId);

        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        applyRequest(variant, request);

        return toResponse(variantRepository.save(variant));
    }

    @Override
    @Transactional
    public ProductVariantResponse update(Long productId, Long variantId, ProductVariantRequest request) {
        Product product = getOwnedProduct(productId);

        ProductVariant variant = variantRepository.findById(variantId)
            .orElseThrow(() -> new ResourceNotFoundException("Variant not found"));

        if (!Objects.equals(variant.getProduct().getId(), product.getId())) {
            throw new BadRequestException("Variant does not belong to product");
        }

        applyRequest(variant, request);

        return toResponse(variantRepository.save(variant));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantResponse> listByProduct(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return variantRepository.findByProduct(product).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long productId, Long variantId) {
        Product product = getOwnedProduct(productId);
        ProductVariant variant = variantRepository.findById(variantId)
            .orElseThrow(() -> new ResourceNotFoundException("Variant not found"));

        if (!Objects.equals(variant.getProduct().getId(), product.getId())) {
            throw new BadRequestException("Variant does not belong to product");
        }

        variantRepository.delete(variant);
    }

    private Product getOwnedProduct(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Long ownerId = product.getSeller() == null ? null : product.getSeller().getOwner().getId();
        Long currentUserId = currentUserService.getCurrentUser().getId();

        if (ownerId == null || !Objects.equals(ownerId, currentUserId)) {
            throw new AccessDeniedException("Not allowed");
        }

        return product;
    }

    private void applyRequest(ProductVariant variant, ProductVariantRequest request) {
        variant.setSku(request.sku());
        variant.setPriceOverride(request.priceOverride());
        variant.setStock(request.stock() == null ? 0 : request.stock());

        List<VariantAttribute> attributes = new ArrayList<>();
        if (request.attributes() != null) {
            for (VariantAttributeRequest attrRequest : request.attributes()) {
                VariantAttribute attribute = new VariantAttribute();
                attribute.setName(attrRequest.name());
                attribute.setValue(attrRequest.value());
                attribute.setVariant(variant);
                attributes.add(attribute);
            }
        }

        variant.getAttributes().clear();
        variant.getAttributes().addAll(attributes);
    }

    private ProductVariantResponse toResponse(ProductVariant variant) {
        List<VariantAttributeResponse> attributes = variant.getAttributes().stream()
            .map(attr -> new VariantAttributeResponse(attr.getId(), attr.getName(), attr.getValue()))
            .collect(Collectors.toList());

        return new ProductVariantResponse(
            variant.getId(),
            variant.getSku(),
            variant.getPriceOverride(),
            variant.getStock(),
            attributes
        );
    }
}
