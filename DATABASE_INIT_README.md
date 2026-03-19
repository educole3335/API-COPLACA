# 📊 Inicialización de Base de Datos - COPLACA API

## 🍌 Acerca de COPLACA

COPLACA es una empresa especializada en la comercialización de frutas tropicales y subtropicales. Su producto estrella es el **Plátano de Canarias (IGP)**, aunque también comercializa otras frutas frescas de calidad.

**Productos principales:**
- 🍌 **Plátano de Canarias (IGP)** - Producto principal y estrella de la empresa
- 🍍 **Frutas Tropicales:** Mango, Papaya, Piña
- 🥑 **Frutas Subtropicales:** Aguacate
- 🍉 **Otras Frutas Frescas:** Sandía, Manzana, Naranja, Fresa, Kiwi
- 🥬 **Ortalizas:** Tomate, Lechuga

Este documento explica cómo se inicializa la base de datos con datos de ejemplo cuando la aplicación se inicia por primera vez.

## 🗂️ Estructura de Datos

### 1. **Roles del Sistema**
Se crean automáticamente 4 roles:
- `ROLE_ADMIN` - Administrador del sistema
- `ROLE_CUSTOMER` - Cliente final
- `ROLE_DELIVERY` - Repartidor/Mensajero
- `ROLE_LOGISTICS` - Personal de logística

---

## 📦 Datos Iniciales Incluidos

### 2. **Almacenes (Warehouses)**
Se crean 3 almacenes en las Islas Canarias:

#### 🏭 Almacén Tenerife
- **Ubicación:** Polígono Industrial Guimar
- **Coordenadas:** 28.3172, -16.4133
- **Teléfono:** 922000001
- **Gerente:** Encargado Tenerife

#### 🏭 Almacén Gran Canaria
- **Ubicación:** Mercalaspalmas
- **Coordenadas:** 28.0997, -15.4134
- **Teléfono:** 928000002
- **Gerente:** Encargado Gran Canaria

#### 🏭 Almacén La Palma
- **Ubicación:** Zona Industrial El Paso
- **Coordenadas:** 28.6500, -17.8830
- **Teléfono:** 922000003
- **Gerente:** Encargado La Palma

---

### 3. **Categorías de Productos**
Se crean 4 categorías principales (Productos especializados de COPLACA):

| Categoría | Descripción | Icono | Color |
|-----------|-------------|-------|-------|
| Frutas Tropicales | Frutas tropicales frescas de Coplaca | 🍌 | #FFD700 |
| Frutas Subtropicales | Frutas subtropicales de calidad premium | 🥑 | #7FBF7F |
| Otras Frutas Frescas | Frutas frescas variadas | 🍉 | #FF6B6B |
| Ortalizas | Hortalizas y vegetales frescos | 🥬 | #8FBC8F |

---

### 4. **Productos de Ejemplo**

#### 🍌 Frutas Tropicales (PRODUCTO ESTRELLA)
- **Plátano de Canarias (IGP)** ⭐ - $2.50 kg (stock: 500) - *Producto principal de Coplaca*
- **Mango** - $3.80 unidad (stock: 200)
- **Papaya** - $2.90 unidad (stock: 150)
- **Piña** - $3.50 unidad (stock: 180)

#### 🥑 Frutas Subtropicales
- **Aguacate** - $2.20 unidad (stock: 250)

#### 🍉 Otras Frutas Frescas
- **Sandía** - $6.50 unidad (stock: 100)
- **Manzana** - $2.80 kg (stock: 300)
- **Naranja** - $1.90 kg (stock: 400)
- **Fresa** - $3.50 caja (stock: 280)
- **Kiwi** - $4.20 kg (stock: 220)

#### 🥬 Ortalizas
- **Tomate Local** - $2.50 kg (stock: 350)
- **Lechuga Fresca** - $1.50 unidad (stock: 400)

---

### 📊 Tabla Rápida de Referencia - Productos COPLACA

| Producto | Unidad | Precio | Stock | Categoría |
|----------|--------|--------|-------|-----------|
| Plátano  | kg | $2.50 | 500 | Frutas Tropicales |
|   Mango  | unidad | $3.80 | 200 | Frutas Tropicales |
|  Papaya  | unidad | $2.90 | 150 | Frutas Tropicales |
|  Piña    | unidad | $3.50 | 180 | Frutas Tropicales |
| Aguacate | unidad | $2.20 | 250 | Frutas Subtropicales |
| Sandía   | unidad | $6.50 | 100 | Otras Frutas Frescas |
|  Manzana | kg | $2.80 | 300 | Otras Frutas Frescas |
|  Naranja | kg | $1.90 | 400 | Otras Frutas Frescas |
| Fresa    | caja | $3.50 | 280 | Otras Frutas Frescas |
|  Kiwi    | kg | $4.20 | 220 | Otras Frutas Frescas |
| Tomate Local | kg | $2.50 | 350 | Ortalizas |
| Lechuga Fresca | unidad | $1.50 | 400 | Ortalizas |

