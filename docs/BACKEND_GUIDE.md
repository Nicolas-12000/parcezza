# Backend Guide — E‑commerce Parcezza

Guía práctica y orientada a la implementación del backend con Spring Boot. Incluye arquitectura recomendada, modelos, DTOs, controllers, servicios, seguridad (JWT), tests, CI/CD y despliegue. Variables y nombres en inglés.

---

## 1. Objetivo

Crear un backend robusto, mantenible y testeable para un e‑commerce. Enfócate en separar responsabilidades, mantener DTOs claros y escribir tests para la lógica crítica (checkout, stock decrement).

## 2. Stack recomendado

- Java 17+ / 21
- Spring Boot 3.x
- Spring Security
- Spring Data JPA (PostgreSQL)
- Flyway (migrations)
- Redis (cache/session/locks)
- Stripe (payments)
- Docker / Docker Compose para entorno local
- JUnit 5, Mockito, Spring Test, Testcontainers (para integration tests)

## 3. Estructura de paquetes (sugerida)

- `com.parcezza.backend` (root)
  - `config` (security, beans)
  - `controller` (REST controllers)
  - `service` (business logic)
  - `repository` (Spring Data interfaces)
  - `domain` (JPA entities)
  - `dto` (request/response DTOs)
  - `mapper` (mapstruct or manual)
  - `exception` (custom exceptions, handlers)
  - `integration` (clients for Stripe, email, storage)

## 4. Principios de diseño

- Single Responsibility (SRP): cada clase una responsabilidad.
- Dependency Injection: inyectar interfaces para facilitar testing.
- Favor small services: un service por agregado lógico (ProductService, OrderService, PaymentService).
- Transactional boundaries: anotar servicios que modifican datos con `@Transactional`.
- Use DTOs to decouple API contract from persistence model.

## 5. Domain models (entities) — ejemplos

`Product` (simplified)

```java
package com.parcezza.backend.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable=false, unique=true)
  private String sku;
  private Long vendorId; // nullable - null = platform
  private String name;
  @Column(columnDefinition = "TEXT")
  private String description;
  private BigDecimal price;
  private Integer stock;
  private String currency;
  private String status; // DRAFT, PUBLISHED, REMOVED
  // constructors, getters, setters
}
```

`User` (simplified)

```java
@Entity
@Table(name = "users")
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String email;
  private String passwordHash;
  private String fullName;
  private String roles; // CSV or separate Role entity
}
```

`Order` (skeleton)

```java
@Entity
@Table(name = "orders")
public class Order {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long userId;
  private BigDecimal total;
  private String status; // PENDING, PAID, CANCELLED, SHIPPED
  private String shippingAddressJson; // or normalized Address entity
  private String itemsJson; // or OneToMany OrderItem
}
```

Nota: para producción usar objetos normalizados (OrderItem entity), aquí simplificamos para el ejemplo.

## 6. DTOs y mappers

- Define DTOs en `dto` package. Ejemplo `ProductDto` y `CreateProductDto`.
- Usa MapStruct o mappers manuales para convertir entre entity y DTO.

`ProductDto` example:

```java
public record ProductDto(Long id, Long vendorId, String sku, String name, String description, BigDecimal price, Integer stock, String status) {}
```

## 7. Repository — ejemplo

`ProductRepository`

```java
package com.parcezza.backend.repository;

import com.parcezza.backend.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
  Optional<Product> findBySku(String sku);
}

// Recommendation: add query methods to support filters, e.g. findByCategoryId, findByVendorId
```

## 8. Service layer — ejemplo con transacción

`ProductService` responsibilities:
- list products with filters
- get product by id
- create/update product (admin)
- decrease stock safely

```java
package com.parcezza.backend.service;

import com.parcezza.backend.domain.Product;
import com.parcezza.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
  private final ProductRepository productRepository;
  public ProductService(ProductRepository productRepository) { this.productRepository = productRepository; }

  public List<Product> listAll() { return productRepository.findAll(); }

  public Product getById(Long id) { return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found")); }

  public Product create(Product p) { return productRepository.save(p); }

  @Transactional
  public void decreaseStock(Long productId, int qty) {
    Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Product not found"));
    if (product.getStock() < qty) throw new BadRequestException("Insufficient stock");
    product.setStock(product.getStock() - qty);
    productRepository.save(product);
  }
}
```

## 9. Controller — ejemplo REST

`ProductController`

```java
package com.parcezza.backend.controller;

import com.parcezza.backend.domain.Product;
import com.parcezza.backend.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
  private final ProductService productService;
  public ProductController(ProductService productService) { this.productService = productService; }

  @GetMapping
  public List<Product> list() { return productService.listAll(); }

  @GetMapping("/{id}")
  public Product get(@PathVariable Long id) { return productService.getById(id); }

  @PostMapping
  public Product create(@RequestBody Product product) { return productService.create(product); }
}
```

## 10. Security — JWT outline

- Use `spring-boot-starter-security`.
- Flow: `AuthController` issues JWT access token + refresh token stored (DB or redis). Use `Authorization: Bearer <token>` on protected endpoints.
- Components:
  - `JwtTokenProvider` (create/validate token)
  - `JwtAuthenticationFilter` (extract token, set SecurityContext)
  - `SecurityConfig` (permit `/api/auth/**` and secure `/api/admin/**` with role checks)

Snippet `SecurityConfig` (simplified):

