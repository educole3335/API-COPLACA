# Backend Coplaca - Mapa completo de archivos

## Objetivo de este documento

Este documento describe qué hace cada archivo del backend y resalta lo más importante para la empresa, para desarrollo y para mantenimiento.

## 1. Raíz del proyecto

- [pom.xml](../pom.xml): definición de dependencias, plugins y ciclo de build Maven.
  - Importante: concentra seguridad, persistencia, web y stack de pruebas.
- [mvnw](../mvnw): wrapper Maven para Linux/macOS.
- [mvnw.cmd](../mvnw.cmd): wrapper Maven para Windows.

## 2. Arranque de la aplicación

- [src/main/java/com/coplaca/apirest/ApirestApplication.java](../src/main/java/com/coplaca/apirest/ApirestApplication.java): punto de entrada Spring Boot.
  - Importante: sin este archivo no inicia el backend.

## 3. Configuración

- [src/main/java/com/coplaca/apirest/config/AppProperties.java](../src/main/java/com/coplaca/apirest/config/AppProperties.java): propiedades tipadas de configuración app (JWT).
  - Importante: evita hardcode y mejora seguridad de configuración.
- [src/main/java/com/coplaca/apirest/config/SecurityConfig.java](../src/main/java/com/coplaca/apirest/config/SecurityConfig.java): política de seguridad HTTP, filtros JWT, bean de autenticación y PasswordEncoder.
  - Importante: define qué endpoints son públicos y cuáles protegidos.
- [src/main/java/com/coplaca/apirest/config/UserDetailsConfig.java](../src/main/java/com/coplaca/apirest/config/UserDetailsConfig.java): adaptación de usuario de base de datos a UserDetails de Spring Security.
  - Importante: traduce roles de negocio a authorities de seguridad.
- [src/main/java/com/coplaca/apirest/config/CorsConfig.java](../src/main/java/com/coplaca/apirest/config/CorsConfig.java): reglas CORS para clientes Angular.
  - Importante: habilita consumo desde frontend en local.
- [src/main/java/com/coplaca/apirest/config/DataInitializer.java](../src/main/java/com/coplaca/apirest/config/DataInitializer.java): carga inicial de roles, almacenes y admin.
  - Importante: acelera puesta en marcha de entornos.

## 4. Seguridad JWT

- [src/main/java/com/coplaca/apirest/security/JwtTokenProvider.java](../src/main/java/com/coplaca/apirest/security/JwtTokenProvider.java): crea, valida y parsea tokens JWT.
  - Importante: núcleo de autenticación stateless.
- [src/main/java/com/coplaca/apirest/security/JwtAuthenticationFilter.java](../src/main/java/com/coplaca/apirest/security/JwtAuthenticationFilter.java): filtro por request para extraer token Bearer y poblar SecurityContext.
  - Importante: habilita autorización por usuario autenticado en cada request.

## 5. Controladores REST

- [src/main/java/com/coplaca/apirest/controller/AuthController.java](../src/main/java/com/coplaca/apirest/controller/AuthController.java): login y signup de cliente.
  - Importante: restringe alta pública a clientes y exige domicilio.
- [src/main/java/com/coplaca/apirest/controller/UserController.java](../src/main/java/com/coplaca/apirest/controller/UserController.java): perfil propio, baja propia y operaciones admin sobre usuarios.
  - Importante: separa autoservicio de operaciones administrativas.
- [src/main/java/com/coplaca/apirest/controller/AdminController.java](../src/main/java/com/coplaca/apirest/controller/AdminController.java): gestión interna de usuarios y estadísticas de productos top.
  - Importante: endpoint de gobierno operativo.
- [src/main/java/com/coplaca/apirest/controller/ProductController.java](../src/main/java/com/coplaca/apirest/controller/ProductController.java): catálogo público y operaciones de logística/admin sobre producto.
  - Importante: soporte de stock por cantidades decimales.
