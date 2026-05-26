# Parcezza Commerce

Plataforma de ecommerce para restaurantes con backend robusto en Spring Boot y frontend moderno en Angular. Este repo incluye la API, la SPA y el despliegue con Docker Compose para levantar todo el stack en un solo servidor.

## Stack

- Backend: Java 21, Spring Boot 4.0.5, Spring Security (JWT), Spring Data JPA, Flyway
- Frontend: Angular 21 (standalone), TypeScript 5.9, SCSS
- DB: PostgreSQL 15
- Infra: Docker, Docker Compose, Nginx

## Vistas y diagramas

- Vista de Contexto: [docs/CONTEXT_DIAGRAM.md](docs/CONTEXT_DIAGRAM.md)
- Vista Funcional: [docs/FUNCTIONAL_DIAGRAM.md](docs/FUNCTIONAL_DIAGRAM.md)
- Vista Logica (clases backend): [docs/CLASS_DIAGRAM.md](docs/CLASS_DIAGRAM.md)
- Vista de Desarrollo (componentes y carpetas): [docs/COMPONENT_DIAGRAM.md](docs/COMPONENT_DIAGRAM.md)
- Vista de Despliegue: [docs/DEPLOYMENT_DIAGRAM.md](docs/DEPLOYMENT_DIAGRAM.md)
- Vista de Capas (backend): [docs/LAYER_DIAGRAM.md](docs/LAYER_DIAGRAM.md)

## Requerimientos funcionales (MVP real)

### RF-01: Autenticacion y sesiones JWT
| Aspecto | Descripcion |
| --- | --- |
| Codigo | RF-01 |
| Nombre | Sistema de autenticacion |
| Entradas | Email, password, rememberMe |
| Salidas | Token JWT |
| Proceso | Registro o login con credenciales, emision de token |
| Criterios | Password hasheada con BCrypt, expiracion configurable, roles basicos |

### RF-02: Gestion de perfil y direcciones
| Aspecto | Descripcion |
| --- | --- |
| Codigo | RF-02 |
| Nombre | Perfil y direcciones |
| Entradas | Datos personales y direcciones |
| Salidas | Perfil actualizado, lista de direcciones |
| Proceso | CRUD de perfil y direcciones bajo /api/me |
| Criterios | Validacion de datos y persistencia en BD |

### RF-03: Catalogo y productos publicos
| Aspecto | Descripcion |
| --- | --- |
| Codigo | RF-03 |
| Nombre | Catalogo y detalle de productos |
| Entradas | Query, collection, paginacion |
| Salidas | Lista paginada y detalle de producto |
| Proceso | Consultas a /api/products y /api/catalogs |
| Criterios | Paginacion por pagina y tamano, acceso publico GET |

### RF-04: Variantes y gestion de productos (seller)
| Aspecto | Descripcion |
| --- | --- |
| Codigo | RF-04 |
| Nombre | Variantes y CRUD de productos |
| Entradas | Datos de producto y variantes |
| Salidas | Producto o variante creada/actualizada |
| Proceso | Endpoints protegidos para rol SELLER |
| Criterios | Control de acceso por rol |

### RF-05: Carrito y reservas de inventario
| Aspecto | Descripcion |
| --- | --- |
| Codigo | RF-05 |
| Nombre | Carrito con reserva |
| Entradas | Producto o variante, cantidad |
| Salidas | Carrito actualizado |
| Proceso | Add/Update/Remove/Clear en /api/cart |
| Criterios | Reserva temporal y liberacion automatica de stock |

### RF-06: Checkout y ordenes
| Aspecto | Descripcion |
| --- | --- |
| Codigo | RF-06 |
| Nombre | Checkout y ordenes |
| Entradas | Carrito y direccion de entrega |
| Salidas | Orden creada, listado y detalle |
| Proceso | /api/orders/checkout, /api/orders, /api/orders/{id} |
| Criterios | Cancelacion disponible via /api/orders/{id}/cancel |

### RF-07: Pagos simulados
| Aspecto | Descripcion |
| --- | --- |
| Codigo | RF-07 |
| Nombre | Confirmacion de pago |
| Entradas | Datos de pago |
| Salidas | Estado del pago |
| Proceso | /api/payments/confirm |
| Criterios | Validacion Luhn y actualizacion de estado |

### RF-08: Envio y tracking basico
| Aspecto | Descripcion |
| --- | --- |
| Codigo | RF-08 |
| Nombre | Gestion de envios |
| Entradas | Orden o cambio de estado |
| Salidas | Estado del envio |
| Proceso | /api/shipments/order/{orderId}, listado admin |
| Criterios | Actualizacion de estado por rol ADMIN |

### RF-09: Devoluciones
| Aspecto | Descripcion |
| --- | --- |
| Codigo | RF-09 |
| Nombre | Solicitud y gestion de devoluciones |
| Entradas | Orden y motivo |
| Salidas | Estado de devolucion |
| Proceso | /api/returns/orders/{orderId}, listado admin |
| Criterios | Actualizacion de estado por rol ADMIN |

## Requerimientos no funcionales

### RNF-01: Seguridad
- JWT con expiracion configurable y CORS controlado.
- Passwords con BCrypt.
- Endpoints sensibles protegidos por roles (ADMIN, SELLER).

### RNF-02: Rendimiento
- Catalogo paginado para evitar cargas pesadas.
- Operaciones criticas en transacciones con reserva de inventario.

### RNF-03: Disponibilidad y despliegue
- Docker Compose con reinicio automatico en servicios.
- Migraciones automatizadas con Flyway.

### RNF-04: Mantenibilidad
- Arquitectura por capas (controller, service, repository, domain).
- Documentacion tecnica en [docs/PROJECT_EXPLANATION.md](docs/PROJECT_EXPLANATION.md).

### RNF-05: Compatibilidad
- API REST JSON.
- SPA responsive (Angular + SCSS).

## Como correr el proyecto

### Opcion 1: Docker Compose (recomendado)
1) Copia variables de entorno:

```bash
cp .env.example .env
```

2) Levanta todo el stack:

```bash
docker-compose up -d --build
```

- Frontend: http://localhost
- Backend: http://localhost:8080
- pgAdmin (opcional): http://localhost:5050

### Opcion 2: Desarrollo local

Backend:

```bash
cd backend
./mvnw spring-boot:run
```

Frontend:

```bash
cd parcezza-frontend
npm install
npm run start
```

## Variables de entorno principales

- DB: POSTGRES_DB, POSTGRES_USER, POSTGRES_PASSWORD
- Backend: SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD
- pgAdmin: PGADMIN_EMAIL, PGADMIN_PASSWORD

## Notas y limites actuales (KISS)

- No hay notificaciones en tiempo real ni panel de cocina.
- No hay modulo de reportes ni analitica.
- La gestion de roles es basica (USER/ADMIN/SELLER) y se controla via API.
