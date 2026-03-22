# Datos Avanzados y Casos de Prueba - COPLACA API

Este documento incluye ejemplos de datos y consultas para pruebas funcionales de la API.

Importante:

- COPLACA opera con frutas y hortalizas.
- Los ejemplos de productos deben mantenerse en ese dominio.
- Evitar datos de snacks, carnes o lacteos.

---

## 1) Crear ordenes de ejemplo

### Opcion A: SQL directo

```sql
-- Orden de ejemplo para cliente
INSERT INTO orders (order_number, customer_id, warehouse_id, status, total_price, subtotal, discount, delivery_fee, created_at, updated_at)
VALUES ('ORD-2026-001', 1, 1, 'PENDING', 16.40, 14.40, 0.00, 2.00, NOW(), NOW());

-- Lineas de orden (productos de frutas y hortalizas)
INSERT INTO order_items (order_id, product_id, quantity, price_per_unit, total_price)
VALUES
(1, 1, 2.000, 2.50, 5.00),   -- 2 kg de Plátano de Canarias
(1, 7, 1.500, 2.80, 4.20),   -- 1.5 kg de Manzana
(1, 11, 2.000, 2.50, 5.00);  -- 2 kg de Tomate Local
```

### Opcion B: API

```bash
# 1. Login
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "cliente@example.com",
    "password": "Cliente123!"
  }' | jq -r '.token')

# 2. Crear orden
curl -X POST http://localhost:8080/orders \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "warehouseId": 1,
    "items": [
      { "productId": 1, "quantity": 2.0 },
      { "productId": 7, "quantity": 1.5 },
      { "productId": 11, "quantity": 2.0 }
    ],
    "deliveryFee": 2.00
  }'
```

---

## 2) Crear usuarios de prueba

### Patron Java para DataInitializer

```java
User nuevoCliente = new User();
nuevoCliente.setEmail("nuevo.cliente@example.com");
nuevoCliente.setPassword(passwordEncoder.encode("Cliente123!"));
nuevoCliente.setFirstName("Lucia");
nuevoCliente.setLastName("Perez");
nuevoCliente.setPhoneNumber("691234567");
nuevoCliente.setEnabled(true);
nuevoCliente.setWarehouse(warehouseRepository.findByIsActiveTrue().stream().findFirst().orElseThrow());
nuevoCliente.setRoles(Set.of(roleRepository.findByName("ROLE_CUSTOMER").orElseThrow()));

Address address = new Address();
address.setStreet("Calle Mercado");
address.setStreetNumber("12");
address.setCity("Santa Cruz de Tenerife");
address.setPostalCode("38001");
address.setProvince("Santa Cruz de Tenerife");
address.setLatitude(28.4636);
address.setLongitude(-16.2518);

nuevoCliente.setAddress(address);
userRepository.save(nuevoCliente);
```

### SQL para usuario nuevo

```sql
INSERT INTO addresses (street, street_number, city, postal_code, province, latitude, longitude, is_default)
VALUES ('Calle Nueva', '55', 'Santa Cruz de Tenerife', '38001', 'Santa Cruz de Tenerife', 28.4640, -16.2516, 1);

INSERT INTO users (email, password, first_name, last_name, phone_number, enabled, address_id, warehouse_id, created_at, updated_at)
VALUES ('nuevo@example.com', '$2a$10$password_hash_aqui', 'Nombre', 'Apellido', '691234567', 1, 6, 1, NOW(), NOW());

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.email = 'nuevo@example.com' AND r.name = 'ROLE_CUSTOMER';
```

---

## 3) Gestion de almacenes

```java
warehouseRepository.save(createWarehouse(
    "Almacen Fuerteventura",
    "Zona Comercial Puerto del Rosario",
    28.5000,
    -13.8627,
    "928111111",
    "Encargado Fuerteventura"
));
```

---

## 4) Estados validos del reparto y pedidos

### DeliveryAgentStatus (real en codigo)

```text
AT_WAREHOUSE
DELIVERING
OFFLINE
```

```sql
UPDATE users SET delivery_status = 'DELIVERING' WHERE email = 'repartidor@example.com';
UPDATE users SET delivery_status = 'AT_WAREHOUSE' WHERE email = 'repartidor@example.com';
```

### OrderStatus (real en codigo)

```text
PENDING
CONFIRMED
ASSIGNED
ACCEPTED
IN_TRANSIT
DELIVERED
CANCELLED
```

```sql
SELECT id, order_number, status, total_price, created_at
FROM orders
ORDER BY created_at DESC;

UPDATE orders SET status = 'CONFIRMED' WHERE id = 1;
UPDATE orders SET status = 'ASSIGNED' WHERE id = 1;
UPDATE orders SET status = 'IN_TRANSIT' WHERE id = 1;
UPDATE orders SET status = 'DELIVERED' WHERE id = 1;
```

---

## 5) Ofertas estacionales

```sql
SELECT * FROM seasonal_offers;

INSERT INTO seasonal_offers (name, description, discount_percentage, valid_from, valid_to, is_active, created_at)
VALUES ('Temporada Verano 2026', 'Descuento en fruta tropical por alta disponibilidad', 15.00, '2026-06-01', '2026-08-31', 1, NOW());
```

---

## 6) Consultas de analitica

```sql
-- Ventas por categoria
SELECT pc.name, COUNT(oi.id) AS articulos, SUM(oi.total_price) AS total_vendido
FROM order_items oi
JOIN products p ON oi.product_id = p.id
JOIN product_categories pc ON p.category_id = pc.id
GROUP BY pc.id, pc.name
ORDER BY total_vendido DESC;

-- Top productos
SELECT p.name, SUM(oi.quantity) AS cantidad, SUM(oi.total_price) AS total
FROM order_items oi
JOIN products p ON oi.product_id = p.id
GROUP BY p.id, p.name
ORDER BY cantidad DESC
LIMIT 5;

-- Ordenes por estado
SELECT status, COUNT(*) AS cantidad, SUM(total_price) AS monto_total
FROM orders
GROUP BY status;
```

---

## 7) Coordenadas de referencia Canarias

| Ubicacion | Latitud | Longitud |
|-----------|---------|----------|
| Santa Cruz de Tenerife | 28.4636 | -16.2518 |
| La Laguna | 28.4891 | -16.3183 |
| Puerto de la Cruz | 28.4128 | -16.5497 |
| Las Palmas de Gran Canaria | 28.1235 | -15.4363 |
| Galdar | 27.9551 | -15.6363 |

---

## 8) Limpieza de datos de prueba

```sql
DELETE FROM orders;
DELETE FROM users WHERE email NOT IN ('admin@coplaca.local', 'cliente@example.com', 'maria@example.com', 'repartidor@example.com', 'ana@example.com', 'logistica@example.com', 'alejandro@example.com');
```

---

## 9) Notas finales

- Usar estas credenciales y datos solo en desarrollo.
- Si cambian enums, DTOs o contratos de endpoints, actualizar este documento en el mismo commit.
- Para referencia funcional completa de endpoints ver API_REFERENCIA.md.

Ultima actualizacion: Marzo 2026
