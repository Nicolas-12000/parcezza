package com.parcezza.backend;

import com.parcezza.backend.domain.Auditable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "audit_test")
@Getter
@Setter
public class AuditTestEntity extends Auditable {
    @Id @GeneratedValue
    private Long id;
    private String name;

}
