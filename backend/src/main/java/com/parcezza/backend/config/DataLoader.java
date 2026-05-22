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

            Product pizzaClasica = new Product();
            pizzaClasica.setSku("TV-55-4K");
            pizzaClasica.setName("TV Smart 55\" 4K");
            pizzaClasica.setDescription("Televisor Smart 55 con resolución 4K, HDR y SmartOS.");
            pizzaClasica.setBasePrice(new BigDecimal("549.99"));
            pizzaClasica.setCurrency("USD");
            pizzaClasica.setStock(25);
            pizzaClasica.setSeller(seller);
            productRepository.save(pizzaClasica);

            ProductVariant clasicaSmall = new ProductVariant();
            clasicaSmall.setProduct(pizzaClasica);
            clasicaSmall.setSku("TV-43-4K");
            clasicaSmall.setPriceOverride(new BigDecimal("449.99"));
            clasicaSmall.setStock(15);
            VariantAttribute clasicaSmallSize = new VariantAttribute();
            clasicaSmallSize.setVariant(clasicaSmall);
            clasicaSmallSize.setName("ScreenSize");
            clasicaSmallSize.setValue("43\"");
            clasicaSmall.getAttributes().add(clasicaSmallSize);
            productVariantRepository.save(clasicaSmall);

            ProductVariant clasicaLarge = new ProductVariant();
            clasicaLarge.setProduct(pizzaClasica);
            clasicaLarge.setSku("TV-55-4K-L");
            clasicaLarge.setPriceOverride(new BigDecimal("599.99"));
            clasicaLarge.setStock(10);
            VariantAttribute clasicaLargeSize = new VariantAttribute();
            clasicaLargeSize.setVariant(clasicaLarge);
            clasicaLargeSize.setName("ScreenSize");
            clasicaLargeSize.setValue("55\"");
            clasicaLarge.getAttributes().add(clasicaLargeSize);
            productVariantRepository.save(clasicaLarge);

            Product pizzaPepperoni = new Product();
            pizzaPepperoni.setSku("PH-ALPHA-128");
            pizzaPepperoni.setName("Phone Alpha 128GB");
            pizzaPepperoni.setDescription("Smartphone Alpha con 128GB, cámara dual y batería de larga duración.");
            pizzaPepperoni.setBasePrice(new BigDecimal("699.99"));
            pizzaPepperoni.setCurrency("USD");
            pizzaPepperoni.setStock(120);
            pizzaPepperoni.setSeller(seller);
            productRepository.save(pizzaPepperoni);

            Product bebidaCola = new Product();
            bebidaCola.setSku("WH-1000");
            bebidaCola.setName("Headphones Wireless X");
            bebidaCola.setDescription("Auriculares inalámbricos con cancelación de ruido y 30h de batería.");
            bebidaCola.setBasePrice(new BigDecimal("199.99"));
            bebidaCola.setCurrency("USD");
            bebidaCola.setStock(250);
            bebidaCola.setSeller(seller);
            productRepository.save(bebidaCola);

            Catalog catalogPizzas = new Catalog();
            catalogPizzas.setName("Electrónicos populares");
            catalogPizzas.setSlug("electronicos-populares");
            catalogPizzas.getProducts().add(pizzaClasica);
            catalogPizzas.getProducts().add(pizzaPepperoni);
            catalogRepository.save(catalogPizzas);

            Catalog catalogBebidas = new Catalog();
            catalogBebidas.setName("Accesorios");
            catalogBebidas.setSlug("accesorios");
            catalogBebidas.getProducts().add(bebidaCola);
            catalogRepository.save(catalogBebidas);

            Cart camilaCart = new Cart();
            camilaCart.setUser(camila);
            cartRepository.save(camilaCart);

            CartItem camilaItem = new CartItem();
            camilaItem.setCart(camilaCart);
            camilaItem.setProduct(pizzaPepperoni);
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
            juanItem.setProduct(bebidaCola);
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

            OrderItem camilaPizza = new OrderItem();
            camilaPizza.setOrder(orderCamila);
            camilaPizza.setProduct(pizzaClasica);
            camilaPizza.setVariant(clasicaLarge);
            camilaPizza.setQuantity(1);
            camilaPizza.setUnitPrice(new BigDecimal("599.99"));
            camilaPizza.setCurrency("USD");
            camilaPizza.setLineTotal(new BigDecimal("599.99"));
            orderItemRepository.save(camilaPizza);

            OrderItem camilaSoda = new OrderItem();
            camilaSoda.setOrder(orderCamila);
            camilaSoda.setProduct(bebidaCola);
            camilaSoda.setQuantity(2);
            camilaSoda.setUnitPrice(new BigDecimal("199.99"));
            camilaSoda.setCurrency("USD");
            camilaSoda.setLineTotal(new BigDecimal("399.98"));
            orderItemRepository.save(camilaSoda);

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

            OrderItem juanPizza = new OrderItem();
            juanPizza.setOrder(orderJuan);
            juanPizza.setProduct(pizzaPepperoni);
            juanPizza.setQuantity(2);
            juanPizza.setUnitPrice(new BigDecimal("699.99"));
            juanPizza.setCurrency("USD");
            juanPizza.setLineTotal(new BigDecimal("1399.98"));
            orderItemRepository.save(juanPizza);

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

            InventoryMovement invCamila = new InventoryMovement();
            invCamila.setProduct(pizzaClasica);
            invCamila.setVariant(clasicaLarge);
            invCamila.setType(InventoryMovementType.SALE);
            invCamila.setQuantity(1);
            invCamila.setReferenceType("ORDER");
            invCamila.setReferenceId(orderCamila.getId());
            inventoryMovementRepository.save(invCamila);

            InventoryMovement invJuan = new InventoryMovement();
            invJuan.setProduct(pizzaPepperoni);
            invJuan.setType(InventoryMovementType.SALE);
            invJuan.setQuantity(2);
            invJuan.setReferenceType("ORDER");
            invJuan.setReferenceId(orderJuan.getId());
            inventoryMovementRepository.save(invJuan);

            ReturnRequest camilaReturn = new ReturnRequest();
            camilaReturn.setOrder(orderCamila);
            camilaReturn.setStatus(ReturnStatus.REQUESTED);
            camilaReturn.setReason("Producto llego tibio y la caja maltratada");
            camilaReturn.setNote("Cliente solicita reembolso parcial");
            returnRequestRepository.save(camilaReturn);
        };
    }
}
