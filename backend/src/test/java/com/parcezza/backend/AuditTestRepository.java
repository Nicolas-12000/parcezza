package com.parcezza.backend;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditTestRepository extends JpaRepository<AuditTestEntity, Long> {
}
