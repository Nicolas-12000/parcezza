package com.parcezza.backend.service;

import com.parcezza.backend.domain.Product;
import com.parcezza.backend.domain.Seller;
import com.parcezza.backend.domain.enums.SellerStatus;
import com.parcezza.backend.dto.product.ProductResponse;
import com.parcezza.backend.dto.product.ProductUpsertRequest;
import com.parcezza.backend.exception.BadRequestException;
import com.parcezza.backend.exception.DuplicateResourceException;
import com.parcezza.backend.exception.ResourceNotFoundException;
import com.parcezza.backend.repository.ProductRepository;
import com.parcezza.backend.repository.SellerRepository;
import com.parcezza.backend.security.CurrentUserService;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final CurrentUserService currentUserService;
    private final CatalogService catalogService;

    public ProductServiceImpl(ProductRepository productRepository,
                              SellerRepository sellerRepository,
                              CurrentUserService currentUserService,
                              CatalogService catalogService) {
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
        this.currentUserService = currentUserService;
        this.catalogService = catalogService;
    }

    @Override
    @Transactional
    public ProductResponse create(ProductUpsertRequest request) {
        if (productRepository.findBySku(request.sku()).isPresent()) {
            throw new DuplicateResourceException("SKU already exists");
        }

        Seller seller = getApprovedSeller();

        Product product = new Product();
        applyRequest(product, request);
        product.setSeller(seller);

        Product saved = productRepository.save(product);

        if (request.catalogIds() != null) {
            for (Long catalogId : request.catalogIds()) {
                // add product to requested catalogs (service layer will validate existence)
                catalogService.addProduct(catalogId, saved.getId());
            }
        }

        return toResponse(saved);
    }

    @Override
    @Transactional
    public ProductResponse update(Long productId, ProductUpsertRequest request) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        verifyOwnership(product);

        productRepository.findBySku(request.sku())
            .filter(existing -> !Objects.equals(existing.getId(), productId))
            .ifPresent(existing -> {
                throw new DuplicateResourceException("SKU already exists");
            });

        applyRequest(product, request);
        Product saved = productRepository.save(product);

        if (request.catalogIds() != null) {
            for (Long catalogId : request.catalogIds()) {
                catalogService.addProduct(catalogId, saved.getId());
            }
        }

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getById(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> listAll(String query, String collectionSlug, Pageable pageable) {
        Page<Product> page;
        if (collectionSlug != null && !collectionSlug.trim().isEmpty()) {
            if (query != null && !query.trim().isEmpty()) {
                return productRepository.searchProductsByCatalog(collectionSlug, query, pageable).map(this::toResponse);
            }
            return productRepository.findByCatalogSlug(collectionSlug, pageable).map(this::toResponse);
        }

        if (query != null && !query.trim().isEmpty()) {
            page = productRepository.searchProducts(query, pageable);
        } else {
            page = productRepository.findAll(pageable);
        }
        return page.map(this::toResponse);
    }

    @Override
    @Transactional
    public void delete(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        verifyOwnership(product);
        productRepository.delete(product);
    }

    private Seller getApprovedSeller() {
        Seller seller = sellerRepository.findFirstByOwner(currentUserService.getCurrentUser())
            .orElseThrow(() -> new BadRequestException("Seller profile not found"));

        if (seller.getStatus() != SellerStatus.APPROVED) {
            throw new BadRequestException("Seller not approved");
        }

        return seller;
    }

    private void verifyOwnership(Product product) {
        if (product.getSeller() == null) {
            throw new BadRequestException("Product has no seller");
        }
        Long ownerId = product.getSeller().getOwner().getId();
        Long currentUserId = currentUserService.getCurrentUser().getId();
        if (!Objects.equals(ownerId, currentUserId)) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    private void applyRequest(Product product, ProductUpsertRequest request) {
        product.setSku(request.sku());
        product.setName(request.name());
        product.setDescription(request.description());
        product.setBasePrice(request.basePrice());
        product.setStock(request.stock());
        product.setCurrency(request.currency());
        product.setActive(request.active());
    }

    private ProductResponse toResponse(Product product) {
        Long sellerId = product.getSeller() == null ? null : product.getSeller().getId();
        return new ProductResponse(
            product.getId(),
            product.getSku(),
            product.getName(),
            product.getDescription(),
            product.getBasePrice(),
            product.getStock(),
            product.getCurrency(),
            product.isActive(),
            sellerId
        );
    }
}
