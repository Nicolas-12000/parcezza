package com.parcezza.backend.service;

import com.parcezza.backend.dto.seller.SellerRequest;
import com.parcezza.backend.dto.seller.SellerResponse;
import com.parcezza.backend.dto.seller.SellerStatusRequest;
import java.util.List;

public interface SellerService {
    SellerResponse register(SellerRequest request);
    SellerResponse getMySeller();
    List<SellerResponse> listAll();
    SellerResponse updateStatus(Long sellerId, SellerStatusRequest request);
}
