# Flujos completos de la API (ruta por ruta y clase responsable)

Documento de referencia para entender todos los flujos de API COPLACA.

## 1. Regla general de ejecucion

Flujo base que se repite en la mayoria de endpoints:

1. Peticion HTTP entra al controller (`@RestController`).
2. Spring Security valida JWT y permisos (global + `@PreAuthorize`).
3. Controller delega en service/repository.
4. Service aplica reglas de negocio, usa repository y mappers.
5. Controller devuelve `SuccessResponse` con `ResponseHelper`.
6. Si hay error, `GlobalExceptionHandler` genera `ErrorResponse`.

## 2. Seguridad transversal

- Publicas por configuracion global: `/auth/**`, `/v3/api-docs/**`, `/swagger-ui/**`, `GET /products/**`, `GET /offers/**`, `GET /warehouses/**`, `/landing/**`.
- El resto de rutas requiere autenticacion.
- Ademas, muchos endpoints aplican autorizacion por rol con `@PreAuthorize`.

## 3. Flujos por controlador y rutas

## 3.1 AuthController

Clase: `com.coplaca.apirest.controller.AuthController`

| Metodo y ruta | Metodo en clase | Flujo interno (clases) | Seguridad |
|---|---|---|---|
| `POST /auth/login` | `AuthController#login` | `AuthenticationManager` -> `UserService` -> `JwtTokenProvider` -> `LoginResponse` | Publico |
| `POST /auth/signup` | `AuthController#signup` | `UserService` + `RoleRepository` + `User`/`Address` -> `JwtTokenProvider` -> `LoginResponse` | Publico |

## 3.2 ProductController

Clase: `com.coplaca.apirest.controller.ProductController`

Base path: `/api/v1/products`

| Metodo y ruta | Metodo en clase | Flujo interno (clases) | Seguridad |
|---|---|---|---|
| `GET /api/v1/products` | `getAllActiveProducts` | `ProductController` -> `ProductService#getAllActiveProducts` -> `ResponseHelper.ok` | Publico |
| `GET /api/v1/products/{id}` | `getProductById` | `ProductService#getProductById` | Publico |
| `GET /api/v1/products/category/{categoryId}` | `getProductsByCategory` | `ProductService#getProductsByCategory` | Publico |
| `GET /api/v1/products/search?query=...` | `searchProducts` | `ProductService#searchProducts` | Publico |
| `POST /api/v1/products` | `createProduct` | `ProductService#createProduct` -> `ProductService#getProductById` | LOGISTICS, ADMIN |
| `PUT /api/v1/products/{id}` | `updateProduct` | `ProductService#updateProduct` -> `ProductService#getProductById` | LOGISTICS, ADMIN |
| `DELETE /api/v1/products/{id}` | `disableProduct` | `ProductService#disableProduct` | LOGISTICS, ADMIN |
| `PATCH /api/v1/products/{id}/stock?delta=...` | `adjustStock` | `ProductService#adjustStock` | LOGISTICS, ADMIN |
| `PATCH /api/v1/products/{id}/price?value=...` | `adjustPrice` | `ProductService#adjustPrice` | LOGISTICS, ADMIN |

## 3.3 CategoryController

Clase: `com.coplaca.apirest.controller.CategoryController`

Base path: `/api/v1/categories`

Nota: este controller usa repositorios directamente (sin service intermedio).

| Metodo y ruta | Metodo en clase | Flujo interno (clases) | Seguridad |
|---|---|---|---|
| `GET /api/v1/categories` | `getAllCategories` | `ProductCategoryRepository#findAll` + `ProductRepository` (conteo) | Publico |
| `GET /api/v1/categories/{id}` | `getCategoryById` | `ProductCategoryRepository#findById` + mapeo DTO | Publico |
| `POST /api/v1/categories` | `createCategory` | `ProductCategoryRepository#save` | LOGISTICS, ADMIN |
| `PUT /api/v1/categories/{id}` | `updateCategory` | `ProductCategoryRepository#findById` -> `save` | LOGISTICS, ADMIN |
| `DELETE /api/v1/categories/{id}` | `deleteCategory` | `ProductCategoryRepository#existsById` -> `deleteById` | ADMIN |

