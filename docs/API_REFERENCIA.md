# API Referencia Completa - COPLACA

Referencia funcional y tecnica de endpoints del backend.

Base URL local:

- http://localhost:8080

Autenticacion:

- JWT Bearer en cabecera Authorization.
- Formato: Authorization: Bearer <token>

---

## 1) Auth

### POST /auth/login

- Acceso: publico
- Uso: iniciar sesion
- Body:

```json
{
  "email": "cliente@example.com",
  "password": "Cliente123!"
}
```

### POST /auth/signup

- Acceso: publico
- Uso: alta publica solo para clientes
- Reglas:
- Debe incluir direccion
- Role permitido: CUSTOMER/ROLE_CUSTOMER

---

## 2) Landing y recomendaciones

### GET /landing

- Acceso: publico
- Uso: contenido general de landing

### GET /landing/seasonal

- Acceso: publico
- Uso: productos de temporada

### GET /landing/recommendations

- Acceso: publico
- Uso: recomendaciones personalizadas (si hay usuario autenticado, usa su contexto)

---

## 3) Productos

### GET /products

- Acceso: publico
- Uso: listado de productos activos

### GET /products/{id}

- Acceso: publico
- Uso: detalle de producto

### GET /products/category/{categoryId}

- Acceso: publico
- Uso: productos por categoria

### GET /products/search?query={texto}

- Acceso: publico
- Uso: busqueda de productos

### POST /products

- Acceso: LOGISTICS o ADMIN
- Uso: crear producto

### PUT /products/{id}

- Acceso: LOGISTICS o ADMIN
- Uso: actualizar producto

### DELETE /products/{id}

- Acceso: LOGISTICS o ADMIN
- Uso: desactivar producto

### PATCH /products/{id}/stock?delta={valor}

- Acceso: LOGISTICS o ADMIN
- Uso: ajustar stock (decimal)

---

## 4) Categorias

### GET /categories

- Acceso: publico
- Uso: listar categorias

### GET /categories/{id}

- Acceso: publico
- Uso: detalle categoria

### POST /categories

- Acceso: LOGISTICS o ADMIN
- Uso: crear categoria

### PUT /categories/{id}

- Acceso: LOGISTICS o ADMIN
- Uso: actualizar categoria

### DELETE /categories/{id}

- Acceso: ADMIN
- Uso: eliminar categoria

---

## 5) Ofertas

### GET /offers

- Acceso: publico
- Uso: listar ofertas activas

### GET /offers/{id}

- Acceso: publico
- Uso: detalle de oferta

### POST /offers

- Acceso: LOGISTICS o ADMIN
- Uso: crear oferta estacional

### PUT /offers/{id}

- Acceso: LOGISTICS o ADMIN
- Uso: actualizar oferta

### DELETE /offers/{id}

- Acceso: LOGISTICS o ADMIN
- Uso: desactivar oferta

---

## 6) Almacenes

### GET /warehouses

- Acceso: publico
- Uso: listar almacenes

### GET /warehouses/{id}

- Acceso: publico
- Uso: detalle de almacen

### GET /warehouses/{id}/delivery-agents

- Acceso: LOGISTICS o ADMIN
- Uso: repartidores disponibles por almacen

### POST /warehouses

- Acceso: ADMIN
- Uso: crear almacen

### PUT /warehouses/{id}

- Acceso: ADMIN
- Uso: actualizar almacen

---

## 7) Usuarios

### GET /users/me

- Acceso: autenticado
- Uso: ver perfil propio

### PUT /users/me

- Acceso: autenticado
- Uso: actualizar perfil propio (parcial)

Body parcial permitido:

```json
{
  "firstName": "NuevoNombre",
  "phoneNumber": "699111222"
}
```

### DELETE /users/me

- Acceso: autenticado
- Uso: baja de cuenta propia (deshabilitar)

### PATCH /users/me/delivery-status?status={valor}

- Acceso: DELIVERY
- Uso: cambiar estado operativo del repartidor
- Valores validos:
- AT_WAREHOUSE
- DELIVERING
- OFFLINE

### GET /users/{id}

- Acceso: ADMIN
- Uso: ver usuario por id

### PUT /users/{id}

- Acceso: ADMIN
- Uso: actualizar usuario por id

