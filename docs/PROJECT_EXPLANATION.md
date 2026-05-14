# Project Explanation (Detailed)

## 1. Goal and main idea
This backend is a Spring Boot REST API for an ecommerce domain. The goal is to keep the structure simple (KISS), avoid repetition (DRY), and keep responsibilities clear (SOLID). The project separates HTTP concerns, business rules, security, persistence, and the domain model. This allows changes in one area without breaking the rest.

## 2. Architecture overview
The code uses a classic layered architecture:

1) API layer (controllers)
2) Service layer (business rules)
3) Persistence layer (repositories)
4) Domain layer (entities)
5) Security layer (JWT, authentication)

The layers only talk downwards. Controllers do not talk to repositories directly, and entities are not exposed as API payloads. Instead, DTOs are used to keep API contracts stable.

## 3. Domain model and why it is designed like this
The domain entities represent the core ecommerce concepts:

- User: the account holder. It has email, fullName, enabled, passwordHash, roles, and addresses.
- Role: the authorization role for the user (ROLE_USER, ROLE_SELLER, ROLE_ADMIN).
- Address: user shipping or billing data. One user can have many addresses.
- Seller: a profile for users that want to sell. A seller is owned by one user.
- Product: a product that belongs to a seller.
- ProductVariant: a variant of a product (size, color) with its own sku, stock, and price override.
- VariantAttribute: key/value attributes for each variant.
- Catalog: a logical grouping of products.
- Cart: a user cart that holds the current intended purchase.
- CartItem: the line inside a cart with product/variant, quantity, unit price, and totals.
- Order: a confirmed snapshot of a cart, with totals and status.
- OrderItem: line items for an order.
- Payment: a payment attempt with status (success/failed) and card last4.
- Shipment: delivery data and status for an order.
- ReturnRequest: tracks a return/refund request for an order.
- InventoryMovement: records stock reservations, releases, sales, and refunds.
- Auditable: shared fields for createdBy/createdAt/updatedAt, inherited by Product and Seller.

Why this shape?
- User to Role is many-to-many because a user can have several roles.
- User to Address is one-to-many because a user can have multiple addresses.
- Seller to User is many-to-one (owner) because a user can have a seller profile.
- Product to Seller is many-to-one because each product belongs to one seller.
- Product to ProductVariant is one-to-many because each product can have many variants.
- ProductVariant to VariantAttribute is one-to-many because each variant can have many attributes.
- Catalog to Product is many-to-many because a product can appear in many catalogs and a catalog contains many products.
- User to Cart is one-to-one because each user keeps one active cart.
- Cart to CartItem is one-to-many because a cart has multiple lines.
- Order to OrderItem is one-to-many because a confirmed order has multiple lines.
- Order to Shipment is one-to-one because each order has a shipment lifecycle.
- Order to Payment is one-to-many because multiple payment attempts can exist.
- Order to ReturnRequest is one-to-one because the current design allows one active return process per order.
- Product/variant to InventoryMovement is one-to-many because we want a trace of every stock change.

## 4. POO (OOP) choices and where they are applied
This project uses OOP in a justified way:

- Entities model the real business concepts and hold state.
- Interfaces define service contracts (AuthService, ProductService, etc.).
- Implementations (AuthServiceImpl, ProductServiceImpl) contain the actual logic.
- This allows dependency inversion: controllers depend on interfaces, not concrete classes.
- Polymorphism is used via service interfaces: any implementation can replace another without changing controllers.
- Abstract base class Auditable provides common audit fields. It avoids repetition in Product and Seller.
- Card validation is behind a CardValidator interface, with LuhnCardValidator implementation. This is an intentional, minimal use of polymorphism to allow different validation rules without changing the PaymentService.

This is minimal but effective. We avoid complex inheritance trees or unnecessary patterns.

## 5. API layer (controllers)
Controllers are small and only handle HTTP details:
- Map endpoints and HTTP verbs.
- Validate input via annotations (@Valid).
- Delegate to services.
- Return DTOs or simple responses.

Key controllers:
- AuthController: register and login.
- ProductController: CRUD products.
- ProductVariantController: CRUD variants under a product.
- SellerController: seller registration and admin status updates.
- CatalogController: CRUD catalogs and add/remove product from catalog.
- ProfileController: current user profile and addresses.
- CartController: cart operations (add, update, remove, clear).
- OrderController: checkout and order queries.
- PaymentController: simulated payment confirmation.
- ShipmentController: shipment status queries and admin updates.

Controllers do not implement business rules. That is the service layer job.

## 6. DTOs and validation
DTOs define the request/response shapes. This avoids leaking entity internals.

Examples:
- ProductUpsertRequest has sku, name, description, basePrice, stock, currency, active.
- ProductResponse returns id, sku, name, etc.
- SellerRequest is used for seller registration.
- ProfileUpdateRequest limits update to fullName only.
- CartItemRequest defines product/variant and quantity.
- PaymentRequest defines card details for the simulation.

Validation uses standard annotations:
- @NotBlank, @Size, @PositiveOrZero, @Email

