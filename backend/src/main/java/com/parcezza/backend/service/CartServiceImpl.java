package com.parcezza.backend.service;

import com.parcezza.backend.domain.Cart;
import com.parcezza.backend.domain.CartItem;
import com.parcezza.backend.domain.Product;
import com.parcezza.backend.domain.ProductVariant;
import com.parcezza.backend.dto.cart.CartItemRequest;
import com.parcezza.backend.dto.cart.CartItemResponse;
import com.parcezza.backend.dto.cart.CartResponse;
import com.parcezza.backend.exception.BadRequestException;
import com.parcezza.backend.exception.ResourceNotFoundException;
import com.parcezza.backend.repository.CartItemRepository;
import com.parcezza.backend.repository.CartRepository;
import com.parcezza.backend.repository.ProductRepository;
import com.parcezza.backend.repository.ProductVariantRepository;
import com.parcezza.backend.security.CurrentUserService;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final CurrentUserService currentUserService;
    private final InventoryService inventoryService;

    private static final Duration RESERVATION_TTL = Duration.ofMinutes(30);

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           ProductRepository productRepository,
                           ProductVariantRepository variantRepository,
                           CurrentUserService currentUserService,
                           InventoryService inventoryService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.currentUserService = currentUserService;
        this.inventoryService = inventoryService;
    }

    @Override
    @Transactional
    public CartResponse getMyCart() {
        Cart cart = getOrCreateCart();
        expireReservations(cart);
        return toResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addItem(CartItemRequest request) {
        Cart cart = getOrCreateCart();
        expireReservations(cart);
        Product product = productRepository.findById(request.productId())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        if (!product.isActive()) {
            throw new BadRequestException("Product is not active");
        }

        ProductVariant variant = resolveVariant(request.variantId(), product);
        BigDecimal unitPrice = resolveUnitPrice(product, variant);
        String currency = product.getCurrency();

        inventoryService.reserve(product, variant, request.quantity(), "CART", cart.getId());

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setVariant(variant);
        item.setQuantity(request.quantity());
        item.setUnitPrice(unitPrice);
        item.setCurrency(currency);
        item.setLineTotal(unitPrice.multiply(BigDecimal.valueOf(request.quantity())));
        item.setReservedUntil(Instant.now().plus(RESERVATION_TTL));

        cartItemRepository.save(item);

        return toResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse updateItem(Long itemId, CartItemRequest request) {
        Cart cart = getOrCreateCart();
        expireReservations(cart);
        CartItem item = cartItemRepository.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!Objects.equals(item.getCart().getId(), cart.getId())) {
            throw new ResourceNotFoundException("Cart item not found");
        }

        Product product = productRepository.findById(request.productId())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        if (!product.isActive()) {
            throw new BadRequestException("Product is not active");
        }
        ProductVariant variant = resolveVariant(request.variantId(), product);
        BigDecimal unitPrice = resolveUnitPrice(product, variant);

        inventoryService.release(item.getProduct(), item.getVariant(), item.getQuantity(), "CART", cart.getId());
        inventoryService.reserve(product, variant, request.quantity(), "CART", cart.getId());

        item.setProduct(product);
        item.setVariant(variant);
        item.setQuantity(request.quantity());
        item.setUnitPrice(unitPrice);
        item.setCurrency(product.getCurrency());
        item.setLineTotal(unitPrice.multiply(BigDecimal.valueOf(request.quantity())));
        item.setReservedUntil(Instant.now().plus(RESERVATION_TTL));

        cartItemRepository.save(item);

        return toResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse removeItem(Long itemId) {
        Cart cart = getOrCreateCart();
        expireReservations(cart);
        CartItem item = cartItemRepository.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!Objects.equals(item.getCart().getId(), cart.getId())) {
            throw new ResourceNotFoundException("Cart item not found");
        }

        inventoryService.release(item.getProduct(), item.getVariant(), item.getQuantity(), "CART", cart.getId());
        cartItemRepository.delete(item);
        return toResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse clear() {
        Cart cart = getOrCreateCart();
        expireReservations(cart);
        List<CartItem> items = cartItemRepository.findByCart(cart);
        for (CartItem item : items) {
            inventoryService.release(item.getProduct(), item.getVariant(), item.getQuantity(), "CART", cart.getId());
        }
        cart.getItems().clear();
        cartRepository.save(cart);
        return toResponse(cart);
    }

    private Cart getOrCreateCart() {
        return cartRepository.findByUser(currentUserService.getCurrentUser())
            .orElseGet(() -> {
                Cart cart = new Cart();
                cart.setUser(currentUserService.getCurrentUser());
                return cartRepository.save(cart);
            });
    }

    private ProductVariant resolveVariant(Long variantId, Product product) {
        if (variantId == null) {
            return null;
        }
        ProductVariant variant = variantRepository.findById(variantId)
            .orElseThrow(() -> new ResourceNotFoundException("Variant not found"));

        if (!Objects.equals(variant.getProduct().getId(), product.getId())) {
            throw new BadRequestException("Variant does not belong to product");
        }

        return variant;
    }

    private BigDecimal resolveUnitPrice(Product product, ProductVariant variant) {
        BigDecimal unitPrice = variant != null && variant.getPriceOverride() != null
            ? variant.getPriceOverride()
            : product.getBasePrice();
        if (unitPrice == null) {
            throw new BadRequestException("Product price not set");
        }
        return unitPrice;
    }

    private void expireReservations(Cart cart) {
        Instant now = Instant.now();
        List<CartItem> items = cartItemRepository.findByCart(cart);
        for (CartItem item : items) {
            if (item.getReservedUntil() != null && item.getReservedUntil().isBefore(now)) {
                inventoryService.release(item.getProduct(), item.getVariant(), item.getQuantity(), "CART", cart.getId());
                cartItemRepository.delete(item);
            }
        }
    }

    private CartResponse toResponse(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCart(cart);
        List<CartItemResponse> itemResponses = items.stream()
            .map(this::toItemResponse)
            .collect(Collectors.toList());

        BigDecimal total = items.stream()
            .map(CartItem::getLineTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        String currency = items.isEmpty() ? null : items.get(0).getCurrency();

        return new CartResponse(cart.getId(), itemResponses, total, currency);
    }

    private CartItemResponse toItemResponse(CartItem item) {
        Long variantId = item.getVariant() == null ? null : item.getVariant().getId();
        return new CartItemResponse(
            item.getId(),
            item.getProduct().getId(),
            variantId,
            item.getQuantity(),
            item.getUnitPrice(),
            item.getCurrency(),
            item.getLineTotal()
        );
    }
}
