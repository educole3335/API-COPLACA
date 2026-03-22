# 📋 Ejemplos de Datos Avanzados - COPLACA API

Este documento contiene ejemplos de datos más complejos que puedes agregar manualmente a la base de datos.

---

## 📦 Crear Órdenes de Ejemplo

### Opción 1: Mediante SQL

```sql
-- Crear una orden para el cliente Juan García
INSERT INTO orders (order_number, customer_id, warehouse_id, status, total_price, subtotal, discount, delivery_fee, created_at, updated_at) VALUES
('ORD-2026-001', 1, 1, 'PENDING', 25.50, 23.50, 0.00, 2.00, NOW(), NOW());

-- Agregar productos a la orden
INSERT INTO order_items (order_id, product_id, quantity, price_per_unit, total_price) VALUES
(1, 1, 2, 3.50, 7.00),  -- 2 kg de Tomates
(1, 5, 1, 1.85, 1.85),  -- 1L de Leche
(1, 9, 1, 8.50, 8.50);  -- 1 kg de Pechuga
```

### Opción 2: Mediante API (después de obtener JWT token)

```bash
# 1. Login para obtener token
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "cliente@example.com",
    "password": "Cliente123!"
  }' | jq -r '.accessToken')

# 2. Crear orden
curl -X POST http://localhost:8080/orders \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "warehouseId": 1,
    "items": [
      {
        "productId": 1,
        "quantity": 2
      },
      {
        "productId": 5,
        "quantity": 1
      },
      {
        "productId": 9,
        "quantity": 1
      }
    ],
    "deliveryFee": 2.00
  }'
```

---

## 👥 Agregar Más Usuarios

### Patrón Común para Agregar Usuarios

```java
// Agregar a DataInitializer.java, dentro de initializeReferenceData()

// Nuevo Cliente
User nuevoCliente = new User();
nuevoCliente.setEmail("new_customer@example.com");
nuevoCliente.setPassword(passwordEncoder.encode("Password123!"));
nuevoCliente.setFirstName("Pedro");
nuevoCliente.setLastName("Sánchez");
nuevoCliente.setPhoneNumber("691234567");
nuevoCliente.setEnabled(true);
nuevoCliente.setWarehouse(warehouseRepository.findByIsActiveTrue().stream().findFirst().orElseThrow());
nuevoCliente.setRoles(Set.of(roleRepository.findByName("ROLE_CUSTOMER").orElseThrow()));

Address direccion = new Address();
direccion.setStreet("Avenida Ejemplar");
direccion.setCity("Santa Cruz de Tenerife");
direccion.setPostalCode("38005");
direccion.setProvince("Santa Cruz de Tenerife");
direccion.setLatitude(28.4610);
direccion.setLongitude(-16.2490);

nuevoCliente.setAddress(direccion);
userRepository.save(nuevoCliente);
```

### SQL para Agregar Usuarios

```sql
-- Insertar dirección
INSERT INTO addresses (street, street_number, city, postal_code, province, latitude, longitude, is_default)
VALUES ('Calle Nueva', '55', 'Santa Cruz de Tenerife', '38001', 'Santa Cruz de Tenerife', 28.4640, -16.2516, 1);

-- Insertar usuario
INSERT INTO users (email, password, first_name, last_name, phone_number, enabled, address_id, warehouse_id, created_at, updated_at)
VALUES ('nuevo@example.com', '$2a$10/password_hash_aqui', 'Nombre', 'Apellido', '691234567', 1, 6, 1, NOW(), NOW());

-- Asignar rol CUSTOMER
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.email = 'nuevo@example.com' AND r.name = 'ROLE_CUSTOMER';
```

---

## 🏪 Agregar Más Almacenes

```java
// En DataInitializer.java

warehouseRepository.save(createWarehouse(
    "Almacén Fuerteventura",
    "Centro Comercial Puerto Shopping, Fuerteventura",
    28.3551,
    -14.0901,
    "928111111",
    "Encargado Fuerteventura"
));
```

---

## 🏷️ Agregar Más Categorías y Productos

### Script SQL para Nuevas Categorías

