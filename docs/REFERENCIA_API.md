# Referencia API Backend COPLACA

Referencia operativa de endpoints expuestos por el backend con rutas actuales, objetivo funcional y acceso por rol.

## Contexto general

- Base URL local: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- Autenticacion: JWT Bearer en cabecera Authorization

## Convencion de rutas

- Autenticacion y landing sin prefijo funcional:
  - /auth/**
  - /landing/**
- Recursos de dominio con prefijo:
  - /api/v1/**

## Comportamiento real de acceso

- Importante: la configuracion de seguridad global permite GET publico para /products/**, /offers/** y /warehouses/** sin prefijo.
- Como los controladores actuales usan /api/v1/**, en la practica los endpoints GET de /api/v1 tambien requieren autenticacion salvo que tengan permiso explicito.
- En esta referencia se documenta el acceso efectivo actual, no el comportamiento esperado historicamente.

## 1. Auth

### POST /auth/login

- Acceso: publico
- Uso: autenticacion de usuario

### POST /auth/signup

- Acceso: publico
- Uso: registro publico de cliente
- Regla: requiere direccion de entrega

## 2. Landing

### GET /landing

- Acceso: publico
- Uso: contenido general de portada

### GET /landing/seasonal

- Acceso: publico
- Uso: coleccion estacional

### GET /landing/recommendations

- Acceso: publico
- Uso: recomendaciones de productos

### GET /landing/health

- Acceso: publico
- Uso: verificacion de estado basico

## 3. Productos

Base: /api/v1/products

### GET /

- Acceso: autenticado (configuracion actual)
- Uso: lista de productos activos

### GET /{id}

- Acceso: autenticado (configuracion actual)
- Uso: detalle de producto

### GET /category/{categoryId}

- Acceso: autenticado (configuracion actual)
- Uso: productos por categoria

### GET /search?query=texto

- Acceso: autenticado (configuracion actual)
- Uso: busqueda textual

### POST /

- Acceso: LOGISTICS o ADMIN
- Uso: alta de producto

### PUT /{id}

- Acceso: LOGISTICS o ADMIN
- Uso: actualizacion de producto

### DELETE /{id}

- Acceso: LOGISTICS o ADMIN
- Uso: desactivacion funcional

### PATCH /{id}/stock?delta=valor

- Acceso: LOGISTICS o ADMIN
- Uso: ajuste incremental de stock

### PATCH /{id}/price?value=valor

- Acceso: LOGISTICS o ADMIN
- Uso: ajuste de precio

## 4. Categorias

Base: /api/v1/categories

### GET /

- Acceso: autenticado (configuracion actual)
- Uso: lista de categorias

### GET /{id}

- Acceso: autenticado (configuracion actual)
- Uso: detalle de categoria

### POST /

- Acceso: LOGISTICS o ADMIN
- Uso: crear categoria

### PUT /{id}

- Acceso: LOGISTICS o ADMIN
- Uso: actualizar categoria

### DELETE /{id}

- Acceso: ADMIN
- Uso: eliminar categoria

## 5. Ofertas estacionales

Base: /api/v1/offers

### GET /

- Acceso: autenticado (configuracion actual)
- Uso: listar ofertas activas

### GET /{id}

- Acceso: autenticado (configuracion actual)
- Uso: detalle de oferta

### POST /

- Acceso: LOGISTICS o ADMIN
- Uso: crear oferta

### PUT /{id}

- Acceso: LOGISTICS o ADMIN
- Uso: editar oferta

### DELETE /{id}

- Acceso: LOGISTICS o ADMIN
- Uso: desactivar oferta

## 6. Almacenes

Base: /api/v1/warehouses

### GET /

- Acceso: autenticado (configuracion actual)
- Uso: listar almacenes

### GET /{id}

- Acceso: autenticado (configuracion actual)
- Uso: detalle de almacen

### GET /{id}/delivery-agents

- Acceso: LOGISTICS o ADMIN
- Uso: listar repartidores por almacen

### POST /

- Acceso: ADMIN
- Uso: crear almacen

### PUT /{id}

- Acceso: ADMIN
- Uso: actualizar almacen

## 7. Usuarios

Base: /api/v1/users

### GET /me

- Acceso: autenticado
- Uso: perfil propio

### PUT /me

- Acceso: autenticado
- Uso: actualizar perfil propio

### DELETE /me

- Acceso: autenticado
- Uso: desactivar cuenta propia

### PATCH /me/delivery-status?status=AT_WAREHOUSE|DELIVERING|OFFLINE

- Acceso: DELIVERY
- Uso: actualizar estado operativo de reparto

### GET /{id}

- Acceso: ADMIN
- Uso: detalle de usuario

### PUT /{id}

- Acceso: ADMIN
- Uso: actualizacion administrativa

### DELETE /{id}

- Acceso: ADMIN
- Uso: desactivar usuario

## 8. Pedidos

Base: /api/v1/orders

### POST /

- Acceso: CUSTOMER
- Uso: crear pedido

### GET /me

- Acceso: CUSTOMER o DELIVERY
- Uso: pedidos del usuario autenticado

### GET /{id}

- Acceso: autenticado
- Uso: detalle de pedido con control interno

### GET /{id}/eta

- Acceso: autenticado
- Uso: obtener informacion ETA asociada al pedido

### GET /customer/{customerId}

- Acceso: CUSTOMER o ADMIN
- Uso: pedidos por cliente

### GET /warehouse/{warehouseId}/pending

- Acceso: LOGISTICS o ADMIN
- Uso: pedidos pendientes

### GET /warehouse/{warehouseId}/all

- Acceso: LOGISTICS o ADMIN
- Uso: pedidos del almacen

### GET /warehouse/{warehouseId}/confirmed

- Acceso: LOGISTICS o ADMIN
- Uso: pedidos confirmados

### GET /warehouse/{warehouseId}/in-transit

- Acceso: LOGISTICS o ADMIN
- Uso: pedidos en transito

### GET /warehouse/{warehouseId}/stats?period=day|week|month

- Acceso: LOGISTICS o ADMIN
- Uso: estadisticas operativas por ventana temporal

### GET /delivery-agent/{deliveryAgentId}

- Acceso: DELIVERY, LOGISTICS o ADMIN
- Uso: pedidos de un repartidor

### PUT /{orderId}/assign/{deliveryAgentId}

- Acceso: LOGISTICS o ADMIN
- Uso: asignar pedido a reparto

### PUT /{orderId}/status?status=valor

- Acceso: DELIVERY, LOGISTICS o ADMIN
- Uso: cambiar estado de pedido

### PUT /{orderId}/accept

- Acceso: DELIVERY
- Uso: aceptar pedido asignado

### PUT /{orderId}/reject?reason=texto

- Acceso: DELIVERY
- Uso: rechazar pedido asignado

### PUT /{orderId}/confirm-loaded

- Acceso: DELIVERY
- Uso: confirmar carga para salida

### PUT /{orderId}/deliver

- Acceso: DELIVERY
- Uso: marcar entrega

### PUT /{orderId}/cancel?reason=texto

- Acceso: CUSTOMER o ADMIN
- Uso: cancelar pedido segun reglas de dominio

## 9. ETA

Base: /api/v1/eta

### GET /order/{orderId}/calculate

- Acceso: autenticado
- Uso: calcular ETA actual

### GET /order/{orderId}

- Acceso: autenticado
- Uso: recuperar ultimo ETA calculado

### POST /delivery-agent/{deliveryAgentId}/recalculate

- Acceso: DELIVERY, LOGISTICS o ADMIN
- Uso: recalculo de ETA para pedidos de un repartidor

## 10. Administracion

Base: /api/v1/admin

### GET /users

- Acceso: ADMIN
- Uso: listado de usuarios

### GET /users/active

- Acceso: ADMIN
- Uso: usuarios activos

### GET /users/disabled

- Acceso: ADMIN
- Uso: usuarios deshabilitados

### PUT /users/{id}/roles

- Acceso: ADMIN
- Uso: reasignar roles

### POST /users/internal

- Acceso: ADMIN
- Uso: crear usuario interno

### POST /users/{id}/reactivate

- Acceso: ADMIN
- Uso: reactivar usuario

### DELETE /users/{id}

- Acceso: ADMIN
- Uso: desactivar usuario

### GET /stats/top-products

- Acceso: ADMIN
- Uso: ranking de productos

### GET /stats/products-detailed

- Acceso: ADMIN
- Uso: analitica detallada de productos

### GET /stats/orders

- Acceso: ADMIN
- Uso: resumen de pedidos

### GET /stats/users

- Acceso: ADMIN
- Uso: resumen de usuarios

### GET /orders/today

- Acceso: ADMIN
- Uso: pedidos del dia

### GET /health

- Acceso: ADMIN
- Uso: health interno de capa admin

## 11. Reglas de dominio

- Catalogo orientado a frutas y hortalizas.
- Soporte de cantidades decimales para productos por kilo.
- Registro publico restringido a perfil cliente.
- Operacion logistica separada por roles DELIVERY y LOGISTICS.

## 12. Mantenimiento de contrato

Cada cambio en endpoints debe actualizar en el mismo ciclo:

1. Este documento.
2. docs/contracts/v1/openapi.yaml.
3. docs/contracts/v1/coplaca-api-v1.postman_collection.json.

Fecha de actualizacion: Marzo 2026
