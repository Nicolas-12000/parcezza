package com.parcezza.backend.repository;

import com.parcezza.backend.domain.InventoryMovement;
import com.parcezza.backend.domain.Product;
import com.parcezza.backend.domain.ProductVariant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {
    List<InventoryMovement> findByProduct(Product product);
    List<InventoryMovement> findByVariant(ProductVariant variant);
}
