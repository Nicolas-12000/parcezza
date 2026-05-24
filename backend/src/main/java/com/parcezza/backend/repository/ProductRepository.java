package com.parcezza.backend.repository;

import com.parcezza.backend.domain.Product;
import com.parcezza.backend.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    List<Product> findBySeller(Seller seller);

    @Query("SELECT p FROM Product p WHERE " +
        "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
        "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
        "LOWER(p.sku) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Product> searchProducts(@Param("query") String query, Pageable pageable);

    Page<Product> findByIdIn(List<Long> ids, Pageable pageable);

    @Query("SELECT p FROM Catalog c JOIN c.products p WHERE c.slug = :slug")
    Page<Product> findByCatalogSlug(@Param("slug") String slug, Pageable pageable);

    @Query("SELECT p FROM Catalog c JOIN c.products p WHERE c.slug = :slug AND (" +
        "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
        "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
        "LOWER(p.sku) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Product> searchProductsByCatalog(@Param("slug") String slug, @Param("query") String query, Pageable pageable);

    @Modifying
    @Query("UPDATE Product p SET p.stock = p.stock + :amount WHERE p.id = :id AND (p.stock + :amount) >= 0")
    int adjustStockAtomically(@Param("id") Long id, @Param("amount") int amount);
}