```sql
-- Nuevas categorías
INSERT IGNORE INTO product_categories (name, description, icon, color, is_active, created_at) VALUES
('Congelados', 'Productos congelados y helados', '❄️', '#B0E0E6', 1, NOW()),
('Snacks y Golosinas', 'Aperitivos y dulces variados', '🍪', '#F4A460', 1, NOW()),
('Productos Dietéticos', 'Alimentos especiales y orgánicos', '🌿', '#98FB98', 1, NOW()),
('Higiene y Limpieza', 'Productos de limpieza e higiene', '🧹', '#D3D3D3', 1, NOW());
```

### Script SQL para Productos en Nueva Categoría

```sql
-- Productos congelados
INSERT IGNORE INTO products (name, description, unit, unit_price, original_price, stock_quantity, category_id, is_active, created_at, updated_at) VALUES
('Nuggets de Pollo', 'Nuggets de pollo 400g', 'paquete', 5.50, 6.50, 150, 6, 1, NOW(), NOW()),
('Ensalada Tropical Congelada', 'Mezcla tropical congelada 600g', 'paquete', 4.20, 5.00, 100, 6, 1, NOW(), NOW()),
('Helado Vainilla', 'Helado de vainilla 500ml', 'unidad', 3.50, 4.20, 200, 6, 1, NOW(), NOW());

-- Snacks
INSERT IGNORE INTO products (name, description, unit, unit_price, original_price, stock_quantity, category_id, is_active, created_at, updated_at) VALUES
('Patatas Chips 200g', 'Patatas chips clásicas', 'bolsa', 2.30, 2.80, 300, 7, 1, NOW(), NOW()),
('Chocolate Negro 70%', 'Chocolate negro premium 100g', 'unidad', 3.00, 3.50, 200, 7, 1, NOW(), NOW()),
('Galletas Integrales', 'Galletas integrales naturales 250g', 'paquete', 2.80, 3.50, 250, 7, 1, NOW(), NOW());
```

---

## 🚚 Estados de Repartidor

Los repartidores pueden tener estos estados:

```
AT_WAREHOUSE      - En el almacén
ON_DELIVERY       - En ruta entregando
BETWEEN_DELIVERIES - Entre entregas
ON_BREAK          - En descanso
OFFLINE           - Desconectado
```

Para cambiar el estado de un repartidor:

```sql
-- Poner a repartidor "en ruta"
UPDATE users SET delivery_status = 'ON_DELIVERY' WHERE email = 'repartidor@example.com';

-- Volver a almacén
UPDATE users SET delivery_status = 'AT_WAREHOUSE' WHERE email = 'repartidor@example.com';
```

---

## 📊 Estados de Órdenes

Los estados de órdenes disponibles son:

```
PENDING        - Pendiente de confirmación
CONFIRMED      - Confirmada por el cliente
ASSIGNED       - Asignada a un repartidor
IN_TRANSIT      - En camino
DELIVERED      - Entregada
CANCELLED      - Cancelada
```

```sql
-- Ver todas las órdenes con su estado
SELECT id, order_number, status, total_price, created_at 
FROM orders 
ORDER BY created_at DESC;

-- Cambiar estado de una orden
UPDATE orders SET status = 'CONFIRMED' WHERE id = 1;
UPDATE orders SET status = 'IN_TRANSIT' WHERE id = 1;
UPDATE orders SET status = 'DELIVERED' WHERE id = 1;
```

---

## 💰 Ofertas Estacionales

Para agregar ofertas: 

```sql
-- Ver tabla de ofertas
SELECT * FROM seasonal_offers;

-- Insertar nueva oferta
INSERT INTO seasonal_offers (name, description, discount_percentage, valid_from, valid_to, is_active, created_at) VALUES
('Verano 2026', 'Descuento 15% en frutas', 15.00, '2026-06-01', '2026-08-31', 1, NOW());
```

---

## 🔗 Relaciones de Datos

