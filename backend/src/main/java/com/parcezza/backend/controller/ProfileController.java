package com.parcezza.backend.controller;

import com.parcezza.backend.dto.user.AddressRequest;
import com.parcezza.backend.dto.user.AddressResponse;
import com.parcezza.backend.dto.user.ProfileResponse;
import com.parcezza.backend.dto.user.ProfileUpdateRequest;
import com.parcezza.backend.service.ProfileService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile() {
        return ResponseEntity.ok(profileService.getProfile());
    }

    @PutMapping
    public ResponseEntity<ProfileResponse> updateProfile(@Valid @RequestBody ProfileUpdateRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(request));
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressResponse>> listAddresses() {
        return ResponseEntity.ok(profileService.listAddresses());
    }

    @PostMapping("/addresses")
    public ResponseEntity<AddressResponse> addAddress(@Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(profileService.addAddress(request));
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable Long addressId,
                                                         @Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(profileService.updateAddress(addressId, request));
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        profileService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }
}