- [src/main/java/com/coplaca/apirest/controller/SeasonalOfferController.java](../src/main/java/com/coplaca/apirest/controller/SeasonalOfferController.java): CRUD de ofertas estacionales.
  - Importante: habilita promociones por razón de negocio.
- [src/main/java/com/coplaca/apirest/controller/WarehouseController.java](../src/main/java/com/coplaca/apirest/controller/WarehouseController.java): consulta/gestión de almacenes y repartidores disponibles.
  - Importante: da visibilidad logística por almacén.
- [src/main/java/com/coplaca/apirest/controller/OrderController.java](../src/main/java/com/coplaca/apirest/controller/OrderController.java): creación y ciclo de vida de pedidos por rol.
  - Importante: concentra el flujo cliente-logística-reparto.

## 6. Servicios de negocio

- [src/main/java/com/coplaca/apirest/service/UserService.java](../src/main/java/com/coplaca/apirest/service/UserService.java): reglas de usuario, roles, estados de repartidor y asignación de almacén para cliente.
  - Importante: reglas de identidad y operación interna.
- [src/main/java/com/coplaca/apirest/service/WarehouseService.java](../src/main/java/com/coplaca/apirest/service/WarehouseService.java): búsqueda de almacén más cercano y cálculo de distancia.
  - Importante: impacta ETA y asignación automática.
- [src/main/java/com/coplaca/apirest/service/ProductService.java](../src/main/java/com/coplaca/apirest/service/ProductService.java): contrato de operaciones de producto.
- [src/main/java/com/coplaca/apirest/service/ProductServiceImpl.java](../src/main/java/com/coplaca/apirest/service/ProductServiceImpl.java): implementación de catálogo y stock decimal.
  - Importante: protección para no dejar stock negativo.
- [src/main/java/com/coplaca/apirest/service/SeasonalOfferService.java](../src/main/java/com/coplaca/apirest/service/SeasonalOfferService.java): contrato de ofertas.
- [src/main/java/com/coplaca/apirest/service/SeasonalOfferServiceImpl.java](../src/main/java/com/coplaca/apirest/service/SeasonalOfferServiceImpl.java): implementación CRUD de ofertas activas.
- [src/main/java/com/coplaca/apirest/service/OrderService.java](../src/main/java/com/coplaca/apirest/service/OrderService.java): reglas de pedido, validaciones por rol, cálculo de total y ETA, transición de estados de reparto.
  - Importante: servicio más crítico del backend.

## 7. Repositorios (acceso a datos)

- [src/main/java/com/coplaca/apirest/repository/UserRepository.java](../src/main/java/com/coplaca/apirest/repository/UserRepository.java): consultas de usuarios por email, almacén, estado y rol.
- [src/main/java/com/coplaca/apirest/repository/WarehouseRepository.java](../src/main/java/com/coplaca/apirest/repository/WarehouseRepository.java): consultas de almacenes activos y fallback.
- [src/main/java/com/coplaca/apirest/repository/ProductRepository.java](../src/main/java/com/coplaca/apirest/repository/ProductRepository.java): consultas de catálogo por estado, categoría y búsqueda.
- [src/main/java/com/coplaca/apirest/repository/SeasonalOfferRepository.java](../src/main/java/com/coplaca/apirest/repository/SeasonalOfferRepository.java): consultas de ofertas activas y por producto.
- [src/main/java/com/coplaca/apirest/repository/RoleRepository.java](../src/main/java/com/coplaca/apirest/repository/RoleRepository.java): consulta de rol por nombre.
- [src/main/java/com/coplaca/apirest/repository/OrderRepository.java](../src/main/java/com/coplaca/apirest/repository/OrderRepository.java): consultas de pedidos por cliente, almacén, repartidor y estado.
  - Importante: soporte de planificación logística.
