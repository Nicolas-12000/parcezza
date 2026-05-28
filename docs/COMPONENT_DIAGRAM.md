# Vista de Desarrollo (Diagrama de desarrollo detallado)

```plantuml
@startuml
title Vista de Desarrollo — Frontend + Backend (dominios, controllers, services, repositorios)
skinparam linetype ortho
skinparam classAttributeIconSize 0
skinparam shadowing false
skinparam defaultFontName "DejaVu Sans"

' ---------------------- Frontend (resumen) ----------------------
package "Frontend (Angular)" {
  [AppModule]
  [AppComponent]
  [HomeComponent]
  [CatalogComponent]
  [ProductDetailComponent]
  [CartComponent]
  [CheckoutComponent]

  [AuthService (FE)] <<service>>
  [CatalogService (FE)] <<service>>
  [CartService (FE)] <<service>>
  [OrderService (FE)] <<service>>
  [ProfileService (FE)] <<service>>
}

' FE components call backend controllers via REST
AppComponent --> [AuthService (FE)]
AppComponent --> [CatalogService (FE)]
AppComponent --> [CartService (FE)]
[CatalogComponent] --> [CatalogService (FE)]
[ProductDetailComponent] --> [CatalogService (FE)]
[CheckoutComponent] --> [OrderService (FE)]
[CartComponent] --> [CartService (FE)]

[AuthService (FE)] ..> AuthController : REST
[CatalogService (FE)] ..> CatalogController : REST
[CartService (FE)] ..> CartController : REST
[OrderService (FE)] ..> OrderController : REST

' ---------------------- Backend (detallado) ----------------------
package "Backend (Spring Boot)" {

  package "domain" {
    class Auditable {
      - createdAt: Instant
      - updatedAt: Instant
    }

    class User {
      + id: Long
      + email: String
      + password: String
      + roles: Set<Role>
    }

    class Role { + id: Long
      + name: String }

    class Address { + id: Long
      + street: String
      + city: String
      + zip: String
      + country: String }

    class Seller { + id: Long
      + name: String
      + ownerId: Long }

    class Product { + id: Long
      + sku: String
      + name: String
      + description: String
      + price: BigDecimal }

    class ProductVariant { + id: Long
      + sku: String
      + price: BigDecimal
      + stock: Integer }

    class VariantAttribute { + name: String
      + value: String }

    class Catalog { + id: Long
      + name: String }

    class Cart { + id: Long
      + userId: Long
      + total: BigDecimal }

    class CartItem { + id: Long
      + variantId: Long
      + quantity: Integer }

    class Order { + id: Long
      + userId: Long
      + status: OrderStatus
      + total: BigDecimal
      + createdAt: Instant }

    class OrderItem { + id: Long
      + variantId: Long
      + quantity: Integer
      + price: BigDecimal }

    class Payment { + id: Long
      + orderId: Long
      + status: PaymentStatus
      + amount: BigDecimal }

    class Shipment { + id: Long
      + orderId: Long
      + trackingNumber: String
      + status: ShipmentStatus }

    class InventoryMovement { + id: Long
      + variantId: Long
      + quantity: Integer
      + type: InventoryMovementType }

    class ReturnRequest { + id: Long
      + orderId: Long
      + reason: String
      + status: ReturnStatus }

    Auditable <|-- User
    Auditable <|-- Product
    Product "1" -- "*" ProductVariant : has
    ProductVariant "1" -- "*" VariantAttribute : attributes
    Catalog "1" -- "*" Product : contains
    User "1" -- "*" Address
    User "1" -- "0..1" Cart
    Cart "1" -- "*" CartItem
    Order "1" -- "*" OrderItem
    Order "1" -- "0..1" Payment
    Order "1" -- "0..1" Shipment
  }

  enum OrderStatus { PENDING
    CONFIRMED
    SHIPPED
    CANCELLED
    COMPLETED }

  enum PaymentStatus { PENDING
    CONFIRMED
    FAILED }

  enum ShipmentStatus { PENDING
    IN_TRANSIT
    DELIVERED
    CANCELLED }

  enum InventoryMovementType { IN
    OUT
    RESERVE
    RELEASE }

  enum ReturnStatus { REQUESTED
    APPROVED
    REJECTED
    COMPLETED }

  package "repository" {
    interface UserRepository <<Repository>>
    interface RoleRepository <<Repository>>
    interface ProductRepository <<Repository>>
    interface ProductVariantRepository <<Repository>>
    interface CatalogRepository <<Repository>>
    interface CartRepository <<Repository>>
    interface CartItemRepository <<Repository>>
    interface OrderRepository <<Repository>>
    interface OrderItemRepository <<Repository>>
    interface PaymentRepository <<Repository>>
    interface ShipmentRepository <<Repository>>
    interface InventoryMovementRepository <<Repository>>
    interface ReturnRequestRepository <<Repository>>
  }

  package "dto" {
    class UserDTO <<DTO>>
    class ProductDTO <<DTO>>
    class CatalogDTO <<DTO>>
    class CartDTO <<DTO>>
    class OrderDTO <<DTO>>
    class PaymentDTO <<DTO>>
  }

  package "mapper" {
    interface ProductMapper <<Mapper>>
    interface OrderMapper <<Mapper>>
    interface UserMapper <<Mapper>>
  }

  package "service" {
    class AuthService <<Service>> {
      + register(dto)
      + login(dto)
      + refreshToken()
    }

    class UserService <<Service>> {
      + findById(id)
      + updateProfile(id, dto)
    }

    class ProductService <<Service>> {
      + list(params)
      + findById(id)
      + create(dto)
      + update(id, dto)
    }

    class CatalogService <<Service>> {
      + getCatalog(id)
      + search(query)
    }

    class CartService <<Service>> {
      + getCart(userId)
      + addItem(userId, variantId, qty)
      + removeItem(userId, itemId)
    }

    class OrderService <<Service>> {
      + checkout(cart)
      + getOrders(userId)
      + cancel(orderId)
    }

    class PaymentService <<Service>> {
      + confirmPayment(orderId, paymentInfo)
    }

    class ShipmentService <<Service>> {
      + createShipment(orderId)
      + updateStatus(id, status)
    }

    class ReturnService <<Service>> {
      + requestReturn(orderId, reason)
      + processReturn(id)
    }

    class InventoryService <<Service>> {
      + reserveStock(variantId, qty)
      + releaseStock(variantId, qty)
    }
  }

  package "controller" {
    class AuthController <<Controller>> {
      + POST /api/auth/register
      + POST /api/auth/login
    }

    class ProductController <<Controller>> {
      + GET /api/products
      + GET /api/products/{id}
      + POST /api/products
      + PUT /api/products/{id}
    }

    class CatalogController <<Controller>> {
      + GET /api/catalogs
    }

    class CartController <<Controller>> {
      + GET /api/cart
      + POST /api/cart/items
      + DELETE /api/cart/items/{id}
    }

    class OrderController <<Controller>> {
      + POST /api/orders/checkout
      + GET /api/orders
      + POST /api/orders/{id}/cancel
    }

    class PaymentController <<Controller>> {
      + POST /api/payments/confirm
    }

    class ShipmentController <<Controller>> {
      + GET /api/shipments
      + POST /api/shipments/{id}/status
    }

    class ReturnController <<Controller>> {
      + POST /api/returns
      + GET /api/returns
    }

    class ProfileController <<Controller>> {
      + GET /api/me
      + PUT /api/me
    }
  }

  package "security" {
    class JwtService
    class JwtAuthenticationFilter <<Filter>>
    class UserDetailsServiceImpl <<Service>>
    class SecurityConfig <<Config>>
  }

  package "config" {
    class JpaConfig
    class DataLoader
  }

  package "exception" {
    class GlobalExceptionHandler <<ControllerAdvice>>
    class ResourceNotFoundException <<Exception>>
    class BadRequestException <<Exception>>
    class DuplicateResourceException <<Exception>>
  }

  ' ---------------------- Wiring ----------------------
  AuthController --> AuthService
  ProductController --> ProductService
  CatalogController --> CatalogService
  CartController --> CartService
  OrderController --> OrderService
  PaymentController --> PaymentService
  ShipmentController --> ShipmentService
  ReturnController --> ReturnService
  ProfileController --> UserService

  AuthService --> UserRepository
  ProductService --> ProductRepository
  CatalogService --> CatalogRepository
  CartService --> CartRepository
  OrderService --> OrderRepository
  PaymentService --> PaymentRepository
  ShipmentService --> ShipmentRepository
  ReturnService --> ReturnRequestRepository
  InventoryService --> InventoryMovementRepository

  ProductMapper ..> ProductDTO
  OrderMapper ..> OrderDTO
  UserMapper ..> UserDTO

  JwtAuthenticationFilter ..> JwtService
  UserDetailsServiceImpl ..> UserRepository

}

@enduml
```

Notes:
- Esta vista contiene paquetes y clases relevantes para un diagrama de desarrollo: entidades (domain), repositorios, servicios, controladores, DTOs, mappers y elementos de seguridad.
- He omitido archivos de despliegue/infra (scripts, docker-compose) y elementos de CI/CD ya que no aportan a la vista de desarrollo.
- Si quieres, reduzco el nivel de detalle para generar un `docs/CLASS_DIAGRAM.md` exclusivamente con las entidades del dominio.