---

### 5. **Cuentas de Usuario**

#### 🔐 Administrador
```
Email: admin@coplaca.local
Contraseña: Admin12345!
Rol: ROLE_ADMIN
```

#### 👥 Cliente Ejemplo 1
```
Email: cliente@example.com
Contraseña: Cliente123!
Nombre: Juan García
Teléfono: 659123456
Dirección: Calle Principal 42, Apto 3B
Ciudad: Santa Cruz de Tenerife
Código Postal: 38003
```

#### 👥 Cliente Ejemplo 2
```
Email: maria@example.com
Contraseña: Maria123!
Nombre: María López
Teléfono: 659654321
Dirección: Avenida de la Paz 78, Planta baja
Ciudad: La Laguna
Código Postal: 38200
```

#### 🚚 Repartidor Ejemplo 1
```
Email: repartidor@example.com
Contraseña: Repartidor123!
Nombre: Carlos Martínez
Teléfono: 695987654
Dirección: Calle del Comercio 15
Ciudad: Santa Cruz de Tenerife
Código Postal: 38001
Estado: AT_WAREHOUSE
```

#### 🚚 Repartidor Ejemplo 2
```
Email: ana@example.com
Contraseña: Ana123!
Nombre: Ana Rodríguez
Teléfono: 695112233
Dirección: Avenida Trinitaria 33
Ciudad: Santa Cruz de Tenerife
Código Postal: 38002
Estado: AT_WAREHOUSE
```

#### 📦 Logística Ejemplo 1
```
Email: logistica@example.com
Contraseña: Logistica123!
Nombre: Pedro Fernández
Teléfono: 695345678
Dirección: Polígono Industrial 1
Ciudad: Santa Cruz de Tenerife
Código Postal: 38001
Rol: ROLE_LOGISTICS
```

#### 📦 Logística Ejemplo 2
```
Email: alejandro@example.com
Contraseña: Alejandro123!
Nombre: Alejandro Sánchez
Teléfono: 695567890
Dirección: Calle del Almacén 50
Ciudad: Santa Cruz de Tenerife
Código Postal: 38003
Rol: ROLE_LOGISTICS
```

---

### 📋 Resumen de Cuentas de Usuario

| Tipo | Email | Contraseña | Nombre | Rol |
|------|-------|-----------|--------|-----|
| 🔐 Admin | admin@coplaca.local | Admin12345! | Admin Coplaca | ROLE_ADMIN |
| 👥 Cliente 1 | cliente@example.com | Cliente123! | Juan García | ROLE_CUSTOMER |
| 👥 Cliente 2 | maria@example.com | Maria123! | María López | ROLE_CUSTOMER |
| 🚚 Repartidor 1 | repartidor@example.com | Repartidor123! | Carlos Martínez | ROLE_DELIVERY |
| 🚚 Repartidor 2 | ana@example.com | Ana123! | Ana Rodríguez | ROLE_DELIVERY |
| 📦 Logística 1 | logistica@example.com | Logistica123! | Pedro Fernández | ROLE_LOGISTICS |
| 📦 Logística 2 | alejandro@example.com | Alejandro123! | Alejandro Sánchez | ROLE_LOGISTICS |

---

## 🚀 Cómo Usar

### Iniciando la Aplicación

1. **Asegurar que Docker está ejecutándose:**
```bash
cd dokersito
docker-compose up -d
```

2. **Esperar a que MySQL esté listo (15-30 segundos)**

3. **Ejecutar la aplicación:**
```bash
mvn spring-boot:run
```

O desde VS Code:
```bash
mvn clean install
mvn spring-boot:run
```

### ✅ Verificar Inicialización

La aplicación debería mostrar logs similares a estos:

```
[...] INFO [...] DataInitializer - Inicializando roles...
[...] INFO [...] DataInitializer - Inicializando almacenes...
[...] INFO [...] DataInitializer - Inicializando categorías de productos...
[...] INFO [...] DataInitializer - Inicializando productos...
[...] INFO [...] DataInitializer - Inicializando usuarios...
[...] INFO [...] Tomcat started on port(s): 8080 (http)
```

---

## 🔌 Accediendo a los Datos

### PhpMyAdmin
- **URL:** http://localhost:8081
- **Usuario:** root
- **Contraseña:** 1234qwerty
- **Base de datos:** proyecto

### H2 Console (si usas H2 en desarrollo)
- **URL:** http://localhost:8080/h2-console
- **JDBC URL:** `jdbc:h2:mem:coplaca`
- **Usuario:** sa
- **Contraseña:** (vacía)

