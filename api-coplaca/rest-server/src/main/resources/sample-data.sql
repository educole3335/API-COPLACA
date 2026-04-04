-- ============================================
-- SCRIPT DE DATOS DE EJEMPLO PARA COPLACA API
-- Base de Datos: proyecto
-- ============================================

-- Nota: Este script es OPCIONAL. Los datos se insertan automáticamente
-- al iniciar la aplicación mediante DataInitializer.java

-- ============================================
-- 1. INSERTAR ROLES
-- ============================================
INSERT IGNORE INTO roles (name, description, created_at) VALUES
('ROLE_ADMIN', 'Administrador del sistema', NOW()),
('ROLE_CUSTOMER', 'Cliente final', NOW()),
('ROLE_DELIVERY', 'Repartidor/Mensajero', NOW()),
('ROLE_LOGISTICS', 'Personal de logística', NOW());

-- ============================================
-- 2. INSERTAR ALMACENES
-- ============================================
INSERT IGNORE INTO warehouses (name, address, latitude, longitude, capacity, phone_number, manager_name, is_active, created_at, updated_at) VALUES
('Almacén Tenerife', 'Polígono Industrial Guimar, Tenerife', 28.3172, -16.4133, 1000, '922000001', 'Encargado Tenerife', 1, NOW(), NOW()),
('Almacén Gran Canaria', 'Mercalaspalmas, Gran Canaria', 28.0997, -15.4134, 1000, '928000002', 'Encargado Gran Canaria', 1, NOW(), NOW()),
('Almacén La Palma', 'Zona Industrial El Paso, La Palma', 28.6500, -17.8830, 1000, '922000003', 'Encargado La Palma', 1, NOW(), NOW());

-- ============================================
-- 3. INSERTAR CATEGORÍAS DE PRODUCTOS
-- ============================================
INSERT IGNORE INTO product_categories (name, description, icon, color, is_active, created_at) VALUES
('Frutas Tropicales', 'Frutas tropicales frescas de Coplaca', '🍌', '#FFD700', 1, NOW()),
('Frutas Subtropicales', 'Frutas subtropicales de calidad premium', '🥑', '#7FBF7F', 1, NOW()),
('Otras Frutas Frescas', 'Frutas frescas variadas', '🍉', '#FF6B6B', 1, NOW()),
('Ortalizas', 'Hortalizas y vegetales frescos', '🥬', '#8FBC8F', 1, NOW());

-- ============================================
-- 4. INSERTAR PRODUCTOS
-- ============================================
INSERT IGNORE INTO products (name, description, unit, unit_price, original_price, stock_quantity, image_url, category_id, is_active, created_at, updated_at) VALUES
-- Frutas Tropicales (PRODUCTO ESTRELLA)
('Plátano de Canarias (IGP)', 'Plátano de Canarias con Indicación Geográfica Protegida. Producto estrella de Coplaca. Cultivado bajo los más altos estándares de calidad.', 'kg', 2.50, 3.00, 500, 'https://upload.wikimedia.org/wikipedia/commons/8/8a/Banana-Single.jpg', 1, 1, NOW(), NOW()),
('Mango', 'Mango fresco y tropical de excelente calidad', 'unidad', 3.80, 4.50, 200, 'https://upload.wikimedia.org/wikipedia/commons/9/90/Hapus_Mango.jpg', 1, 1, NOW(), NOW()),
('Papaya', 'Papaya tropical fresca jugosa', 'unidad', 2.90, 3.50, 150, 'https://upload.wikimedia.org/wikipedia/commons/5/50/Papaya_cross_section_BNC.jpg', 1, 1, NOW(), NOW()),
('Piña', 'Piña tropical dulce y fresca', 'unidad', 3.50, 4.20, 180, 'https://upload.wikimedia.org/wikipedia/commons/c/cb/Pineapple_and_cross_section.jpg', 1, 1, NOW(), NOW()),

-- Frutas Subtropicales
('Aguacate', 'Aguacate fresco de calidad premium', 'unidad', 2.20, 2.80, 250, 'https://upload.wikimedia.org/wikipedia/commons/c/c8/Avocado_Hass_-_single_and_halved.jpg', 2, 1, NOW(), NOW()),

