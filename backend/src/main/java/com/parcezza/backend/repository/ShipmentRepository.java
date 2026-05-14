package com.parcezza.backend.repository;

import com.parcezza.backend.domain.Order;
import com.parcezza.backend.domain.Shipment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Optional<Shipment> findByOrder(Order order);
}
