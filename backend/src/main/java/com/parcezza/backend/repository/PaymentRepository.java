package com.parcezza.backend.repository;

import com.parcezza.backend.domain.Order;
import com.parcezza.backend.domain.Payment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByOrder(Order order);
}
