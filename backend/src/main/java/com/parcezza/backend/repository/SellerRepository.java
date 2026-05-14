package com.parcezza.backend.repository;

import com.parcezza.backend.domain.Seller;
import com.parcezza.backend.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    List<Seller> findByOwner(User owner);
    Optional<Seller> findFirstByOwner(User owner);
    boolean existsByOwner(User owner);
}
