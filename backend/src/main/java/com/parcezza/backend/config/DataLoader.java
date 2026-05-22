package com.parcezza.backend.config;

import com.parcezza.backend.domain.Address;
import com.parcezza.backend.domain.Cart;
import com.parcezza.backend.domain.CartItem;
import com.parcezza.backend.domain.Catalog;
import com.parcezza.backend.domain.InventoryMovement;
import com.parcezza.backend.domain.Order;
import com.parcezza.backend.domain.OrderItem;
import com.parcezza.backend.domain.Payment;
import com.parcezza.backend.domain.Product;
import com.parcezza.backend.domain.ProductVariant;
import com.parcezza.backend.domain.ReturnRequest;
import com.parcezza.backend.domain.Role;
import com.parcezza.backend.domain.Seller;
import com.parcezza.backend.domain.Shipment;
import com.parcezza.backend.domain.User;
import com.parcezza.backend.domain.VariantAttribute;
import com.parcezza.backend.domain.enums.InventoryMovementType;
import com.parcezza.backend.domain.enums.OrderStatus;
import com.parcezza.backend.domain.enums.PaymentStatus;
import com.parcezza.backend.domain.enums.ReturnStatus;
import com.parcezza.backend.domain.enums.SellerStatus;
import com.parcezza.backend.domain.enums.ShipmentStatus;
import com.parcezza.backend.repository.AddressRepository;
import com.parcezza.backend.repository.CartItemRepository;
import com.parcezza.backend.repository.CartRepository;
import com.parcezza.backend.repository.CatalogRepository;
import com.parcezza.backend.repository.InventoryMovementRepository;
import com.parcezza.backend.repository.OrderItemRepository;
import com.parcezza.backend.repository.OrderRepository;
import com.parcezza.backend.repository.PaymentRepository;
import com.parcezza.backend.repository.ProductRepository;
import com.parcezza.backend.repository.ProductVariantRepository;
import com.parcezza.backend.repository.ReturnRequestRepository;
import com.parcezza.backend.repository.RoleRepository;
import com.parcezza.backend.repository.SellerRepository;
import com.parcezza.backend.repository.ShipmentRepository;
import com.parcezza.backend.repository.UserRepository;
import com.parcezza.backend.repository.VariantAttributeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.Instant;

@Configuration
@Profile("dev")
public class DataLoader {

