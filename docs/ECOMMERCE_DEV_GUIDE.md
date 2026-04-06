# Guía de desarrollo — E‑commerce Parcezza

Documento guía para arrancar el frontend Angular y el backend Spring Boot del e‑commerce. Contiene requisitos, modelos, endpoints, principios de diseño (SOLID, KISS, DRY), patrones recomendados, ejercicios y checklist inicial.

---

## 1. Resumen / Objetivo

Crear un e‑commerce profesional con frontend en Angular y backend en Spring Boot. Esta guía te da la estructura mínima, los modelos que necesitaremos, decisiones arquitectónicas y ejercicios para que aprendas y pienses mientras desarrollas.

-## 2. Requisitos funcionales (mínimos)
- Registro / Login (roles: `customer`, `admin`, optional `vendor`)
- Catálogo: listar, filtrar, ver detalle
- Carrito: agregar, actualizar, persistir
- Checkout: crear orden, integrar Stripe (modo test)
- Órdenes: historial y estado
- Admin: CRUD productos, gestión pedidos y stock

## 3. Requisitos no funcionales
- Seguridad: HTTPS, JWT + refresh tokens, validación
- Performance: lazy loading, optimización de imágenes, CDN
- Escalabilidad: servicios stateless, cache (Redis)
- Observabilidad: logs estructurados, Sentry, métricas
- Tests: unitarios, integración y E2E

## 4. Modelos de dominio (resumen)

Tip: define DTOs para API y no expongas entidades JPA tal cual.

### 4.1 TypeScript (ejemplos)

`src/app/models/product.model.ts`

```ts
export interface Product {
  id: number;
  vendorId?: number; // nullable: null = platform-managed
  sku: string;
  name: string;
  description?: string;
  price: number;
  currency?: string;
  stock: number;
  images?: string[];
  categoryId?: number;
  status?: 'DRAFT'|'PUBLISHED'|'REMOVED';
}

export interface CartItem {
  productVariantId: number;
  qty: number;
  priceSnapshot: number; // price at time of add
}

export interface Order {
  id: number;
  userId: number;
  items: CartItem[];
  subtotal: number;
  shipping: number;
  tax: number;
  total: number;
  status: 'PENDING'|'PAID'|'SHIPPED'|'COMPLETED'|'CANCELLED';
  createdAt: string;
}
```

### 4.2 Java (ejemplo de entidad simplificada)

`backend/src/main/java/com/parcezza/backend/domain/Product.java`

```java
package com.parcezza.backend.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String sku;
  private String name;
  @Column(columnDefinition = "TEXT")
  private String description;
  private BigDecimal price;
  private Integer stock;
  private String currency;
  // getters / setters
}
```

## 5. Endpoints API (mínimos)

- `POST /api/auth/register`
- `POST /api/auth/login` -> accessToken + refreshToken
- `GET /api/products` (filtros: category, search, price)
- `GET /api/products/{id}`
- `GET /api/categories`
- `GET /api/cart` (usuario)
- `POST /api/cart/items`
- `POST /api/checkout` -> crea orden + intenta paymentIntent
- `POST /api/webhooks/stripe` -> webhook
- `GET /api/orders` (usuario)

Admin (protegidos por rol):
- `POST /api/admin/products`
- `PUT /api/admin/products/{id}`
- `DELETE /api/admin/products/{id}`
- `GET /api/admin/orders`

## 6. Arquitectura recomendada

Backend (Spring Boot):
- Controllers (DTOs)
- Services (transacciones y lógica)
- Repositories (Spring Data JPA)
- Integrations: Stripe client, Email service, S3 storage
- Events: publicar eventos (OrderCreated) para tareas asíncronas

Frontend (Angular):
- Feature modules: `core`, `shared`, `catalog`, `cart`, `checkout`, `orders`, `admin`
- Lazy load por feature
- `core`: `AuthService`, `ApiInterceptor`, `ErrorHandler`
- State: Signals + services (NgRx sólo si realmente lo necesitas)
- UI: Angular Material + Tailwind (Material para componentes, Tailwind para utilidades)

## 7. Principios y buenas prácticas (para pensar)

- SOLID: cada servicio/clase con una única responsabilidad; inyección de dependencias; abstraer con interfaces cuando haya múltiples implementaciones.
- KISS: soluciones claras y simples; evita optimizaciones prematuras.
- DRY: extrae lógica repetida a helpers o services; no repitas validaciones.
- YAGNI: no implementes features que no vas a usar pronto.
- Fail fast: valida y lanza errores tempranos.

