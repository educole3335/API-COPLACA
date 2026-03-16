# Backend Coplaca - Informe de avance

## 1. Resumen ejecutivo

Este documento describe el avance del backend de Coplaca para la venta minorista con reparto a domicilio, incluyendo los cambios funcionales, técnicos y de calidad realizados en la API.

Objetivo de negocio cubierto:
- Soportar registro y operación de clientes y personal interno.
- Gestionar pedidos por peso (kg), logística de almacén y reparto.
- Mejorar seguridad y trazabilidad.
- Incorporar pruebas unitarias para reducir riesgo de regresión.

## 2. Cronología de avance (commits)

Hitos principales por commit:
- 0c3160b: suite de pruebas unitarias JUnit del backend.
- ee178e2: flujo de pedidos por rol y ETA de entrega.
- 0bb189a: soporte de stock decimal y venta por peso.
- 447bcb8: gestión de usuarios internos y autoservicio.
- b00b5f6: configuración de seguridad y propiedades tipadas JWT.

Referencia de historial de cambios: use el historial de Git del repositorio.

## 3. Arquitectura y configuración base

### 3.1 Seguridad y autenticación

Archivos clave:
- [SecurityConfig](../src/main/java/com/coplaca/apirest/config/SecurityConfig.java)
- [UserDetailsConfig](../src/main/java/com/coplaca/apirest/config/UserDetailsConfig.java)
- [JwtTokenProvider](../src/main/java/com/coplaca/apirest/security/JwtTokenProvider.java)
- [AppProperties](../src/main/java/com/coplaca/apirest/config/AppProperties.java)
- [application.properties](../src/main/resources/application.properties)

Cambio principal:
- Se consolidó autenticación stateless con JWT, reglas por endpoint y seguridad a nivel método.

Fragmento relevante explicado:

    .authorizeHttpRequests(authz -> authz
        .requestMatchers("/auth/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/products/**", "/offers/**", "/warehouses/**").permitAll()
        .anyRequest().authenticated()
    );

Qué aporta:
- Permite acceso público de catálogo.
- Obliga autenticación en operaciones de negocio sensibles.

### 3.2 Inicialización de datos de referencia

Archivo:
- [DataInitializer](../src/main/java/com/coplaca/apirest/config/DataInitializer.java)

Cambio principal:
- Alta automática de roles base, almacenes y usuario admin inicial si no existen.

Valor para empresa:
- Reduce tiempo de puesta en marcha de entornos de demo, QA y desarrollo.

## 4. Gestión de usuarios y roles

Archivos clave:
- [AuthController](../src/main/java/com/coplaca/apirest/controller/AuthController.java)
- [UserController](../src/main/java/com/coplaca/apirest/controller/UserController.java)
- [AdminController](../src/main/java/com/coplaca/apirest/controller/AdminController.java)
- [UserService](../src/main/java/com/coplaca/apirest/service/UserService.java)
- [UserRepository](../src/main/java/com/coplaca/apirest/repository/UserRepository.java)
- [User](../src/main/java/com/coplaca/apirest/entity/User.java)
- [DeliveryAgentStatus](../src/main/java/com/coplaca/apirest/entity/DeliveryAgentStatus.java)

Cambios funcionales:
- El alta pública se limita a clientes.
- Domicilio obligatorio para cliente y asignación automática de almacén.
- Endpoints de autoservicio para perfil y baja de cuenta.
- Gestión de usuarios internos por administrador.
- Estado operativo de repartidor (en almacén, repartiendo, fuera de línea).

Fragmento relevante explicado:

    if (requestedRole != null && !requestedRole.isBlank() && !"ROLE_CUSTOMER".equalsIgnoreCase(requestedRole)
            && !"CUSTOMER".equalsIgnoreCase(requestedRole)) {
        throw new IllegalArgumentException("Public signup is only available for customer accounts");
    }

Qué aporta:
- Evita escaladas de privilegio por alta pública.

## 5. Catálogo, stock y venta por kilo

Archivos clave:
- [Product](../src/main/java/com/coplaca/apirest/entity/Product.java)
- [OrderItem](../src/main/java/com/coplaca/apirest/entity/OrderItem.java)
- [ProductDTO](../src/main/java/com/coplaca/apirest/dto/ProductDTO.java)
- [OrderItemDTO](../src/main/java/com/coplaca/apirest/dto/OrderItemDTO.java)
- [ProductService](../src/main/java/com/coplaca/apirest/service/ProductService.java)
- [ProductServiceImpl](../src/main/java/com/coplaca/apirest/service/ProductServiceImpl.java)
- [ProductRepository](../src/main/java/com/coplaca/apirest/repository/ProductRepository.java)
- [ProductController](../src/main/java/com/coplaca/apirest/controller/ProductController.java)