- [src/main/java/com/coplaca/apirest/repository/OrderItemRepository.java](../src/main/java/com/coplaca/apirest/repository/OrderItemRepository.java): líneas de pedido por pedido.
- [src/main/java/com/coplaca/apirest/repository/AddressRepository.java](../src/main/java/com/coplaca/apirest/repository/AddressRepository.java): persistencia de direcciones.
- [src/main/java/com/coplaca/apirest/repository/ProductCategoryRepository.java](../src/main/java/com/coplaca/apirest/repository/ProductCategoryRepository.java): categorías de producto activas y por nombre.

## 8. Entidades del dominio

- [src/main/java/com/coplaca/apirest/entity/User.java](../src/main/java/com/coplaca/apirest/entity/User.java): usuario del sistema (cliente o interno), roles, estado y almacén.
  - Importante: entidad transversal de seguridad y operación.
- [src/main/java/com/coplaca/apirest/entity/Role.java](../src/main/java/com/coplaca/apirest/entity/Role.java): rol de seguridad (CUSTOMER, LOGISTICS, DELIVERY, ADMIN).
- [src/main/java/com/coplaca/apirest/entity/Address.java](../src/main/java/com/coplaca/apirest/entity/Address.java): domicilio y coordenadas geográficas.
  - Importante: base del cálculo de proximidad y ETA.
- [src/main/java/com/coplaca/apirest/entity/Warehouse.java](../src/main/java/com/coplaca/apirest/entity/Warehouse.java): almacén logístico y datos de localización.
- [src/main/java/com/coplaca/apirest/entity/ProductCategory.java](../src/main/java/com/coplaca/apirest/entity/ProductCategory.java): categoría comercial del catálogo.
- [src/main/java/com/coplaca/apirest/entity/Product.java](../src/main/java/com/coplaca/apirest/entity/Product.java): producto, precio y stock decimal.
  - Importante: permite venta por kilo.
- [src/main/java/com/coplaca/apirest/entity/SeasonalOffer.java](../src/main/java/com/coplaca/apirest/entity/SeasonalOffer.java): oferta temporal y motivo comercial.
- [src/main/java/com/coplaca/apirest/entity/Order.java](../src/main/java/com/coplaca/apirest/entity/Order.java): cabecera de pedido con pagos, tiempos y asignaciones.
  - Importante: estado operativo del pedido.
- [src/main/java/com/coplaca/apirest/entity/OrderItem.java](../src/main/java/com/coplaca/apirest/entity/OrderItem.java): líneas de pedido con cantidad decimal y subtotal.
- [src/main/java/com/coplaca/apirest/entity/OrderStatus.java](../src/main/java/com/coplaca/apirest/entity/OrderStatus.java): estados del flujo de pedido.
- [src/main/java/com/coplaca/apirest/entity/DeliveryAgentStatus.java](../src/main/java/com/coplaca/apirest/entity/DeliveryAgentStatus.java): estados operativos de repartidor.

## 9. DTOs de entrada y salida

- [src/main/java/com/coplaca/apirest/dto/LoginRequest.java](../src/main/java/com/coplaca/apirest/dto/LoginRequest.java): credenciales de login.
- [src/main/java/com/coplaca/apirest/dto/LoginResponse.java](../src/main/java/com/coplaca/apirest/dto/LoginResponse.java): respuesta con token y datos mínimos de sesión.
- [src/main/java/com/coplaca/apirest/dto/SignUpRequest.java](../src/main/java/com/coplaca/apirest/dto/SignUpRequest.java): alta de usuario.
- [src/main/java/com/coplaca/apirest/dto/UserDTO.java](../src/main/java/com/coplaca/apirest/dto/UserDTO.java): proyección pública de usuario.
- [src/main/java/com/coplaca/apirest/dto/AddressDTO.java](../src/main/java/com/coplaca/apirest/dto/AddressDTO.java): proyección de dirección.
- [src/main/java/com/coplaca/apirest/dto/ProductDTO.java](../src/main/java/com/coplaca/apirest/dto/ProductDTO.java): proyección de producto para catálogo y oferta.
- [src/main/java/com/coplaca/apirest/dto/SeasonalOfferDTO.java](../src/main/java/com/coplaca/apirest/dto/SeasonalOfferDTO.java): proyección de oferta activa.
- [src/main/java/com/coplaca/apirest/dto/OrderDTO.java](../src/main/java/com/coplaca/apirest/dto/OrderDTO.java): proyección completa de pedido.
- [src/main/java/com/coplaca/apirest/dto/OrderItemDTO.java](../src/main/java/com/coplaca/apirest/dto/OrderItemDTO.java): proyección de línea de pedido.
- [src/main/java/com/coplaca/apirest/dto/CreateOrderRequest.java](../src/main/java/com/coplaca/apirest/dto/CreateOrderRequest.java): payload de creación de pedido.
- [src/main/java/com/coplaca/apirest/dto/CreateOrderItemRequest.java](../src/main/java/com/coplaca/apirest/dto/CreateOrderItemRequest.java): línea de creación de pedido.

