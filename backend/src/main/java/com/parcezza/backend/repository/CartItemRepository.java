package com.parcezza.backend.repository;

import com.parcezza.backend.domain.Cart;
import com.parcezza.backend.domain.CartItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart cart);
}
