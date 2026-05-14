package com.parcezza.backend.service;

import com.parcezza.backend.domain.Role;
import com.parcezza.backend.domain.Seller;
import com.parcezza.backend.domain.User;
import com.parcezza.backend.domain.enums.SellerStatus;
import com.parcezza.backend.dto.seller.SellerRequest;
import com.parcezza.backend.dto.seller.SellerResponse;
import com.parcezza.backend.dto.seller.SellerStatusRequest;
import com.parcezza.backend.exception.BadRequestException;
import com.parcezza.backend.exception.ResourceNotFoundException;
import com.parcezza.backend.repository.RoleRepository;
import com.parcezza.backend.repository.SellerRepository;
import com.parcezza.backend.repository.UserRepository;
import com.parcezza.backend.security.CurrentUserService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SellerServiceImpl implements SellerService {

    private static final String SELLER_ROLE = "ROLE_SELLER";

    private final SellerRepository sellerRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public SellerServiceImpl(SellerRepository sellerRepository,
                             RoleRepository roleRepository,
                             UserRepository userRepository,
                             CurrentUserService currentUserService) {
        this.sellerRepository = sellerRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    @Transactional
    public SellerResponse register(SellerRequest request) {
        User user = currentUserService.getCurrentUser();
        if (sellerRepository.existsByOwner(user)) {
            throw new BadRequestException("Seller already exists");
        }

        Seller seller = new Seller();
        seller.setOwner(user);
        seller.setCompanyName(request.companyName());
        seller.setContactEmail(request.contactEmail());
        seller.setTaxId(request.taxId());
        seller.setLogoUrl(request.logoUrl());
        seller.setStatus(SellerStatus.PENDING);

        return toResponse(sellerRepository.save(seller));
    }

    @Override
    public SellerResponse getMySeller() {
        User user = currentUserService.getCurrentUser();
        Seller seller = sellerRepository.findFirstByOwner(user)
            .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
        return toResponse(seller);
    }

    @Override
    public List<SellerResponse> listAll() {
        return sellerRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SellerResponse updateStatus(Long sellerId, SellerStatusRequest request) {
        Seller seller = sellerRepository.findById(sellerId)
            .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

        seller.setStatus(request.status());

        if (request.status() == SellerStatus.APPROVED) {
            User owner = seller.getOwner();
            Role role = roleRepository.findByRoleName(SELLER_ROLE)
                .orElseGet(() -> roleRepository.save(new Role(SELLER_ROLE)));
            owner.addRole(role);
            userRepository.save(owner);
        }

        return toResponse(sellerRepository.save(seller));
    }

    private SellerResponse toResponse(Seller seller) {
        return new SellerResponse(
            seller.getId(),
            seller.getCompanyName(),
            seller.getContactEmail(),
            seller.getTaxId(),
            seller.getStatus(),
            seller.getLogoUrl()
        );
    }
}