### API Testing
Puedes usar Postman o cURL para probar los endpoints:

```bash
# Login como Cliente
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "cliente@example.com",
    "password": "Cliente123!"
  }'

# Login como Repartidor
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "repartidor@example.com",
    "password": "Repartidor123!"
  }'

# Login como Admin
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@coplaca.local",
    "password": "Admin12345!"
  }'
```

---

## 📝 Personalizar Datos Iniciales

Para modificar los datos que se inicializan, edita el archivo:

```
src/main/java/com/coplaca/apirest/config/DataInitializer.java
```

### Cambiar Email/Contraseña del Admin

En `application.properties`:
```properties
app.bootstrap.admin.email=tu_nuevo_email@coplaca.local
app.bootstrap.admin.password=TuNuevaContraseña123!
```

O mediante variables de entorno:
```bash
export ADMIN_EMAIL=admin@example.com
export ADMIN_PASSWORD=TuContraseña123!
```

### Agregar Más Productos

Dentro del método `initializeReferenceData()`, añade:

```java
productRepository.save(createProduct(
    "Nombre del Producto",
    "Descripción del producto",
    "kg", // o "unidad", "pack", etc
    new BigDecimal("precio_unitario"),
    new BigDecimal("precio_original"),
    new BigDecimal("cantidad_stock"),
    categoriaProducto
));
```

### Agregar Más Usuarios

```java
User nuevoUsuario = new User();
nuevoUsuario.setEmail("nuevo@example.com");
nuevoUsuario.setPassword(passwordEncoder.encode("Contraseña123!"));
nuevoUsuario.setFirstName("Nombre");
nuevoUsuario.setLastName("Apellido");
nuevoUsuario.setPhoneNumber("690112233");
nuevoUsuario.setEnabled(true);
nuevoUsuario.setRoles(Set.of(roleRepository.findByName("ROLE_CUSTOMER").orElseThrow()));

Address address = new Address();
address.setStreet("Calle Ejemplo");
address.setCity("Santa Cruz de Tenerife");
address.setPostalCode("38001");
address.setProvince("Santa Cruz de Tenerife");
address.setLatitude(28.4636);
address.setLongitude(-16.2518);
nuevoUsuario.setAddress(address);

userRepository.save(nuevoUsuario);
```

---

## ⚙️ Condiciones de Inicialización

Los datos **solo se insertan** si:
- Las tablas están **vacías** (primera ejecución)
- Se verifica con condiciones como `isEmpty()` o `findByEmail().isEmpty()`

Para **reiniciar los datos**, tienes 2 opciones:

### Opción 1: Limpiar Base de Datos (Recomendado)
```bash
# Detener la aplicación
# Limpiar los datos
docker-compose down -v
# Estará listo para nuevos datos en el siguiente inicio
```

### Opción 2: Configurar Hibernate para Recrear Tablas

En `application.properties`:
```properties
# Opción de desarrollo (SOLO en desarrollo, NUNCA en producción)
spring.jpa.hibernate.ddl-auto=create-drop
```

Valores disponibles:
- `validate` - Valida esquema (usado en producción)
- `update` - Actualiza esquema (por defecto)
- `create` - Crea de nuevo (destruye datos)
- `create-drop` - Crea y destruye al apagar (para testing)

---

## 🔍 Troubleshooting

### ❌ Error: "Cannot connect to database"
```bash
# Verificar que Docker está corriendo
docker ps

# Reiniciar contenedores
docker-compose restart
```

### ❌ Error: "Duplicate entry"
Los datos ya existen. Si necesitas reiniciar:
```bash
docker-compose down -v
docker-compose up -d
```

### ❌ Constraints violations
Asegúrate de que los emails son únicos en tu código personalizado.

### ❌ PhpMyAdmin no carga en localhost:8081
```bash
# Ver logs de Docker
docker-compose logs phpmyadmin

# Esperar 30 segundos y reintentar
docker-compose down
docker-compose up -d
```

---

## 📚 Referencias Adicionales

- **Spring Boot JPA:** https://spring.io/projects/spring-data-jpa
- **Lombok:** https://projectlombok.org/
- **Docker Compose:** https://docs.docker.com/compose/
- **MySQL:** https://dev.mysql.com/doc/

---

## ✨ Notas Importantes

1. **Seguridad:** Los datos de ejemplo contienen contraseñas simples. **NUNCA uses en producción**.
2. **Almacenes:** Todos los usuarios se asignan al primer almacén activo automáticamente.
3. **Coordenadas:** Las coordenadas GPS son reales (Islas Canarias, España).
4. **Stock:** Los productos comienzan con stock inicial realista.
5. **Precios:** Los precios incluyen tanto precio unitario como precio original (con descuento).

---

**Última actualización:** Marzo 2026
**Versión:** 1.0