## 3.4 SeasonalOfferController

Clase: `com.coplaca.apirest.controller.SeasonalOfferController`

Base path: `/api/v1/offers`

| Metodo y ruta | Metodo en clase | Flujo interno (clases) | Seguridad |
|---|---|---|---|
| `GET /api/v1/offers` | `getAllOffers` | `SeasonalOfferService#getAllActiveOffers` | Publico |
| `GET /api/v1/offers/{id}` | `getOffer` | `SeasonalOfferService#getOfferById` | Publico |
| `POST /api/v1/offers` | `createOffer` | `SeasonalOfferService#createOffer` | LOGISTICS, ADMIN |
| `PUT /api/v1/offers/{id}` | `updateOffer` | `SeasonalOfferService#updateOffer` | LOGISTICS, ADMIN |
| `DELETE /api/v1/offers/{id}` | `deactivateOffer` | `SeasonalOfferService#deactivateOffer` | LOGISTICS, ADMIN |

## 3.5 WarehouseController

Clase: `com.coplaca.apirest.controller.WarehouseController`

Base path: `/api/v1/warehouses`

| Metodo y ruta | Metodo en clase | Flujo interno (clases) | Seguridad |
|---|---|---|---|
| `GET /api/v1/warehouses` | `getAll` | `WarehouseService#getAllWarehouses` | Publico |
| `GET /api/v1/warehouses/{id}` | `getById` | `WarehouseService#getWarehouseById` | Publico |
| `GET /api/v1/warehouses/{id}/delivery-agents` | `getAvailableDeliveryAgents` | `UserService#getAvailableDeliveryAgents` | LOGISTICS, ADMIN |
| `POST /api/v1/warehouses` | `create` | `WarehouseService#createWarehouse` | ADMIN |
| `PUT /api/v1/warehouses/{id}` | `update` | `WarehouseService#updateWarehouse` | ADMIN |

## 3.6 OrderController

Clase: `com.coplaca.apirest.controller.OrderController`

Base path: `/api/v1/orders`

