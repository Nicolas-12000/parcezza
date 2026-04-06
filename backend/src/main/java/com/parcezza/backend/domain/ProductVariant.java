package com.parcezza.backend.domain;
import java.math.BigDecimal;
import java.util.Map;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class ProductVariant {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private Long productId;
    private String sku;
    private Map<String, String> attributes;
    private BigDecimal priceOverride;
    private Integer stock;
    
}