-- Otras Frutas Frescas
('Sandía', 'Sandía fresca y jugosa', 'unidad', 6.50, 8.00, 100, 'https://upload.wikimedia.org/wikipedia/commons/f/fb/Watermelon_cross_BNC.jpg', 3, 1, NOW(), NOW()),
('Manzana', 'Manzana fresca de buena calidad', 'kg', 2.80, 3.50, 300, 'https://upload.wikimedia.org/wikipedia/commons/1/15/Red_Apple.jpg', 3, 1, NOW(), NOW()),
('Naranja', 'Naranja fresca jugosa', 'kg', 1.90, 2.50, 400, 'https://upload.wikimedia.org/wikipedia/commons/c/c4/Orange-Fruit-Pieces.jpg', 3, 1, NOW(), NOW()),
('Fresa', 'Fresa fresca y aromática', 'caja', 3.50, 4.50, 280, 'https://upload.wikimedia.org/wikipedia/commons/2/29/PerfectStrawberry.jpg', 3, 1, NOW(), NOW()),
('Kiwi', 'Kiwi fresco con sabor agradable', 'kg', 4.20, 5.00, 220, 'https://upload.wikimedia.org/wikipedia/commons/d/d3/Kiwi_aka.jpg', 3, 1, NOW(), NOW()),

-- Ortalizas
('Tomate Local', 'Tomate fresco cultivado localmente', 'kg', 2.50, 3.20, 350, 'https://upload.wikimedia.org/wikipedia/commons/8/89/Tomato_je.jpg', 4, 1, NOW(), NOW()),
('Lechuga Fresca', 'Lechuga crujiente y fresca', 'unidad', 1.50, 2.00, 400, 'https://upload.wikimedia.org/wikipedia/commons/2/21/Lettuce_Mini_Romaine.jpg', 4, 1, NOW(), NOW());

-- ============================================
-- 4.1 INSERTAR OFERTAS DE TEMPORADA
-- ============================================
INSERT IGNORE INTO seasonal_offers (product_id, discount_percentage, reason, start_date, end_date, is_active, created_at)
SELECT p.id, 15.0, 'Exceso de cosecha', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 14 DAY), 1, NOW()
FROM products p
WHERE p.name LIKE 'Plátano%';

INSERT IGNORE INTO seasonal_offers (product_id, discount_percentage, reason, start_date, end_date, is_active, created_at)
SELECT p.id, 18.0, 'Promoción tropical de la semana', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 10 DAY), 1, NOW()
FROM products p
WHERE p.name = 'Mango';

INSERT IGNORE INTO seasonal_offers (product_id, discount_percentage, reason, start_date, end_date, is_active, created_at)
SELECT p.id, 12.0, 'Lote fresco con salida rápida', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 7 DAY), 1, NOW()
FROM products p
WHERE p.name = 'Fresa';

INSERT IGNORE INTO seasonal_offers (product_id, discount_percentage, reason, start_date, end_date, is_active, created_at)
SELECT p.id, 20.0, 'Campaña especial de verano', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_ADD(NOW(), INTERVAL 12 DAY), 1, NOW()
FROM products p
WHERE p.name = 'Sandía';

-- ============================================
-- 5. INSERTAR DIRECCIONES Y USUARIOS
-- ============================================

-- Admin
INSERT IGNORE INTO addresses (street, street_number, city, postal_code, province, latitude, longitude, is_default) VALUES
('Sede central Coplaca', NULL, 'Santa Cruz de Tenerife', '38001', 'Santa Cruz de Tenerife', 28.4636, -16.2518, 1);

INSERT IGNORE INTO users (email, password, first_name, last_name, phone_number, enabled, address_id, warehouse_id, delivery_status, created_at, updated_at) VALUES
('admin@coplaca.local', '$2a$10$slYQmyNdGzin7olVRYGBRe2K.Gw3o0gIJU3G7Fv2Nq7VkrZW6BTK.', 'Admin', 'Coplaca', '922111111', 1, 1, 1, 'AT_WAREHOUSE', NOW(), NOW());

-- Asignar rol ADMIN al usuario admin
INSERT IGNORE INTO user_roles SELECT u.id, r.id FROM users u, roles r 
WHERE u.email = 'admin@coplaca.local' AND r.name = 'ROLE_ADMIN';

-- Cliente 1
INSERT IGNORE INTO addresses (street, street_number, apartment, city, postal_code, province, latitude, longitude, additional_info, is_default) VALUES
('Calle Principal', '42', '3B', 'Santa Cruz de Tenerife', '38003', 'Santa Cruz de Tenerife', 28.4635, -16.2520, 'Apto 3B', 1);

INSERT IGNORE INTO users (email, password, first_name, last_name, phone_number, enabled, address_id, warehouse_id, delivery_status, created_at, updated_at) VALUES
('cliente@example.com', '$2a$10$K7Zv6I5L2a3B8D9M0N1O2Pp3Qq4Rr5Ss/6Tt7Uu8Vv9Ww.KxYzA', 'Juan', 'García', '659123456', 1, 2, 1, 'AT_WAREHOUSE', NOW(), NOW());

INSERT IGNORE INTO user_roles SELECT u.id, r.id FROM users u, roles r 
WHERE u.email = 'cliente@example.com' AND r.name = 'ROLE_CUSTOMER';

