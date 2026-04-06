package com.parcezza.backend.domain;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import com.parcezza.backend.domain.enums.SellerStatus;

import jakarta.persistence.Column;

public class Seller {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userid;
    private String companyName;
    private String contactEmail;
    private String taxId;
    @Enumerated(EnumType.STRING)
    @Column(name="status", length=20, nullable=false)
    private SellerStatus status;

}