This ensures invalid data is rejected early with a 400 response.

## 7. Service layer (business rules)
Services contain rules and state changes. This is where most decisions live.

### AuthService
- register: checks if email exists, creates user, assigns ROLE_USER, hashes password, returns JWT.
- login: uses AuthenticationManager to validate credentials, returns JWT.

### SellerService
- register: creates a seller for the current user. It starts as PENDING.
- updateStatus (admin): changes seller status. When approved, user gets ROLE_SELLER.

### ProductService
- create: only for approved sellers, unique SKU enforced, product tied to seller.
- update: only owner can update, SKU uniqueness enforced.
- delete: only owner can delete.

### ProductVariantService
- create/update/delete: only owner can change variants on their product.
- list: public access for a product.

### CatalogService
- CRUD for catalogs (admin).
- addProduct/removeProduct: maintain many-to-many relation.

### ProfileService
- get/update profile for current user.
- manage addresses with primary address handling.

### CartService
- add/update/remove/clear: manages cart items and reserves stock immediately when adding to the cart.
- stock reservation uses product or variant stock depending on the item.

### OrderService
- checkout: creates order and order items from cart snapshot.
- creates a shipment in PENDING status with a tracking code.

### PaymentService
- confirm: validates card with Luhn + expiry + CVV.
- stores a payment attempt and sets status to SUCCESS or FAILED.
- on success, the order moves to PAID.
- refund: creates a refund payment record, sets the order to REFUNDED, and restores inventory.

### ShipmentService
- updateStatus: admin updates shipment status and tracking code.
- getByOrder: customer can see shipment for their own order.

### InventoryService
- reserve: reduces available stock when an item is placed in the cart.
- release: returns reserved stock when a cart item is removed or expires.
- consume: records final sale when checkout confirms the order.
- refund: restores stock when an order is refunded.

### ReturnService
- requestReturn: customer requests a return only after delivery.
- updateStatus: admin advances the return workflow using explicit transitions.

Transactions are used in write operations so data changes stay consistent.

## 8. Security and authorization
Security is JWT based and stateless.

Key components:
- JwtAuthenticationFilter: extracts Bearer token, validates, builds security context.
- JwtService: token generation and validation.
- UserDetailsServiceImpl: loads user details by email.
- CurrentUserService: provides the authenticated user for services.

Authorization rules:
- /api/auth/** is public.
- GET /api/products/** and GET /api/catalogs/** are public.
- Everything else requires authentication.
- Method-level checks using @PreAuthorize enforce roles:
  - Seller endpoints for product create/update/delete require ROLE_SELLER.
  - Admin endpoints for seller status and catalog management require ROLE_ADMIN.
  - Admin shipment updates require ROLE_ADMIN.

This keeps security easy to understand while still enforcing business rules.

## 9. Persistence and database
Spring Data JPA repositories handle persistence. Each repository is a simple interface with derived queries.

Flyway manages schema via migration files. The schema is explicit and reproducible.

Important tables:
- users, roles, user_roles
- sellers, products, product_variants, variant_attributes
- catalogs, catalog_products
- addresses
- carts, cart_items
- orders, order_items
- payments
- shipments
- inventory_movements
- return_requests

## 10. Error handling
Exceptions are mapped to HTTP responses:
- DuplicateResourceException -> 409
- ResourceNotFoundException -> 404
- BadRequestException -> 400
- UnauthorizedException -> 401
- AccessDeniedException -> 403

This keeps the API consistent and predictable.

## 11. KISS and DRY in practice
- KISS: Each class has a narrow role. No heavy patterns or frameworks beyond Spring.
- DRY: shared logic is centralized (CurrentUserService, Auditable, DTOs).
- Services reuse repository queries and avoid repeating ownership checks.

## 12. How a request flows end to end (example)
Example: checkout and simulated payment

1) Client adds items to cart. Stock is reserved immediately.
2) Client sends POST /api/orders/checkout.
3) OrderService creates the order and a shipment record.
4) Client sends POST /api/payments/confirm with card details.
5) PaymentService validates card using Luhn + expiry + CVV.
6) If valid, payment is SUCCESS and order becomes PAID.
7) If the order is delivered later, the customer can request a return, and the admin can move it through approval, receipt, and refund.

### Reservation expiry
Cart items carry a `reservedUntil` timestamp. If the user keeps items too long, they expire, the reservation is released, and the cart line is removed. This is a simple reservation policy that prevents stock from being blocked forever.

Each step has a clear responsibility and no layer breaks boundaries.

## 13. What to change safely
- Add new endpoints by creating DTOs + service method + controller method.
- Add new fields by updating entity + migration + DTOs.
- Add new roles by adding Role row and using @PreAuthorize.

## 14. Why this is a good balance
The system is simple enough for a small team but solid enough to grow:
- Clear boundaries.
- Minimal but useful OOP.
- Security and persistence are explicit.
- Tests can be added per service or controller.

This is what "simple but correct" looks like in a real backend.