-- Cliente 2
INSERT IGNORE INTO addresses (street, street_number, city, postal_code, province, latitude, longitude, additional_info, is_default) VALUES
('Avenida de la Paz', '78', 'La Laguna', '38200', 'Santa Cruz de Tenerife', 28.4891, -16.3183, 'Planta baja', 1);

INSERT IGNORE INTO users (email, password, first_name, last_name, phone_number, enabled, address_id, warehouse_id, delivery_status, created_at, updated_at) VALUES
('maria@example.com', '$2a$10$K7Zv6I5L2a3B8D9M0N1O2Pp3Qq4Rr5Ss/6Tt7Uu8Vv9Ww.KxYzA', 'María', 'López', '659654321', 1, 3, 1, 'AT_WAREHOUSE', NOW(), NOW());

INSERT IGNORE INTO user_roles SELECT u.id, r.id FROM users u, roles r 
WHERE u.email = 'maria@example.com' AND r.name = 'ROLE_CUSTOMER';

-- Repartidor 1
INSERT IGNORE INTO addresses (street, street_number, city, postal_code, province, latitude, longitude, is_default) VALUES
('Calle del Comercio', '15', 'Santa Cruz de Tenerife', '38001', 'Santa Cruz de Tenerife', 28.4636, -16.2518, 1);

INSERT IGNORE INTO users (email, password, first_name, last_name, phone_number, enabled, address_id, warehouse_id, delivery_status, created_at, updated_at) VALUES
('repartidor@example.com', '$2a$10$K7Zv6I5L2a3B8D9M0N1O2Pp3Qq4Rr5Ss/6Tt7Uu8Vv9Ww.KxYzA', 'Carlos', 'Martínez', '695987654', 1, 4, 1, 'AT_WAREHOUSE', NOW(), NOW());

INSERT IGNORE INTO user_roles SELECT u.id, r.id FROM users u, roles r 
WHERE u.email = 'repartidor@example.com' AND r.name = 'ROLE_DELIVERY';

-- Repartidor 2
INSERT IGNORE INTO addresses (street, street_number, city, postal_code, province, latitude, longitude, is_default) VALUES
('Avenida Trinitaria', '33', 'Santa Cruz de Tenerife', '38002', 'Santa Cruz de Tenerife', 28.465, -16.260, 1);

INSERT IGNORE INTO users (email, password, first_name, last_name, phone_number, enabled, address_id, warehouse_id, delivery_status, created_at, updated_at) VALUES
('ana@example.com', '$2a$10$K7Zv6I5L2a3B8D9M0N1O2Pp3Qq4Rr5Ss/6Tt7Uu8Vv9Ww.KxYzA', 'Ana', 'Rodríguez', '695112233', 1, 5, 1, 'AT_WAREHOUSE', NOW(), NOW());

INSERT IGNORE INTO user_roles SELECT u.id, r.id FROM users u, roles r 
WHERE u.email = 'ana@example.com' AND r.name = 'ROLE_DELIVERY';

-- Logística 1
INSERT IGNORE INTO addresses (street, street_number, city, postal_code, province, latitude, longitude, is_default) VALUES
('Polígono Industrial', '1', 'Santa Cruz de Tenerife', '38001', 'Santa Cruz de Tenerife', 28.4636, -16.2518, 1);

INSERT IGNORE INTO users (email, password, first_name, last_name, phone_number, enabled, address_id, warehouse_id, delivery_status, created_at, updated_at) VALUES
('logistica@example.com', '$2a$10$K7Zv6I5L2a3B8D9M0N1O2Pp3Qq4Rr5Ss/6Tt7Uu8Vv9Ww.KxYzA', 'Pedro', 'Fernández', '695345678', 1, 6, 1, 'AT_WAREHOUSE', NOW(), NOW());

INSERT IGNORE INTO user_roles SELECT u.id, r.id FROM users u, roles r 
WHERE u.email = 'logistica@example.com' AND r.name = 'ROLE_LOGISTICS';

-- Logística 2
INSERT IGNORE INTO addresses (street, street_number, city, postal_code, province, latitude, longitude, is_default) VALUES
('Calle del Almacén', '50', 'Santa Cruz de Tenerife', '38003', 'Santa Cruz de Tenerife', 28.4620, -16.2530, 1);

INSERT IGNORE INTO users (email, password, first_name, last_name, phone_number, enabled, address_id, warehouse_id, delivery_status, created_at, updated_at) VALUES
('alejandro@example.com', '$2a$10$K7Zv6I5L2a3B8D9M0N1O2Pp3Qq4Rr5Ss/6Tt7Uu8Vv9Ww.KxYzA', 'Alejandro', 'Sánchez', '695567890', 1, 7, 1, 'AT_WAREHOUSE', NOW(), NOW());

INSERT IGNORE INTO user_roles SELECT u.id, r.id FROM users u, roles r 
WHERE u.email = 'alejandro@example.com' AND r.name = 'ROLE_LOGISTICS';

