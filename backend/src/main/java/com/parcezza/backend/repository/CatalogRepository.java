package com.parcezza.backend.repository;

import com.parcezza.backend.domain.Catalog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {
	Optional<Catalog> findBySlug(String slug);
}