## 10. Excepciones

- [src/main/java/com/coplaca/apirest/exception/ResourceNotFoundException.java](../src/main/java/com/coplaca/apirest/exception/ResourceNotFoundException.java): excepción de recurso inexistente.
- [src/main/java/com/coplaca/apirest/exception/GlobalExceptionHandler.java](../src/main/java/com/coplaca/apirest/exception/GlobalExceptionHandler.java): traducción de excepciones a respuestas HTTP homogéneas.
  - Importante: estandariza 400, 403, 404 y 500.

## 11. Recursos de configuración

- [src/main/resources/application.properties](../src/main/resources/application.properties): configuración de datasource, JPA, H2, JWT y parámetros de app.
  - Importante: fichero central de comportamiento por entorno.
- [src/main/resources/META-INF/additional-spring-configuration-metadata.json](../src/main/resources/META-INF/additional-spring-configuration-metadata.json): metadata de propiedades custom para tooling.

## 12. Pruebas del backend

- [src/test/java/com/coplaca/apirest/ApirestApplicationTests.java](../src/test/java/com/coplaca/apirest/ApirestApplicationTests.java): verificación de carga de contexto Spring.
- [src/test/java/com/coplaca/apirest/controller/AuthControllerTest.java](../src/test/java/com/coplaca/apirest/controller/AuthControllerTest.java): pruebas unitarias de login/signup.
- [src/test/java/com/coplaca/apirest/security/JwtTokenProviderTest.java](../src/test/java/com/coplaca/apirest/security/JwtTokenProviderTest.java): pruebas unitarias de token JWT.
- [src/test/java/com/coplaca/apirest/service/WarehouseServiceTest.java](../src/test/java/com/coplaca/apirest/service/WarehouseServiceTest.java): pruebas de asignación/proximidad de almacenes.
- [src/test/java/com/coplaca/apirest/service/ProductServiceImplTest.java](../src/test/java/com/coplaca/apirest/service/ProductServiceImplTest.java): pruebas de stock y proyección de oferta.
- [src/test/java/com/coplaca/apirest/service/UserServiceTest.java](../src/test/java/com/coplaca/apirest/service/UserServiceTest.java): pruebas de reglas de usuarios internos y estado repartidor.
- [src/test/java/com/coplaca/apirest/service/OrderServiceTest.java](../src/test/java/com/coplaca/apirest/service/OrderServiceTest.java): pruebas de cálculo de pedido y transiciones.
- [src/test/java/com/coplaca/apirest/config/DataInitializerTest.java](../src/test/java/com/coplaca/apirest/config/DataInitializerTest.java): pruebas de inicialización de datos base.

## 13. Lo más importante para la empresa

- Seguridad de acceso por rol y token JWT en producción.
- Flujo de pedido con validaciones de negocio por actor.
- Stock decimal para venta por peso en tienda.
- Asignación de almacén y ETA para experiencia de entrega.
- Base de tests unitarios para reducir incidencias en futuras entregas.
