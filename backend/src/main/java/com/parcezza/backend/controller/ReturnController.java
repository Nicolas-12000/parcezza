package com.parcezza.backend.controller;

import com.parcezza.backend.dto.returnrequest.ReturnRequestCreate;
import com.parcezza.backend.dto.returnrequest.ReturnResponse;
import com.parcezza.backend.dto.returnrequest.ReturnStatusUpdateRequest;
import com.parcezza.backend.service.ReturnService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/returns")
public class ReturnController {

    private final ReturnService returnService;

    public ReturnController(ReturnService returnService) {
        this.returnService = returnService;
    }

    @PostMapping("/orders/{orderId}")
    public ResponseEntity<ReturnResponse> request(@PathVariable Long orderId,
                                                  @Valid @RequestBody ReturnRequestCreate request) {
        return ResponseEntity.ok(returnService.requestReturn(orderId, request));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ReturnResponse> getByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(returnService.getByOrder(orderId));
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<java.util.List<ReturnResponse>> listAll() {
        return ResponseEntity.ok(returnService.listAll());
    }

    @PatchMapping("/{returnId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReturnResponse> updateStatus(@PathVariable Long returnId,
                                                       @Valid @RequestBody ReturnStatusUpdateRequest request) {
        return ResponseEntity.ok(returnService.updateStatus(returnId, request));
    }
}
