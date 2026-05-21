# Documentación Técnica Detallada: Proyecto Parcezza Commerce

Esta documentación explica en profundidad la arquitectura, la lógica de negocio y los algoritmos que determinan cómo se calculan y guardan los datos críticos en el sistema.

---

## 1. Arquitectura General y Stack Tecnológico

- **Backend (API REST):** Java 21, Spring Boot 3, Spring Security (JWT), Spring Data JPA, PostgreSQL.
- **Frontend (SPA):** Angular 21 (Standalone Components), TypeScript, SCSS.

El flujo de datos siempre es **Unidireccional**:
`Cliente -> Controller -> Service (Interface -> Impl) -> Repository -> Base de Datos`

---

## 2. Explicación Profunda de la Lógica de Negocio (Backend)

Aquí es donde ocurren los cálculos reales. No dependemos de la base de datos para calcular totales ni precios; todo se hace en memoria dentro de transacciones de Java para garantizar consistencia.

### 2.1. Lógica del Carrito y Reserva de Inventario (`CartServiceImpl`)
Cuando un usuario añade un producto al carrito, no solo se guarda un registro; el sistema aparta físicamente el inventario para que nadie más lo pueda comprar.

1. **Resolución de Precios:** El sistema evalúa si el usuario escogió una "Variante" (ej. Talla XL) o el producto base. 
   ```java
   // En CartServiceImpl.java
   private BigDecimal resolveUnitPrice(Product product, ProductVariant variant) {
       // Si hay una variante con un precio específico, lo usa. Si no, usa el precio base.
       return variant != null && variant.getPriceOverride() != null
           ? variant.getPriceOverride()
           : product.getBasePrice();
   }
   ```
2. **Cálculo del Total de la Línea:** Multiplica el precio resuelto por la cantidad: `unitPrice.multiply(BigDecimal.valueOf(request.quantity()))`.
3. **Timer de Expiración (Reserva):** El sistema añade exactamente 30 minutos a la hora actual (`Instant.now().plus(Duration.ofMinutes(30))`) y lo guarda en el campo `reservedUntil`.
4. **Impacto en Inventario:** Llama a `inventoryService.reserve()`. Este servicio busca el producto en la base de datos, hace `stock = stock - cantidad`, y crea un registro `InventoryMovement` de tipo `RESERVE` atado al ID del Carrito. 

*¿Qué pasa si pasan los 30 minutos?* Cada vez que el usuario consulta su carrito, el método `expireReservations(cart)` revisa si `reservedUntil` ya pasó. Si es así, elimina el ítem del carrito y llama a `inventoryService.release()`, devolviendo el stock a la tienda.

### 2.2. Lógica de Checkout (`OrderServiceImpl`)
Convertir un carrito temporal en una orden firme e inmutable.

1. **Validación de Expiración:** Antes de hacer nada, el sistema recorre todo el carrito. Si *al menos un ítem* expiró mientras el usuario ponía su dirección, cancela todo el proceso y le avisa al usuario (`throw new BadRequestException`).
2. **"Congelación" de Precios:** La orden no hace referencia a los precios actuales del catálogo (porque los precios pueden cambiar mañana). El servicio itera sobre el `Cart`, crea objetos `OrderItem` y les copia el precio exacto que el usuario tenía en su carrito.
3. **Cálculo del Total:** A la vez que itera, va sumando los totales de las líneas en una variable `BigDecimal total`, y finalmente se lo asigna a `order.setTotalAmount(total)`.
4. **Consumo Definitivo de Inventario:** Llama a `inventoryService.consume()`. Esto no resta stock (porque ya se restó en el paso del Carrito), sino que toma el registro de "Reserva" en la tabla `InventoryMovement` y lo cambia permanentemente a tipo `CONSUME`.
5. **Creación del Envío:** Automáticamente genera un objeto `Shipment` con estado `PENDING` y genera un código de rastreo aleatorio (`TRK-` + UUID). Finalmente, vacía el carrito borrando sus ítems. Todo ocurre en una sola `@Transactional`.

### 2.3. Lógica de Pagos Simulada (`PaymentServiceImpl`)
Cuando la orden ya está en estado `CREATED`, el usuario ingresa su tarjeta.
1. **Validación (Algoritmo de Luhn):** Pasamos el número por un `CardValidator` (implementa Luhn para chequear que la tarjeta sea matemáticamente válida). Revisa que el CVV tenga 3 o 4 dígitos y que la fecha de expiración sea futura.
2. **Cambio de Estado:** Si falla, crea un registro de pago `FAILED`. Si es exitoso, crea el registro `SUCCESS` guardando solo los últimos 4 dígitos (`last4`) y actualiza el estado de la Orden a `PAID`.

---

## 3. Explicación de la Lógica del Frontend (Angular)

El frontend está diseñado para no bloquearse y enviar datos limpios al servidor.

### 3.1. Protección y Lógica de Formularios Reactivos
El cálculo de qué es válido y qué no se hace en tiempo real mientras el usuario teclea.
Usamos `SecurityValidators`, una clase que inyecta código directamente en el ciclo de validación de Angular.
```typescript
static noHtmlTags(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    // Si el texto contiene "<", ">" o "&", retorna un error { 'htmlTags': true }
    // Esto desactiva automáticamente el botón de "Pagar" o "Registrarse" en el HTML.
    const hasHtml = /[<>&]/.test(control.value);
    return hasHtml ? { 'htmlTags': true } : null;
  };
}
```

### 3.2. Scroll Infinito y Paginación (Carga por Tandas)
Para no saturar la memoria RAM (PC) del usuario al cargar el catálogo:
1. El componente define un "Lote" de tamaño fijo (`pageSize = 12`) y una "Página actual" (`page = signal(0)`).
2. Un `@HostListener('window:scroll')` calcula continuamente: `(posición del scroll) + (altura de la ventana)`.
3. Si ese número es mayor a la altura total de la página menos 400 píxeles, dispara la lógica matemática: `page = page + 1`.
4. Angular manda un `GET` a Spring Boot con los nuevos parámetros (`?page=1`). 
5. Cuando la data llega, Angular **no** reemplaza el arreglo viejo. Usa el operador de propagación (`...`) para fusionar la vieja data con la nueva: `products.update(current => [...current, ...newItems])`. Esto hace que el catálogo crezca fluidamente hacia abajo.

### 3.3. Manejo de Estado con Signals
En lugar de depender del Backend para sumar los números en la interfaz, el `CartService` del frontend recalcula localmente el `totalCount` de ítems en el carrito usando la directiva matemática de Signals:
```typescript
cartItemCount = computed(() => {
  const currentCart = this.cart(); // Si el carrito cambia, esto se recalcula solo
  return currentCart ? currentCart.items.reduce((sum, item) => sum + item.quantity, 0) : 0;
});
```
Ese número se enlaza directamente a la "Burbuja roja" del carrito en el menú superior, haciendo que brinque y se actualice al instante sin peticiones HTTP extras.

---

## 4. ¿Cómo Modificar o Añadir Lógica Compleja?

Si vas a añadir un descuento o código promocional en el futuro, no debes tocar el Frontend ni la Entidad directamente. La arquitectura dicta que:
1. Creas un `DiscountService` (Backend).
2. Modificas la función `OrderServiceImpl.checkout()`. Justo antes de asignar `order.setTotalAmount(total)`, llamas a tu `DiscountService` para restar el porcentaje matemático del `total`.
3. Todo debe ocurrir dentro del bloque `@Transactional` para asegurar que, si el cálculo falla, se revierta todo el proceso (incluyendo el envío y el consumo de stock).
