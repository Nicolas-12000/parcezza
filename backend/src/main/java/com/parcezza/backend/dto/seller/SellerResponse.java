package com.parcezza.backend.dto.seller;

import com.parcezza.backend.domain.enums.SellerStatus;

public record SellerResponse(
    Long id,
    String companyName,
    String contactEmail,
    String taxId,
    SellerStatus status,
    String logoUrl
) {
}
