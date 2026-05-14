package com.parcezza.backend.repository;

import com.parcezza.backend.domain.VariantAttribute;
import com.parcezza.backend.domain.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VariantAttributeRepository extends JpaRepository<VariantAttribute, Long> {
	List<VariantAttribute> findByVariant(ProductVariant variant);
}
