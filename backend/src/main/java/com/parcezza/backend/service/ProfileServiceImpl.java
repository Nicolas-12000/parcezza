package com.parcezza.backend.service;

import com.parcezza.backend.domain.Address;
import com.parcezza.backend.domain.Role;
import com.parcezza.backend.domain.User;
import com.parcezza.backend.dto.user.AddressRequest;
import com.parcezza.backend.dto.user.AddressResponse;
import com.parcezza.backend.dto.user.ProfileResponse;
import com.parcezza.backend.dto.user.ProfileUpdateRequest;
import com.parcezza.backend.exception.ResourceNotFoundException;
import com.parcezza.backend.repository.AddressRepository;
import com.parcezza.backend.repository.UserRepository;
import com.parcezza.backend.security.CurrentUserService;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public ProfileServiceImpl(CurrentUserService currentUserService,
                              UserRepository userRepository,
                              AddressRepository addressRepository) {
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public ProfileResponse getProfile() {
        return toProfileResponse(currentUserService.getCurrentUser());
    }

    @Override
    @Transactional
    public ProfileResponse updateProfile(ProfileUpdateRequest request) {
        User user = currentUserService.getCurrentUser();
        user.setFullName(request.fullName());
        return toProfileResponse(userRepository.save(user));
    }

    @Override
    public List<AddressResponse> listAddresses() {
        User user = currentUserService.getCurrentUser();
        return addressRepository.findByUser(user).stream()
            .map(this::toAddressResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressResponse addAddress(AddressRequest request) {
        User user = currentUserService.getCurrentUser();

        Address address = new Address();
        applyAddress(address, request, user);

        if (request.primary()) {
            clearPrimary(user);
        }

        return toAddressResponse(addressRepository.save(address));
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(Long addressId, AddressRequest request) {
        User user = currentUserService.getCurrentUser();
        Address address = addressRepository.findById(addressId)
            .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (!Objects.equals(address.getUser().getId(), user.getId())) {
            throw new ResourceNotFoundException("Address not found");
        }

        applyAddress(address, request, user);

        if (request.primary()) {
            clearPrimary(user);
            address.setPrimary(true);
        }

        return toAddressResponse(addressRepository.save(address));
    }

    @Override
    @Transactional
    public void deleteAddress(Long addressId) {
        User user = currentUserService.getCurrentUser();
        Address address = addressRepository.findById(addressId)
            .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        if (!Objects.equals(address.getUser().getId(), user.getId())) {
            throw new ResourceNotFoundException("Address not found");
        }

        addressRepository.delete(address);
    }

    private void applyAddress(Address address, AddressRequest request, User user) {
        address.setLine1(request.line1());
        address.setLine2(request.line2());
        address.setPostalCode(request.postalCode());
        address.setAdministrativeArea(request.administrativeArea());
        address.setAdministrativeAreaCode(request.administrativeAreaCode());
        address.setCountry(request.country());
        address.setPrimary(request.primary());
        address.setUser(user);
    }

    private void clearPrimary(User user) {
        List<Address> addresses = addressRepository.findByUser(user);
        for (Address address : addresses) {
            if (address.isPrimary()) {
                address.setPrimary(false);
                addressRepository.save(address);
            }
        }
    }

    private ProfileResponse toProfileResponse(User user) {
        Set<String> roles = user.getRoles().stream()
            .map(Role::getRoleName)
            .collect(Collectors.toSet());
        return new ProfileResponse(user.getId(), user.getEmail(), user.getFullName(), user.getEnabled(), roles);
    }

    private AddressResponse toAddressResponse(Address address) {
        return new AddressResponse(
            address.getId(),
            address.getLine1(),
            address.getLine2(),
            address.getPostalCode(),
            address.getAdministrativeArea(),
            address.getAdministrativeAreaCode(),
            address.getCountry(),
            address.isPrimary()
        );
    }
}
