package com.parcezza.backend.dto.seller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SellerRequest(
    @NotBlank @Size(max = 255) String companyName,
    @Email @Size(max = 255) String contactEmail,
    @Size(max = 100) String taxId,
    @Size(max = 500) String logoUrl
) {
}