### DELETE /users/{id}

- Acceso: ADMIN
- Uso: desactivar usuario por id

---

## 8) Pedidos

### POST /orders

- Acceso: CUSTOMER
- Uso: crear pedido

### GET /orders/my

- Acceso: CUSTOMER o DELIVERY
- Uso: pedidos del usuario autenticado

### GET /orders/{id}

- Acceso: autenticado
- Uso: detalle de pedido (con control interno de acceso)

### GET /orders/customer/{customerId}

- Acceso: CUSTOMER o ADMIN
- Uso: pedidos por cliente

### GET /orders/warehouse/{warehouseId}/pending

- Acceso: LOGISTICS o ADMIN
- Uso: pedidos pendientes por almacen

### GET /orders/warehouse/{warehouseId}/confirmed

- Acceso: LOGISTICS o ADMIN
- Uso: pedidos confirmados listos para asignar

### GET /orders/warehouse/{warehouseId}/in-transit

- Acceso: LOGISTICS o ADMIN
- Uso: pedidos en transito por almacen

### GET /orders/delivery-agent/{deliveryAgentId}

- Acceso: DELIVERY, LOGISTICS o ADMIN
- Uso: pedidos de un repartidor

### PUT /orders/{orderId}/assign/{deliveryAgentId}

- Acceso: LOGISTICS o ADMIN
- Uso: asignar pedido a repartidor

### PUT /orders/{orderId}/status?status={valor}

- Acceso: DELIVERY, LOGISTICS o ADMIN
- Uso: actualizar estado de pedido

### PUT /orders/{orderId}/accept

- Acceso: DELIVERY
- Uso: aceptar pedido asignado

### PUT /orders/{orderId}/reject?reason={texto}

- Acceso: DELIVERY
- Uso: rechazar pedido asignado

### PUT /orders/{orderId}/confirm-loaded

- Acceso: DELIVERY
- Uso: confirmar carga para salida

### PUT /orders/{orderId}/deliver

- Acceso: DELIVERY
- Uso: marcar pedido como entregado

### PUT /orders/{orderId}/cancel?reason={texto}

- Acceso: CUSTOMER o ADMIN
- Uso: cancelar pedido segun reglas de estado

---

## 9) ETA

### GET /orders/eta/{orderId}/calculate

- Acceso: autenticado
- Uso: calcular ETA

### GET /orders/eta/{orderId}

- Acceso: autenticado
- Uso: obtener ultimo ETA

### POST /orders/eta/delivery-agent/{deliveryAgentId}/recalculate

- Acceso: DELIVERY, LOGISTICS o ADMIN
- Uso: recalculo masivo de ETA por repartidor

---

## 10) Administracion

### GET /admin/users

- Acceso: ADMIN
- Uso: listar usuarios

### PUT /admin/users/{id}/roles

- Acceso: ADMIN
- Uso: cambiar roles

### POST /admin/users/internal

- Acceso: ADMIN
- Uso: crear cuentas internas (logistica, reparto, admin)

### DELETE /admin/users/{id}

- Acceso: ADMIN
- Uso: deshabilitar usuario

### GET /admin/users/active

- Acceso: ADMIN
- Uso: listar usuarios activos

### GET /admin/users/disabled

- Acceso: ADMIN
- Uso: listar usuarios deshabilitados

### POST /admin/users/{id}/reactivate

- Acceso: ADMIN
- Uso: reactivar usuario

### GET /admin/stats/top-products

- Acceso: ADMIN
- Uso: top productos del ultimo mes

### GET /admin/stats/products-detailed

- Acceso: ADMIN
- Uso: detalle de productos top

### GET /admin/stats/orders

- Acceso: ADMIN
- Uso: resumen de ordenes

### GET /admin/stats/users

- Acceso: ADMIN
- Uso: resumen de usuarios

### GET /admin/orders/today

- Acceso: ADMIN
- Uso: ordenes del dia

---

## 11) Reglas de dominio COPLACA

- El catalogo y ejemplos deben representar frutas y hortalizas.
- La venta contempla cantidades decimales para productos por kilo.
- En registro publico, el cliente debe proporcionar direccion para asignacion automatica de almacen.
- La logistica administra stock, ofertas y asignacion de pedidos.

Ultima actualizacion: Marzo 2026
