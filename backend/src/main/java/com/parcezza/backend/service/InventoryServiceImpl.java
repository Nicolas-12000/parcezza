package com.parcezza.backend.service;

import com.parcezza.backend.domain.InventoryMovement;
import com.parcezza.backend.domain.Product;
import com.parcezza.backend.domain.ProductVariant;
import com.parcezza.backend.domain.enums.InventoryMovementType;
import com.parcezza.backend.exception.BadRequestException;
import com.parcezza.backend.repository.InventoryMovementRepository;
import com.parcezza.backend.repository.ProductRepository;
import com.parcezza.backend.repository.ProductVariantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final InventoryMovementRepository movementRepository;

    public InventoryServiceImpl(ProductRepository productRepository,
                                ProductVariantRepository variantRepository,
                                InventoryMovementRepository movementRepository) {
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.movementRepository = movementRepository;
    }

    @Override
    @Transactional
    public void reserve(Product product, ProductVariant variant, int quantity, String referenceType, Long referenceId) {
        adjustStock(product, variant, quantity, InventoryMovementType.RESERVE, referenceType, referenceId, -1);
    }

    @Override
    @Transactional
    public void release(Product product, ProductVariant variant, int quantity, String referenceType, Long referenceId) {
        adjustStock(product, variant, quantity, InventoryMovementType.RELEASE, referenceType, referenceId, 1);
    }

    @Override
    @Transactional
    public void consume(Product product, ProductVariant variant, int quantity, String referenceType, Long referenceId) {
        recordMovement(product, variant, quantity, InventoryMovementType.SALE, referenceType, referenceId);
    }

    @Override
    @Transactional
    public void refund(Product product, ProductVariant variant, int quantity, String referenceType, Long referenceId) {
        adjustStock(product, variant, quantity, InventoryMovementType.REFUND, referenceType, referenceId, 1);
    }

    private void adjustStock(Product product,
                             ProductVariant variant,
                             int quantity,
                             InventoryMovementType type,
                             String referenceType,
                             Long referenceId,
                             int direction) {
        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be positive");
        }

        if (variant != null) {
            int available = variant.getStock() == null ? 0 : variant.getStock();
            int updated = available + (direction * quantity);
            if (updated < 0) {
                throw new BadRequestException("Not enough variant stock");
            }
            variant.setStock(updated);
            variantRepository.save(variant);
            recordMovement(product, variant, quantity, type, referenceType, referenceId);
            return;
        }

        int available = product.getStock() == null ? 0 : product.getStock();
        int updated = available + (direction * quantity);
        if (updated < 0) {
            throw new BadRequestException("Not enough product stock");
        }
        product.setStock(updated);
        productRepository.save(product);
        recordMovement(product, null, quantity, type, referenceType, referenceId);
    }

    private void recordMovement(Product product,
                                ProductVariant variant,
                                int quantity,
                                InventoryMovementType type,
                                String referenceType,
                                Long referenceId) {
        InventoryMovement movement = new InventoryMovement();
        movement.setProduct(product);
        movement.setVariant(variant);
        movement.setQuantity(quantity);
        movement.setType(type);
        movement.setReferenceType(referenceType);
        movement.setReferenceId(referenceId);
        movementRepository.save(movement);
    }
}