| Metodo y ruta | Metodo en clase | Flujo interno (clases) | Seguridad |
|---|---|---|---|
| `POST /api/v1/orders` | `createOrder` | `OrderService#createOrder` -> `OrderRepository` + `OrderMapper` | CUSTOMER |
| `GET /api/v1/orders/me` | `getMyOrders` | `OrderService#getCurrentUserOrders` | CUSTOMER, DELIVERY |
| `GET /api/v1/orders/{id}` | `getOrder` | `OrderService#getOrderById` | Autenticado |
| `GET /api/v1/orders/{id}/eta` | `getOrderETA` | `OrderService#getOrderById` (reusa DTO con ETA) | Autenticado |
| `GET /api/v1/orders/customer/{customerId}` | `getCustomerOrders` | `OrderService#getOrdersByCustomer` | CUSTOMER, ADMIN |
| `GET /api/v1/orders/warehouse/{warehouseId}/pending` | `getWarehousePendingOrders` | `OrderService#getPendingOrdersByWarehouse` | LOGISTICS, ADMIN |
| `GET /api/v1/orders/warehouse/{warehouseId}/all` | `getWarehouseAllOrders` | `OrderService#getAllOrdersByWarehouse` | LOGISTICS, ADMIN |
| `GET /api/v1/orders/warehouse/{warehouseId}/confirmed` | `getWarehouseConfirmedOrders` | `OrderService#getConfirmedOrdersByWarehouse` | LOGISTICS, ADMIN |
| `GET /api/v1/orders/warehouse/{warehouseId}/in-transit` | `getWarehouseInTransitOrders` | `OrderService#getInTransitOrdersByWarehouse` | LOGISTICS, ADMIN |
| `GET /api/v1/orders/warehouse/{warehouseId}/stats?period=...` | `getWarehouseStats` | `OrderService#getWarehouseStats` | LOGISTICS, ADMIN |
| `PUT /api/v1/orders/{orderId}/assign/{deliveryAgentId}` | `assignOrderToDeliveryAgent` | `OrderService#assignOrderToDeliveryAgent` | LOGISTICS, ADMIN |
| `PUT /api/v1/orders/{orderId}/status?status=...` | `updateOrderStatus` | `OrderService#updateOrderStatus` | DELIVERY, LOGISTICS, ADMIN |
| `GET /api/v1/orders/delivery-agent/{deliveryAgentId}` | `getDeliveryAgentOrders` | `OrderService#getOrdersByDeliveryAgent` | DELIVERY, LOGISTICS, ADMIN |
| `PUT /api/v1/orders/{orderId}/accept` | `acceptOrder` | `OrderService#acceptOrder` | DELIVERY |
| `PUT /api/v1/orders/{orderId}/reject?reason=...` | `rejectOrder` | `OrderService#rejectOrder` | DELIVERY |
| `PUT /api/v1/orders/{orderId}/confirm-loaded` | `confirmOrderLoaded` | `OrderService#confirmOrderLoaded` | DELIVERY |
| `PUT /api/v1/orders/{orderId}/deliver` | `deliverOrder` | `OrderService#deliverOrder` | DELIVERY |
| `PUT /api/v1/orders/{orderId}/cancel?reason=...` | `cancelOrder` | `OrderService#cancelOrder` | CUSTOMER, ADMIN |

## 3.7 UserController

Clase: `com.coplaca.apirest.controller.UserController`

Base path: `/api/v1/users`

| Metodo y ruta | Metodo en clase | Flujo interno (clases) | Seguridad |
|---|---|---|---|
| `GET /api/v1/users/me` | `getCurrentUser` | `UserService#getCurrentUser` | Autenticado |
| `PUT /api/v1/users/me` | `updateCurrentUser` | `UserService#updateCurrentUser` | Autenticado |
| `DELETE /api/v1/users/me` | `deleteCurrentUser` | `UserService#disableCurrentUser` | Autenticado |
| `PATCH /api/v1/users/me/delivery-status?status=...` | `updateDeliveryStatus` | `UserService#updateDeliveryStatus` | DELIVERY |
| `GET /api/v1/users/{id}` | `getUser` | `UserService#getUserById` | ADMIN |
| `PUT /api/v1/users/{id}` | `updateUser` | `UserService#updateUser` | ADMIN |
| `DELETE /api/v1/users/{id}` | `deleteUser` | `UserService#disableUser` | ADMIN |

## 3.8 AdminController

Clase: `com.coplaca.apirest.controller.AdminController`

Base path: `/api/v1/admin`

