package com.parcezza.backend.repository;

import com.parcezza.backend.domain.ProductVariant;
import com.parcezza.backend.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
	List<ProductVariant> findByProduct(Product product);
	
	@Modifying
	@Query("UPDATE ProductVariant v SET v.stock = v.stock + :amount WHERE v.id = :id AND (v.stock + :amount) >= 0")
	int adjustStockAtomically(@Param("id") Long id, @Param("amount") int amount);
}