```
┌──────────────┐
│   Usuarios   │
└──────────────┘
      ↓
   ┌─────┴──────────────┬──────────────┐
   ↓                     ↓              ↓
┌─────────┐      ┌────────────┐   ┌──────────┐
│ Roles   │      │ Direcciones│   │Almacenes │
└─────────┘      └────────────┘   └──────────┘
                         
    ┌────────────────┐
    │    Órdenes     │
    └────────────────┘
      Cliente ↑    ↓ Almacén
             └─────┘
                ↓
        ┌────────────────┐
        │ Artículos Orden│
        └────────────────┘
               ↓
        ┌────────────────┐
        │   Productos    │
        └────────────────┘
               ↓
        ┌────────────────┐
        │   Categorías   │
        └────────────────┘
```

---

## 🔐 Generar Hash de Contraseña (BCrypt)

Si necesitas agregar un usuario manualmente con contraseña encriptada:

```bash
# Usar online (NO en producción): https://www.bcryptcalculator.com/
# O en Java:
# PasswordEncoder encoder = new BCryptPasswordEncoder();
# String hashed = encoder.encode("MiContraseña123!");

# Ejemplo de hash BCrypt válido:
$2a$10$slYQmyNdGzin7olVRYGBRe2K.Gw3o0gIJU3G7Fv2Nq7VkrZW6BTK.
(contraseña original: Admin12345!)
```

---

## 🧹 Limpiar Datos

```sql
-- Eliminar todas las órdenes (cascada elimina items)
DELETE FROM orders;

-- Eliminar todos los usuarios (excepto admin)
DELETE FROM users WHERE email != 'admin@coplaca.local';

-- Eliminar todos los productos
DELETE FROM products;

-- Ver cuántos registros hay
SELECT 'Usuarios' as tabla, COUNT(*) as cantidad FROM users
UNION ALL
SELECT 'Órdenes', COUNT(*) FROM orders
UNION ALL
SELECT 'Productos', COUNT(*) FROM products;
```

---

## 📈 Estadísticas y Reportes

```sql
-- Total de ventas por categoría
SELECT pc.name, COUNT(oi.id) as articulos, SUM(oi.total_price) as total_vendido
FROM order_items oi
JOIN products p ON oi.product_id = p.id
JOIN product_categories pc ON p.category_id = pc.id
GROUP BY pc.id, pc.name
ORDER BY total_vendido DESC;

-- Top 5 productos más vendidos
SELECT p.name, SUM(oi.quantity) as cantidad, SUM(oi.total_price) as total
FROM order_items oi
JOIN products p ON oi.product_id = p.id
GROUP BY p.id, p.name
ORDER BY cantidad DESC
LIMIT 5;

-- Órdenes por estado
SELECT status, COUNT(*) as cantidad, SUM(total_price) as monto_total
FROM orders
GROUP BY status;

-- Clientes más activos
SELECT u.first_name, u.last_name, COUNT(o.id) as ordenes, SUM(o.total_price) as total_gastado
FROM users u
LEFT JOIN orders o ON u.id = o.customer_id
WHERE u.email != 'admin@coplaca.local'
GROUP BY u.id
ORDER BY total_gastado DESC;
```

---

## 🗺️ Coordenadas GPS de Referencia

| Ubicación | Latitud | Longitud |
|-----------|---------|----------|
| Santa Cruz de Tenerife (Centro) | 28.4636 | -16.2518 |
| La Laguna | 28.4891 | -16.3183 |
| Puerto de la Cruz | 28.4128 | -16.5497 |
| La Orotava | 28.3906 | -16.5192 |
| Icod de los Vinos | 28.3689 | -16.7195 |
| Las Palmas (Centro) | 28.1235 | -15.4363 |
| Gáldar | 27.9551 | -15.6363 |
| Vegueta | 28.0998 | -15.4130 |

---

## 🔔 Notas Finales

1. **Backup:** Antes de agregar grandes cantidades de datos, haz backup:
   ```bash
   docker-compose exec db mysqldump -u root -p1234qwerty proyecto > backup.sql
   ```

2. **Límites:** El stock máximo es `DECIMAL(12,3)` - hasta 999,999,999 unidades

3. **Precios:** Se usan `DECIMAL(10,2)` - máximo 99,999,999.99

4. **Geocercas:** Las coordenadas GPS se pueden usar para definir áreas de entrega

5. **Testing:** Los datos de ejemplo se cargan cuando la base esta vacia (primera ejecucion o despues de limpiar volumenes)

---

**Última actualización:** Marzo 2026
