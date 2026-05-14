package com.parcezza.backend.dto.user;

public record AddressResponse(
    Long id,
    String line1,
    String line2,
    String postalCode,
    String administrativeArea,
    String administrativeAreaCode,
    String country,
    boolean primary
) {
}
