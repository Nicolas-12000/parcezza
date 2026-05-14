package com.parcezza.backend.repository;

import com.parcezza.backend.domain.Product;
import com.parcezza.backend.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    List<Product> findBySeller(Seller seller);
}
