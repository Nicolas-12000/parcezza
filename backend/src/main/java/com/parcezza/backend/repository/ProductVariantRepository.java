package com.parcezza.backend.repository;

import com.parcezza.backend.domain.ProductVariant;
import com.parcezza.backend.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
	List<ProductVariant> findByProduct(Product product);
}
