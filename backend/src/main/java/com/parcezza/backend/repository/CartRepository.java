package com.parcezza.backend.repository;

import com.parcezza.backend.domain.Cart;
import com.parcezza.backend.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
