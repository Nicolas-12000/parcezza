package com.parcezza.backend.repository;

import com.parcezza.backend.domain.Order;
import com.parcezza.backend.domain.OrderItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
}