    @Bean
    CommandLineRunner init(RoleRepository roleRepository,
                           UserRepository userRepository,
                           AddressRepository addressRepository,
                           SellerRepository sellerRepository,
                           ProductRepository productRepository,
                           ProductVariantRepository productVariantRepository,
                           VariantAttributeRepository variantAttributeRepository,
                           CatalogRepository catalogRepository,
                           CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           OrderRepository orderRepository,
                           OrderItemRepository orderItemRepository,
                           PaymentRepository paymentRepository,
                           ShipmentRepository shipmentRepository,
                           InventoryMovementRepository inventoryMovementRepository,
                           ReturnRequestRepository returnRequestRepository,
                           PasswordEncoder encoder) {
        return args -> {
            Role roleUser = roleRepository.findByRoleName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));
            Role roleAdmin = roleRepository.findByRoleName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
            Role roleSeller = roleRepository.findByRoleName("ROLE_SELLER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_SELLER")));

            if (userRepository.findByEmail("admin@parcezza.com").isPresent()) {
                return;
            }

            // Admin user
            // Email: admin@parcezza.com | Password: admin12345 | Name: Admin Parcezza
            User admin = new User();
            admin.setEmail("admin@parcezza.com");
            admin.setFullName("Admin Parcezza");
            admin.setPasswordHash(encoder.encode("admin12345"));
            admin.setEnabled(true);
            admin.addRole(roleAdmin);
            admin.addRole(roleUser);
            userRepository.save(admin);

            // Customer user
            // Email: camila.perez@example.com | Password: camila123 | Name: Camila Perez
            User camila = new User();
            camila.setEmail("camila.perez@example.com");
            camila.setFullName("Camila Perez");
            camila.setPasswordHash(encoder.encode("camila123"));
            camila.setEnabled(true);
            camila.addRole(roleUser);

            Address camilaHome = new Address();
            camilaHome.setLine1("Calle 10 # 42-15");
            camilaHome.setLine2("Apto 301");
            camilaHome.setPostalCode("050021");
            camilaHome.setAdministrativeArea("Medellin");
            camilaHome.setAdministrativeAreaCode("ANT");
            camilaHome.setCountry("CO");
            camilaHome.setPrimary(true);
            camila.addAddress(camilaHome);
            userRepository.save(camila);

            // Customer user
            // Email: juan.gomez@example.com | Password: juan123 | Name: Juan Gomez
            User juan = new User();
            juan.setEmail("juan.gomez@example.com");
            juan.setFullName("Juan Gomez");
            juan.setPasswordHash(encoder.encode("juan123"));
            juan.setEnabled(true);
            juan.addRole(roleUser);

            Address juanHome = new Address();
            juanHome.setLine1("Carrera 25 # 8-19");
            juanHome.setPostalCode("110111");
            juanHome.setAdministrativeArea("Bogota");
            juanHome.setAdministrativeAreaCode("DC");
            juanHome.setCountry("CO");
            juanHome.setPrimary(true);
            juan.addAddress(juanHome);
            userRepository.save(juan);

            // Seller user
            // Email: luisa.seller@example.com | Password: seller123 | Name: Luisa Herrera
            User luisa = new User();
            luisa.setEmail("luisa.seller@example.com");
            luisa.setFullName("Luisa Herrera");
            luisa.setPasswordHash(encoder.encode("seller123"));
            luisa.setEnabled(true);
            luisa.addRole(roleUser);
            luisa.addRole(roleSeller);
            userRepository.save(luisa);

            Seller seller = new Seller();
            seller.setOwner(luisa);
            seller.setCompanyName("ACME Electronics");
            seller.setContactEmail("sales@acme-electronics.com");
            seller.setTaxId("NIT-900111222-3");
            seller.setStatus(SellerStatus.APPROVED);
            seller.setLogoUrl("https://images.parcezza.local/brands/acme-electronics.png");
            sellerRepository.save(seller);

            Product tv55 = new Product();
            tv55.setSku("TV-55-4K");
            tv55.setName("TV Smart 55\" 4K");
            tv55.setDescription("Televisor Smart 55 con resolución 4K, HDR y SmartOS.");
            tv55.setBasePrice(new BigDecimal("549.99"));
            tv55.setCurrency("USD");
            tv55.setStock(25);
            tv55.setSeller(seller);
            productRepository.save(tv55);

            ProductVariant tv43Variant = new ProductVariant();
            tv43Variant.setProduct(tv55);
            tv43Variant.setSku("TV-43-4K");
            tv43Variant.setPriceOverride(new BigDecimal("449.99"));
            tv43Variant.setStock(15);
            VariantAttribute tv43Attr = new VariantAttribute();
            tv43Attr.setVariant(tv43Variant);
            tv43Attr.setName("ScreenSize");
            tv43Attr.setValue("43\"");
            tv43Variant.getAttributes().add(tv43Attr);
            productVariantRepository.save(tv43Variant);

            ProductVariant tv55Variant = new ProductVariant();
            tv55Variant.setProduct(tv55);
            tv55Variant.setSku("TV-55-4K-L");
            tv55Variant.setPriceOverride(new BigDecimal("599.99"));
            tv55Variant.setStock(10);
            VariantAttribute tv55Attr = new VariantAttribute();
            tv55Attr.setVariant(tv55Variant);
            tv55Attr.setName("ScreenSize");
            tv55Attr.setValue("55\"");
            tv55Variant.getAttributes().add(tv55Attr);
            productVariantRepository.save(tv55Variant);

            Product phoneAlpha = new Product();
            phoneAlpha.setSku("PH-ALPHA-128");
            phoneAlpha.setName("Phone Alpha 128GB");
            phoneAlpha.setDescription("Smartphone Alpha con 128GB, cámara dual y batería de larga duración.");
            phoneAlpha.setBasePrice(new BigDecimal("699.99"));
            phoneAlpha.setCurrency("USD");
            phoneAlpha.setStock(120);
            phoneAlpha.setSeller(seller);
            productRepository.save(phoneAlpha);

            Product headphonesX = new Product();
            headphonesX.setSku("WH-1000");
            headphonesX.setName("Headphones Wireless X");
            headphonesX.setDescription("Auriculares inalámbricos con cancelación de ruido y 30h de batería.");
            headphonesX.setBasePrice(new BigDecimal("199.99"));
            headphonesX.setCurrency("USD");
            headphonesX.setStock(250);
            headphonesX.setSeller(seller);
            productRepository.save(headphonesX);

            Catalog catalogElectronics = new Catalog();
            catalogElectronics.setName("Electrónicos populares");
            catalogElectronics.setSlug("electronicos-populares");
            catalogElectronics.getProducts().add(tv55);
            catalogElectronics.getProducts().add(phoneAlpha);
            catalogRepository.save(catalogElectronics);

            Catalog catalogAccessories = new Catalog();
            catalogAccessories.setName("Accesorios");
            catalogAccessories.setSlug("accesorios");
            catalogAccessories.getProducts().add(headphonesX);
            catalogRepository.save(catalogAccessories);

            Cart camilaCart = new Cart();
            camilaCart.setUser(camila);
            cartRepository.save(camilaCart);

            CartItem camilaItem = new CartItem();
            camilaItem.setCart(camilaCart);
            camilaItem.setProduct(phoneAlpha);
            camilaItem.setQuantity(1);
            camilaItem.setUnitPrice(new BigDecimal("699.99"));
            camilaItem.setCurrency("USD");
            camilaItem.setLineTotal(new BigDecimal("699.99"));
            camilaItem.setReservedUntil(Instant.now().plusSeconds(3600));
            cartItemRepository.save(camilaItem);

            Cart juanCart = new Cart();
            juanCart.setUser(juan);
            cartRepository.save(juanCart);

            CartItem juanItem = new CartItem();
            juanItem.setCart(juanCart);
            juanItem.setProduct(headphonesX);
            juanItem.setQuantity(2);
            juanItem.setUnitPrice(new BigDecimal("199.99"));
            juanItem.setCurrency("USD");
            juanItem.setLineTotal(new BigDecimal("399.98"));
            cartItemRepository.save(juanItem);

            Order orderCamila = new Order();
            orderCamila.setUser(camila);
            orderCamila.setShippingAddress(camilaHome);
            orderCamila.setStatus(OrderStatus.DELIVERED);
            orderCamila.setCurrency("USD");
            orderCamila.setTotalAmount(new BigDecimal("949.97"));
            orderRepository.save(orderCamila);

            OrderItem camilaTv = new OrderItem();
            camilaTv.setOrder(orderCamila);
            camilaTv.setProduct(tv55);
            camilaTv.setVariant(tv55Variant);
            camilaTv.setQuantity(1);
            camilaTv.setUnitPrice(new BigDecimal("599.99"));
            camilaTv.setCurrency("USD");
            camilaTv.setLineTotal(new BigDecimal("599.99"));
            orderItemRepository.save(camilaTv);

            OrderItem camilaHeadphones = new OrderItem();
            camilaHeadphones.setOrder(orderCamila);
            camilaHeadphones.setProduct(headphonesX);
            camilaHeadphones.setQuantity(2);
            camilaHeadphones.setUnitPrice(new BigDecimal("199.99"));
            camilaHeadphones.setCurrency("USD");
            camilaHeadphones.setLineTotal(new BigDecimal("399.98"));
            orderItemRepository.save(camilaHeadphones);

            Payment camilaPayment = new Payment();
            camilaPayment.setOrder(orderCamila);
            camilaPayment.setStatus(PaymentStatus.SUCCESS);
            camilaPayment.setProvider("stripe");
            camilaPayment.setProviderRef("pi_3NQ1PLv8C6");
            camilaPayment.setCardLast4("4242");
            camilaPayment.setAmount(new BigDecimal("949.97"));
            camilaPayment.setCurrency("USD");
            paymentRepository.save(camilaPayment);

            Shipment camilaShipment = new Shipment();
            camilaShipment.setOrder(orderCamila);
            camilaShipment.setStatus(ShipmentStatus.DELIVERED);
            camilaShipment.setTrackingCode("PKG-CO-102938");
            shipmentRepository.save(camilaShipment);

            Order orderJuan = new Order();
            orderJuan.setUser(juan);
            orderJuan.setShippingAddress(juanHome);
            orderJuan.setStatus(OrderStatus.PROCESSING);
            orderJuan.setCurrency("USD");
            orderJuan.setTotalAmount(new BigDecimal("1399.98"));
            orderRepository.save(orderJuan);

            OrderItem juanPhones = new OrderItem();
            juanPhones.setOrder(orderJuan);
            juanPhones.setProduct(phoneAlpha);
            juanPhones.setQuantity(2);
            juanPhones.setUnitPrice(new BigDecimal("699.99"));
            juanPhones.setCurrency("USD");
            juanPhones.setLineTotal(new BigDecimal("1399.98"));
            orderItemRepository.save(juanPhones);

            Payment juanPayment = new Payment();
            juanPayment.setOrder(orderJuan);
            juanPayment.setStatus(PaymentStatus.PENDING);
            juanPayment.setProvider("mercado_pago");
            juanPayment.setProviderRef("mp_984203");
            juanPayment.setAmount(new BigDecimal("1399.98"));
            juanPayment.setCurrency("USD");
            paymentRepository.save(juanPayment);

            Shipment juanShipment = new Shipment();
            juanShipment.setOrder(orderJuan);
            juanShipment.setStatus(ShipmentStatus.PENDING);
            juanShipment.setTrackingCode("PKG-CO-549201");
            shipmentRepository.save(juanShipment);

            InventoryMovement invTvSale = new InventoryMovement();
            invTvSale.setProduct(tv55);
            invTvSale.setVariant(tv55Variant);
            invTvSale.setType(InventoryMovementType.SALE);
            invTvSale.setQuantity(1);
            invTvSale.setReferenceType("ORDER");
            invTvSale.setReferenceId(orderCamila.getId());
            inventoryMovementRepository.save(invTvSale);

            InventoryMovement invPhoneSale = new InventoryMovement();
            invPhoneSale.setProduct(phoneAlpha);
            invPhoneSale.setType(InventoryMovementType.SALE);
            invPhoneSale.setQuantity(2);
            invPhoneSale.setReferenceType("ORDER");
            invPhoneSale.setReferenceId(orderJuan.getId());
            inventoryMovementRepository.save(invPhoneSale);

            ReturnRequest camilaReturn = new ReturnRequest();
            camilaReturn.setOrder(orderCamila);
            camilaReturn.setStatus(ReturnStatus.REQUESTED);
            camilaReturn.setReason("Producto llegó con caja dañada y embalaje deficiente");
            camilaReturn.setNote("Cliente solicita reembolso parcial");
            returnRequestRepository.save(camilaReturn);
        };
    }
}
