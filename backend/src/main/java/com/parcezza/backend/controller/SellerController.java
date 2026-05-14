package com.parcezza.backend.controller;

import com.parcezza.backend.dto.seller.SellerRequest;
import com.parcezza.backend.dto.seller.SellerResponse;
import com.parcezza.backend.dto.seller.SellerStatusRequest;
import com.parcezza.backend.service.SellerService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @PostMapping
    public ResponseEntity<SellerResponse> register(@Valid @RequestBody SellerRequest request) {
        return ResponseEntity.ok(sellerService.register(request));
    }

    @GetMapping("/me")
    public ResponseEntity<SellerResponse> mySeller() {
        return ResponseEntity.ok(sellerService.getMySeller());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SellerResponse>> list() {
        return ResponseEntity.ok(sellerService.listAll());
    }

    @PatchMapping("/{sellerId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SellerResponse> updateStatus(@PathVariable Long sellerId,
                                                       @Valid @RequestBody SellerStatusRequest request) {
        return ResponseEntity.ok(sellerService.updateStatus(sellerId, request));
    }
}
