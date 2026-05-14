package com.parcezza.backend.service;

import com.parcezza.backend.dto.user.AddressRequest;
import com.parcezza.backend.dto.user.AddressResponse;
import com.parcezza.backend.dto.user.ProfileResponse;
import com.parcezza.backend.dto.user.ProfileUpdateRequest;
import java.util.List;

public interface ProfileService {
    ProfileResponse getProfile();
    ProfileResponse updateProfile(ProfileUpdateRequest request);
    List<AddressResponse> listAddresses();
    AddressResponse addAddress(AddressRequest request);
    AddressResponse updateAddress(Long addressId, AddressRequest request);
    void deleteAddress(Long addressId);
}
