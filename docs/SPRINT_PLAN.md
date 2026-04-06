# Plan de Sprints — Proyecto Parcezza

Este documento describe un plan de trabajo en 8 sprints (orientativo) para organizar el desarrollo del backend y los entregables principales. Cada sprint incluye objetivos, criterios de aceptación y tareas concretas para que puedas avanzar en orden sin perder dependencias.

Duración sugerida: sprints de 1 a 2 semanas según prioridad y equipo.

## Sprint 1 — Proyecto e Infra (1 semana)
- Objetivo: dejar el entorno de desarrollo reproducible y la infraestructura mínima local.
- Criterios de aceptación:
  - `docker-compose` con Postgres, Redis y MinIO funcionando.
  - Maven compila y la app arranca enlazándose a la BD local.
  - `README.md` con comandos de arranque.
- Tareas clave:
  - Añadir `docker-compose.yml` (postgres, redis, minio) en `/dev`.
  - Habilitar Flyway y crear carpeta `src/main/resources/db/migration`.
  - Añadir `application-dev.properties` con conexiones locales.
  - Documentar comandos para arrancar localmente.

## Sprint 2 — Dominio base y auditoría (1 semana)
- Objetivo: crear la base común del dominio (auditoría y enums) para que otras entidades la hereden.
- Criterios de aceptación:
  - `Auditable` mapped superclass presente y testeado.
  - Enums comunes (`ProductStatus`, `OrderStatus`, `SellerStatus`) creados.
- Tareas clave:
  - Implementar `Auditable` (createdBy, createdAt, updatedBy, updatedAt) y habilitar `@EnableJpaAuditing`.
  - Añadir los enums en `domain/enums` y documentar transiciones críticas.

## Sprint 3 — Identidad y Seller (1 semana)
- Objetivo: completar modelo de usuarios, roles y vendedores (seller) y un flujo básico de registro/login.
- Criterios de aceptación:
  - Entidades `User`, `Role`, `Seller` con repositorios y migraciones.
  - Registro básico que guarda `passwordHash` con `PasswordEncoder`.
- Tareas clave:
  - Corregir `User` (auditing, validaciones, roles, convenience methods).
  - Crear `Seller` + `SellerStatus` y endpoints admin básicos.
  - Implementar `UserDetailsService` o adapter para Spring Security.

## Sprint 4 — Catálogo (2 semanas)
- Objetivo: modelar y exponer CRUD de productos, variantes, categorías e imágenes.
- Criterios de aceptación:
  - `Product`, `ProductVariant`, `Category`, `ProductImage` con migraciones y repositorios.
  - Generación de `slug` seguro y `sku` único (índices Flyway).
- Tareas clave:
  - DTOs + MapStruct para mapeo entidad↔API.
  - Endpoints CRUD con validaciones y paginación.
  - Tests unitarios básicos para servicio de productos.

## Sprint 5 — Almacenamiento y Media (1 semana)
- Objetivo: abstracción de almacenamiento y subida/consulta de media (MinIO/S3).
- Criterios de aceptación:
  - `StorageService` interface con implementaciones `MinioStorageService` y `S3StorageService`.
  - `MediaAsset` entidad y flujo de upload/download con MinIO.
- Tareas clave:
  - Configurar cliente MinIO y ejemplos de signed URLs.
  - Integrar subida en el endpoint de producto (foto/imagen).

## Sprint 6 — Inventario y Carrito (1 semana)
- Objetivo: manejar stock por variante y carrito de compra (reservas básicas).
- Criterios de aceptación:
  - `InventoryTransaction` funcionando y pruebas transaccionales.
  - `Cart` / `CartItem` endpoints para añadir/quitar items y calcular totales.
- Tareas clave:
  - Implementar decremento/rollback en transacciones (optimistic/pessimistic según diseño).
  - Integrar con `ProductVariant.stock` y restricciones en checkout.

## Sprint 7 — Pedidos, Pagos y Envíos (2 semanas)
- Objetivo: implementar flujo de pedido completo y pasarela de pago (sandbox).
- Criterios de aceptación:
  - `Order`, `OrderItem`, `Payment`, `Shipment` con transiciones y webhooks simulados.
  - Prueba end-to-end: crear pedido, pagar (simulado), cambiar estado a `SHIPPED`.
- Tareas clave:
  - Integración con Stripe (modo test) o mock de proveedor.
  - Manejar webhooks y estado de pago/cancelación.

## Sprint 8 — Admin, QA y CI/CD (2 semanas)
- Objetivo: pulir la plataforma con panel admin, tests y pipeline de CI/CD.
- Criterios de aceptación:
  - GitHub Actions ejecuta build y tests; imágenes Docker generadas.
  - Admin endpoints para moderar productos y verificar sellers.
  - Cobertura mínima de tests de integración con Testcontainers.
- Tareas clave:
  - Configurar GitHub Actions (build/test/publish to registry).
  - Añadir pruebas de integración y smoke tests.
  - Documentar despliegue y variables secretas.

---

## Flujo de trabajo recomendado por entidad (patrón repetible)
1. Definir entidad + validaciones.  
2. Añadir migración Flyway (DDL).  
3. Crear `Repository` (Spring Data).  
4. Añadir `DTO` y `Mapper` (MapStruct).  
5. Implementar `Service` con lógica transaccional.  
6. Exponer `Controller` (REST) con validaciones y tests.  

## Notas prácticas
- Trabaja por verticales (catalogo completo antes de orders) para reducir dependencias rotas.  
- Usa feature branches pequeñas y PRs por tarea para facilitar revisiones.  
- Prioriza tests automáticos para las partes críticas (pagos, stock).  

Si quieres, puedo: generar el `docker-compose.yml` de Sprint 1, crear los enums y `Auditable` de Sprint 2, o aplicar las correcciones mínimas en `User` del Sprint 3. Indica cuál prefieres y lo hago.
