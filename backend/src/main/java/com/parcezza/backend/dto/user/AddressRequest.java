package com.parcezza.backend.dto.user;

import jakarta.validation.constraints.Size;

public record AddressRequest(
    @Size(max = 255) String line1,
    @Size(max = 255) String line2,
    @Size(max = 50) String postalCode,
    @Size(max = 100) String administrativeArea,
    @Size(max = 50) String administrativeAreaCode,
    @Size(max = 100) String country,
    boolean primary
) {
}