```java
@Configuration
public class SecurityConfig {
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
    http.csrf().disable()
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/auth/**", "/api/products", "/api/products/**").permitAll()
        .requestMatchers("/api/admin/**").hasRole("ADMIN")
        .anyRequest().authenticated()
      )
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
```

## 11. Payments (Stripe) — flow

1. Client hits `POST /api/checkout` with cart and shipping data.
2. Backend creates Order with status `PENDING`, calls Stripe API to create `PaymentIntent` and returns `clientSecret`.
3. Front calls Stripe.js to confirm payment in client.
4. Stripe webhook `POST /api/webhooks/stripe` updates order status to `PAID` and triggers post‑payment actions (email, decrement stock).

Implement webhook verification using Stripe signing secret.

## 11.1 Use enums and auditing (recommended)

Use enums for domain states (product, order, vendor, payment, shipment). Enums provide strong typing and avoid invalid string values.

Suggested enums:
- `ProductStatus { DRAFT, SUBMITTED, PUBLISHED, SUSPENDED, REMOVED }`
- `OrderStatus { PENDING, PAID, CANCELLED, SHIPPED, COMPLETED }`
- `VendorStatus { PENDING, VERIFIED, SUSPENDED }`
- `PaymentStatus { CREATED, SUCCEEDED, FAILED, REFUNDED }`
- `ShipmentStatus { READY, IN_TRANSIT, DELIVERED, RETURNED }`

JPA mapping example:

```java
public enum ProductStatus { DRAFT, SUBMITTED, PUBLISHED, SUSPENDED, REMOVED }

@Entity
public class Product {
  // ...
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ProductStatus status;
}
```

Auditing (who/when): use a mapped superclass and Spring Data JPA auditing to capture createdBy/createdAt/updatedBy/updatedAt automatically.

`Auditable` example:

```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {
  @CreatedBy
  @Column(name = "created_by", updatable = false)
  private String createdBy;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private Instant createdAt;

  @LastModifiedBy
  @Column(name = "updated_by")
  private String updatedBy;

  @LastModifiedDate
  @Column(name = "updated_at")
  private Instant updatedAt;
}
```

Enable auditing in a config class:

```java
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {
  @Bean
  public AuditorAware<String> auditorProvider() {
    return new SpringSecurityAuditorAware(); // implement to return current username/email
  }
}
```

Notes:
- Store auditor (String) as user identifier (email or id). Implement `AuditorAware` to read from SecurityContext.
- For full historical audit (every change), consider an audit log table or use tools like Hibernate Envers.

## 12. Persistence and migrations

- Use PostgreSQL in production.
- Use Flyway for DB migrations: place SQLs in `db/migration`.

Example `application.yml` snippet:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:parcezza}
    username: ${DB_USER:postgres}
    password: ${DB_PASS:postgres}
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        format_sql: true
  flyway:
    enabled: true
```

### Flyway example: create unique index on sku and product_variant sku

Create file `src/main/resources/db/migration/V1__create_sku_indexes.sql` with:

```sql
-- unique sku for products
CREATE UNIQUE INDEX IF NOT EXISTS idx_products_sku ON products (sku);

-- if using product_variants table
CREATE UNIQUE INDEX IF NOT EXISTS idx_product_variants_sku ON product_variants (sku);
```

## 13. Local development — Docker Compose

`docker-compose.yml` minimal:

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15
    env_file: .env
    volumes:
      - pgdata:/var/lib/postgresql/data
    ports: ['5432:5432']

  redis:
    image: redis:7
    ports: ['6379:6379']

  backend:
    build: ./backend
    depends_on: [postgres, redis]
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/parcezza
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=postgres
    ports: ['8080:8080']

volumes:
  pgdata:
```

## 14. Testing

- Unit tests: JUnit 5 + Mockito for services.
- Integration tests: use Testcontainers (Postgres) or H2 for fast runs.
- Controller tests: use MockMvc or `@WebMvcTest` for focused tests.

Example unit test for `ProductService`:

```java
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
  @Mock ProductRepository productRepository;
  @InjectMocks ProductService productService;

  @Test
  void decreaseStock_whenInsufficient_thenThrow() {
    Product p = new Product(); p.setId(1L); p.setStock(1);
    when(productRepository.findById(1L)).thenReturn(Optional.of(p));
    assertThrows(BadRequestException.class, () -> productService.decreaseStock(1L, 2));
  }
}
```

## 15. Observability

- Logs: use structured logging (Logback JSON encoder) and correlation ids.
- Errors: central `@ControllerAdvice` for consistent error responses.
- Monitoring: expose metrics with Micrometer + Prometheus.

## 16. CI/CD (sketch)

- GitHub Actions workflow: `build-and-test-backend.yml`:
  - Checkout, setup Java, cache maven, mvn -DskipTests=false test, mvn package
  - Build Docker image and push to registry
  - Deploy to staging

## 17. Security checklist

- Use BCrypt for password hashing.
- Short-lived access tokens + refresh tokens.
- Validate all input DTOs (Bean Validation).
- Rate limit auth endpoints.
- Do not log sensitive data.

## 18. Common pitfalls & tips

- Atomic checkout: ensure creating order and decrementing stock is atomic; use DB transaction.
- Idempotency: webhooks must be idempotent (store event id).
- Avoid business logic in controllers.

---

Si quieres, ahora genero en el proyecto los archivos ejemplo (`Product` entity ya es un ejemplo; puedo crear `ProductRepository`, `ProductService`, `ProductController`, DTOs y un `README` con comandos de dev). ¿Lo genero ahora? 
