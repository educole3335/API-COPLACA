# Pruebas Avanzadas y Datos de Escenario

Guia para preparar, ejecutar y validar escenarios funcionales avanzados en API COPLACA.

## 1. Objetivos de prueba

- Validar ciclo de pedido completo.
- Verificar permisos por rol.
- Contrastar consistencia entre API y base de datos.
- Probar analitica y consultas operativas.

## 2. Precondiciones

1. Backend levantado en local.
2. Datos semilla cargados.
3. Usuario de prueba autenticable.

Referencias:

- docs/GUIA_ARRANQUE_RAPIDO.md
- docs/DATOS_INICIALES_BOOTSTRAP.md

## 3. Escenario A: crear pedido por API

Nota: los endpoints de pedidos usan prefijo /api/v1.

```bash
# 1) Login
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "cliente@example.com",
    "password": "Cliente123!"
  }' | jq -r '.token')

# 2) Crear pedido
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "warehouseId": 1,
    "items": [
      { "productId": 1, "quantity": 2.0 },
      { "productId": 7, "quantity": 1.5 }
    ],
    "deliveryFee": 2.00
  }'
```

Validar:

1. HTTP 201 o 200 segun wrapper.
2. Pedido visible en GET /api/v1/orders/me.
3. Totales consistentes en DB.

## 4. Escenario B: ciclo logistico de pedido

Objetivo: validar transiciones controladas por rol.

Flujo recomendado:

1. LOGISTICS asigna pedido.
2. DELIVERY acepta pedido.
3. DELIVERY confirma carga.
4. DELIVERY marca entrega.

Endpoints:

- PUT /api/v1/orders/{orderId}/assign/{deliveryAgentId}
- PUT /api/v1/orders/{orderId}/accept
- PUT /api/v1/orders/{orderId}/confirm-loaded
- PUT /api/v1/orders/{orderId}/deliver

## 5. Escenario C: estado operativo de reparto

Endpoint:

- PATCH /api/v1/users/me/delivery-status?status=AT_WAREHOUSE|DELIVERING|OFFLINE

Verificar en DB:

```sql
SELECT email, delivery_status
FROM users
WHERE email IN ('repartidor@example.com', 'ana@example.com');
```

## 6. Escenario D: validaciones de seguridad

Casos recomendados:

1. Sin token en endpoint privado debe responder 401.
2. Rol incorrecto en endpoint restringido debe responder 403.
3. Token valido con rol correcto debe responder 2xx.

## 7. Consultas SQL de soporte

### Pedidos recientes

```sql
SELECT id, order_number, status, total_price, created_at
FROM orders
ORDER BY created_at DESC;
```

### Lineas por pedido

```sql
SELECT oi.order_id, oi.product_id, oi.quantity, oi.price_per_unit, oi.total_price
FROM order_items oi
WHERE oi.order_id = 1;
```

### Estado de inventario

```sql
SELECT id, name, stock_quantity, unit_price
FROM products
ORDER BY id;
```

## 8. Consultas analiticas

```sql
-- Ventas por categoria
SELECT pc.name, COUNT(oi.id) AS articulos, SUM(oi.total_price) AS total_vendido
FROM order_items oi
JOIN products p ON oi.product_id = p.id
JOIN product_categories pc ON p.category_id = pc.id
GROUP BY pc.id, pc.name
ORDER BY total_vendido DESC;

-- Top productos por cantidad
SELECT p.name, SUM(oi.quantity) AS cantidad, SUM(oi.total_price) AS total
FROM order_items oi
JOIN products p ON oi.product_id = p.id
GROUP BY p.id, p.name
ORDER BY cantidad DESC
LIMIT 5;

-- Volumen por estado
SELECT status, COUNT(*) AS cantidad, SUM(total_price) AS monto_total
FROM orders
GROUP BY status;
```

## 9. Matriz de estados de referencia

DeliveryAgentStatus:

- AT_WAREHOUSE
- DELIVERING
- OFFLINE

OrderStatus:

- PENDING
- CONFIRMED
- ASSIGNED
- ACCEPTED
- IN_TRANSIT
- DELIVERED
- CANCELLED

## 10. Limpieza de entorno de pruebas

Ejemplo de limpieza controlada:

```sql
DELETE FROM order_items;
DELETE FROM orders;
```

Recomendacion:

- Evitar borrar usuarios semilla si se reutilizan para smoke tests.

## 11. Buenas practicas

- Ejecutar pruebas de API y validaciones SQL en paralelo para detectar divergencias.
- Registrar evidencias de respuesta y query en incidencias.
- Si cambian enums o rutas, actualizar este documento y docs/REFERENCIA_API.md.

Fecha de actualizacion: Marzo 2026