Cambios funcionales:
- Stock y cantidades migradas a decimal para soportar kg.
- Ajuste de stock con protección para no bajar de cero.

Fragmento relevante explicado:

    BigDecimal newQty = product.getStockQuantity().add(quantityChange);
    product.setStockQuantity(newQty.max(BigDecimal.ZERO));

Qué aporta:
- Evita inconsistencias de inventario por operaciones concurrentes o ajustes negativos.

## 6. Pedidos, logística y reparto

Archivos clave:
- [OrderController](../src/main/java/com/coplaca/apirest/controller/OrderController.java)
- [OrderService](../src/main/java/com/coplaca/apirest/service/OrderService.java)
- [OrderRepository](../src/main/java/com/coplaca/apirest/repository/OrderRepository.java)
- [OrderStatus](../src/main/java/com/coplaca/apirest/entity/OrderStatus.java)
- [CreateOrderRequest](../src/main/java/com/coplaca/apirest/dto/CreateOrderRequest.java)
- [CreateOrderItemRequest](../src/main/java/com/coplaca/apirest/dto/CreateOrderItemRequest.java)

Cambios funcionales:
- Creación de pedido basada en usuario autenticado.
- Cálculo de subtotal, gastos de envío y total.
- Descuento de stock al confirmar líneas de pedido.
- Asignación de pedidos a repartidor por logística.
- Flujo de estados por rol con validaciones.
- ETA estimada por distancia y carga activa del repartidor.

Fragmento relevante explicado:

    if (newStatus == OrderStatus.DELIVERED && order.getStatus() == OrderStatus.IN_TRANSIT) {
        deliveryAgent.setDeliveryStatus(DeliveryAgentStatus.AT_WAREHOUSE);
        order.setStatus(OrderStatus.DELIVERED);
        order.setActualDeliveryTime(LocalDateTime.now());
        return;
    }

Qué aporta:
- Cierre operativo completo del ciclo de entrega y actualización del estado del repartidor.

## 7. Manejo de errores y respuestas

Archivo:
- [GlobalExceptionHandler](../src/main/java/com/coplaca/apirest/exception/GlobalExceptionHandler.java)

Cambios funcionales:
- Reglas de negocio inválidas devuelven 400.
- Accesos no autorizados devuelven 403.
- Recursos inexistentes devuelven 404.

Valor para empresa:
- Mayor claridad para frontend y soporte en diagnóstico de incidencias.

## 8. Pruebas unitarias incorporadas

Archivos de pruebas:
- [AuthControllerTest](../src/test/java/com/coplaca/apirest/controller/AuthControllerTest.java)
- [JwtTokenProviderTest](../src/test/java/com/coplaca/apirest/security/JwtTokenProviderTest.java)
- [OrderServiceTest](../src/test/java/com/coplaca/apirest/service/OrderServiceTest.java)
- [ProductServiceImplTest](../src/test/java/com/coplaca/apirest/service/ProductServiceImplTest.java)
- [UserServiceTest](../src/test/java/com/coplaca/apirest/service/UserServiceTest.java)
- [WarehouseServiceTest](../src/test/java/com/coplaca/apirest/service/WarehouseServiceTest.java)
- [DataInitializerTest](../src/test/java/com/coplaca/apirest/config/DataInitializerTest.java)
- [ApirestApplicationTests](../src/test/java/com/coplaca/apirest/ApirestApplicationTests.java)

Cobertura funcional de pruebas:
- Login y signup.
- Generación, parseo y validación de JWT.
- Validaciones de negocio en pedidos.
- Reglas de stock decimal.
- Reglas de creación de usuarios internos.
- Asignación de almacén y cálculos básicos de distancia.
- Inicialización de datos de referencia.

Ejecución:

    .\\mvnw.cmd -q test

Estado actual:
- Pruebas en verde.

## 9. Riesgos y siguientes pasos recomendados

Riesgos técnicos vigentes:
- Falta de pruebas de integración HTTP completas por endpoint.
- Falta de pruebas de persistencia con escenarios de concurrencia real.

Siguientes pasos propuestos:
- Añadir pruebas de integración con MockMvc para endpoints críticos.
- Incorporar cobertura de autorización por rol con casos positivos y negativos.
- Publicar contrato de API (OpenAPI) para alinear frontend y backend.

## 10. Conclusión

El backend está evolucionado a un estado más robusto para operación real de Coplaca:
- Seguridad y modelo de roles alineados con negocio.
- Flujo de pedido-logística-reparto implementado y validado.
- Soporte de venta por kilo y control de stock decimal.
- Base de pruebas unitarias estable para continuar el crecimiento del sistema.
