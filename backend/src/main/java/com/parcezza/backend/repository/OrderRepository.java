package com.parcezza.backend.repository;

import com.parcezza.backend.domain.Order;
import com.parcezza.backend.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
