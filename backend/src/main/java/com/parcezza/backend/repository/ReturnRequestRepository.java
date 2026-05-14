package com.parcezza.backend.repository;

import com.parcezza.backend.domain.Order;
import com.parcezza.backend.domain.ReturnRequest;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, Long> {
    Optional<ReturnRequest> findByOrder(Order order);
}