Consejo: escribe PRs pequeños y con propósito; añade tests mínimos para la lógica crítica (checkout, cálculos de total).

**Naming & conventions**
- Use English names for variables, DTO fields and DB columns (e.g. `product_id`, `vendor_id`, `sku`, `price`).
- Use camelCase in code (`productVariantId`) and snake_case for DB columns (`product_variant_id`) if you prefer SQL conventions.

**SKU guidance**
- SKU = Stock Keeping Unit, unique identifier per sellable item or variant.
- Make `sku` unique (DB unique index). Prefer immutability: if product/variant changes materially, create new SKU.
- Validation: allow alphanumerics, dashes/underscores, 6–30 chars. Examples: `PAR-TSH-001-M-BLU`, `00012345`.

Si quieres, puedo añadir una migration Flyway de ejemplo que crea índice único sobre `products.sku`.

## 8. Patrones útiles

- Repository + Service: separar persistencia de lógica de negocio.
- DTO / Mapper: evitar exponer entidades; usa mappers (MapStruct en Java o manualmente).
- Adapter / Client: encapsular integraciones externas (Stripe, S3) tras una interfaz.
- Strategy: para calcular precios, impuestos o promociones configurables.
- Event-driven: publicar eventos (OrderCreated) para email, stock, notificaciones.
- Circuit Breaker / Retry: para llamadas externas críticas.

## 9. Ejercicios prácticos (con énfasis en aprender)

Ejercicio A — CRUD de Productos (nivel 1)
- Backend: CRUD endpoints, JPA repository, tests unitarios.
- Frontend: list + detail + admin create form.
- Tests: unit para `ProductService` (comprobación de stock y cálculos simples).

Ejercicio B — Carrito básico (nivel 2)
- Implementa `CartService` en Angular usando Signals; persiste en `localStorage`.
- Sincroniza con backend al loguear al usuario.

Ejercicio C — Checkout minimal con Stripe (nivel 3)
- Backend: endpoint `/api/checkout` que devuelva clientSecret y completar flujo front.
- Frontend: usar Stripe.js para confirmar el pago usando `clientSecret` devuelto.
- Añade webhook en backend para actualizar estado.

Ejercicio D — SSR/Prerender (nivel 3)
- Añadir Angular Universal y prerenderizar páginas de catálogo y producto.

## 10. Ejemplo de una clase a implementar (te hace pensar)

Implementa `CartService` en Angular (pseudocódigo):

```ts
// responsibilities: add/remove items, total, persist, sync when logged in
class CartService {
  private items = signal<CartItem[]>([]);
  add(item: CartItem) { /* merge qty if exists, push, persist */ }
  updateQty(productId:number, qty:number) { /* update and persist */ }
  total() { /* reduce prices */ }
  persist() { localStorage.setItem('cart', JSON.stringify(this.items())) }
  syncToServer() { /* POST /api/cart when user logged */ }
}
```

Pregunta para reflexionar: ¿Dónde pones la lógica de cálculo de descuentos? ¿En frontend (mejor UX) o backend (fuente de la verdad)? ¿Cómo mantienes ambos en sincronía?

## 11. Testing mínimo sugerido
- Backend: tests unit para services; integración en endpoints clave usando H2.
- Frontend: unit tests para services (Jest) y E2E para flujos con Playwright.

## 12. CI/CD (esquema rápido)

- Jobs: `build-frontend`, `test-frontend`, `build-backend`, `test-backend`, `deploy-staging`, `approve->deploy-prod`.
- Frontend: `npm ci`, `npm run build`, artefacto `dist/` deploy a S3/CloudFront o Vercel.
- Backend: Maven build, tests, Docker image push, despliegue a Fargate/ECS o provider.

## 13. Checklist inicial (rápido)
- [ ] `.nvmrc` con versión Node (recomendado: `lts/*`)
- [ ] `parcezza-frontend/README.md` con comandos `npm run start`, `build`
- [ ] Instalar Tailwind + Material
- [ ] Crear `core/services`: `AuthService`, `ApiInterceptor`, `ProductService`, `CartService`
- [ ] Endpoints backend básicos: products, auth, cart, checkout

---

Si quieres, genero automáticamente en el repo los siguientes archivos iniciales:
- `docs/ECOMMERCE_DEV_GUIDE.md` (este archivo)
- `parcezza-frontend/src/app/models/product.model.ts` (TypeScript model)
- `parcezza-frontend/src/app/core/services/product.service.ts` (Angular service)
- `parcezza-frontend/README.md` con comandos básicos

Dime qué quieres que cree ahora y lo añado.