| Metodo y ruta | Metodo en clase | Flujo interno (clases) | Seguridad |
|---|---|---|---|
| `GET /api/v1/admin/users` | `listUsers` | `UserService#getAllUsers` | ADMIN |
| `GET /api/v1/admin/users/active` | `getActiveUsers` | `UserService#getAllUsers` + filtro enabled | ADMIN |
| `GET /api/v1/admin/users/disabled` | `getDisabledUsers` | `UserService#getAllUsers` + filtro disabled | ADMIN |
| `PUT /api/v1/admin/users/{id}/roles` | `changeRoles` | `UserService#changeRoles` | ADMIN |
| `POST /api/v1/admin/users/internal` | `createInternalUser` | `UserService#createManagedUser` -> `UserService#getUserById` | ADMIN |
| `POST /api/v1/admin/users/{id}/reactivate` | `reactivateUser` | `UserService#reactivateUser` | ADMIN |
| `DELETE /api/v1/admin/users/{id}` | `disableUser` | `OrderService#validateUserCanBeDisabled` -> `UserService#disableUser` | ADMIN |
| `GET /api/v1/admin/stats/top-products` | `topProductsLastMonth` | `OrderService#getTopProductsDetailedSince` | ADMIN |
| `GET /api/v1/admin/stats/products-detailed` | `detailedProductStats` | `OrderService#getTopProductsSince` | ADMIN |
| `GET /api/v1/admin/stats/orders?period=...` | `orderStats` | `OrderService#getOrderStatsSince` | ADMIN |
| `GET /api/v1/admin/stats/users` | `userStats` | `UserService#getAllUsers` + agregacion por roles | ADMIN |
| `GET /api/v1/admin/orders/today` | `ordersToday` | `OrderService#getOrdersToday` | ADMIN |
| `GET /api/v1/admin/health` | `healthCheck` | `UserService#getAllUsers` (comprobacion DB) | ADMIN |

## 3.9 LandingPageController

Clase: `com.coplaca.apirest.controller.LandingPageController`

Base path: `/landing`

| Metodo y ruta | Metodo en clase | Flujo interno (clases) | Seguridad |
|---|---|---|---|
| `GET /landing` | `getLandingPage` | `RecommendationService#generateLandingPageContent` | Publico (si hay auth usa usuario, si no anonymous) |
| `GET /landing/seasonal` | `getSeasonalProducts` | `RecommendationService#getSeasonalProducts` | Publico |
| `GET /landing/recommendations` | `getPersonalizedRecommendations` | `RecommendationService#getRecommendations` | Publico (mejora con usuario autenticado) |
| `GET /landing/health` | `healthCheck` | Respuesta directa desde controller | Publico |

## 3.10 ETAController

Clase: `com.coplaca.apirest.controller.ETAController`

Base path: `/api/v1/eta`

| Metodo y ruta | Metodo en clase | Flujo interno (clases) | Seguridad |
|---|---|---|---|
| `GET /api/v1/eta/order/{orderId}/calculate` | `calculateETA` | `ETAService#calculateETA` | Autenticado |
| `GET /api/v1/eta/order/{orderId}` | `getLatestETA` | `ETAService#getLatestETA` | Autenticado |
| `POST /api/v1/eta/delivery-agent/{deliveryAgentId}/recalculate` | `recalculateDeliveryAgentETAs` | `ETAService#recalculateETAForDeliveryAgent` | DELIVERY, LOGISTICS, ADMIN |

## 4. Flujo detallado de referencia (pedido)

Ejemplo completo de negocio: `POST /api/v1/orders`

1. `OrderController#createOrder` recibe `CreateOrderRequest`.
2. `@PreAuthorize("hasRole('CUSTOMER')")` valida rol.
3. `OrderService#createOrder` valida usuario activo y datos del pedido.
4. `OrderService` consulta entidades relacionadas (usuario, direccion, almacen, productos).
5. Calcula subtotal, tarifa de envio, total y ETA.
6. Persiste con `OrderRepository#save`.
7. Convierte `Order` a `OrderDTO` con `OrderMapper`.
8. Responde `201` con `ResponseHelper.created` y `SuccessResponse<OrderDTO>`.
9. Cualquier error funcional termina en `GlobalExceptionHandler` con respuesta de error normalizada.

## 5. Como crear un flujo nuevo correctamente

1. Crear/actualizar `Entity` en el dominio.
2. Crear `Repository` con consultas necesarias.
3. Definir DTO request/response.
4. Crear mapper (`MapStruct`) si aplica.
5. Implementar reglas en `Service`.
6. Exponer ruta en `Controller`.
7. Proteger con seguridad global + `@PreAuthorize`.
8. Devolver respuestas consistentes con `ResponseHelper`.
9. Documentar ruta y flujo en este archivo y en OpenAPI.
