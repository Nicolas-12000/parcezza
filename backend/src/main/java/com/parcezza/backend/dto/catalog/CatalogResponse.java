package com.parcezza.backend.dto.catalog;

import java.util.List;

public record CatalogResponse(Long id, String name, String slug, List<Long> productIds) {
}
