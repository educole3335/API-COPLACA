# Datos Iniciales y Bootstrap

Este documento describe la carga automatica de datos al iniciar el backend y el comportamiento esperado en entornos de desarrollo.

## Fuente oficial del bootstrap

- api-coplaca/rest-server/src/main/java/com/coplaca/apirest/config/DataInitializer.java

## Comportamiento general

- Se ejecuta al arranque de la aplicacion.
- Crea datos faltantes y evita duplicados en registros ya existentes.
- Mezcla estrategias idempotentes por email, nombre o conteo segun entidad.

## Entidades inicializadas

### Roles

- ROLE_CUSTOMER
- ROLE_LOGISTICS
- ROLE_DELIVERY
- ROLE_ADMIN

### Almacenes

- Almacen Tenerife
- Almacen Gran Canaria
- Almacen La Palma

### Categorias

- Frutas Tropicales
- Frutas Subtropicales
- Otras Frutas Frescas
- Ortalizas

Nota: la categoria se mantiene con nombre Ortalizas segun el codigo actual.

### Productos

Se inicializan 12 productos base distribuidos por categoria, incluyendo:

- Plátano de Canarias (IGP)
- Mango
- Papaya
- Piña
- Aguacate
- Sandía
- Manzana
- Naranja
- Fresa
- Kiwi
- Tomate Local
- Lechuga Fresca

### Usuarios semilla

#### Administracion

- admin@coplaca.local / Admin12345!

#### Clientes

- cliente@example.com / Cliente123!
- maria@example.com / Maria123!

#### Reparto

- repartidor@example.com / Repartidor123!
- ana@example.com / Ana123!
- luis.reparto@example.com / Reparto123!
- carmen.reparto@example.com / Reparto123!

#### Logistica

- logistica@example.com / Logistica123!
- alejandro@example.com / Alejandro123!

## Personalizacion del admin inicial

Propiedades soportadas:

```properties
app.bootstrap.admin.email=tu_admin@coplaca.local
app.bootstrap.admin.password=TuPasswordSeguro123!
```

Variables de entorno equivalentes:

```powershell
$env:APP_BOOTSTRAP_ADMIN_EMAIL="tu_admin@coplaca.local"
$env:APP_BOOTSTRAP_ADMIN_PASSWORD="TuPasswordSeguro123!"
```

## Arranque para ejecutar bootstrap

### H2 en memoria

```powershell
$env:JWT_SECRET="dev-jwt-secret-change-me"
.\mvnw.cmd -f api-coplaca\pom.xml -pl rest-server -am spring-boot:run
```

### MySQL con Docker

```powershell
cd doker
docker-compose up -d
cd ..

$env:JWT_SECRET="dev-jwt-secret-change-me"
$env:DB_URL="jdbc:mysql://localhost:3306/proyecto?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:DB_USER="root"
$env:DB_PASSWORD="1234qwerty"
$env:DB_DRIVER="com.mysql.cj.jdbc.Driver"
.\mvnw.cmd -f api-coplaca\pom.xml -pl rest-server -am spring-boot:run
```

## Validacion del bootstrap

1. Confirmar inicio correcto del backend.
2. Ejecutar login con un usuario semilla.
3. Consultar entidades base desde API o DB.

Ejemplo login:

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "cliente@example.com",
    "password": "Cliente123!"
  }'
```

## Reinicio completo de datos

### En H2

- Reiniciar aplicacion suele resetear datos por ser en memoria.

### En MySQL Docker

```powershell
cd doker
docker-compose down -v
docker-compose up -d
cd ..
```

Luego reiniciar backend para repoblar.

## Notas de seguridad

- Credenciales de este documento son solo para desarrollo.
- No reutilizar passwords semilla en entornos reales.
- Cambios en DataInitializer requieren actualizar esta guia en el mismo commit.

## Fecha de actualizacion

- Marzo 2026
