# Inicializacion de Base de Datos - COPLACA API

Este documento explica los datos que se cargan automaticamente al iniciar el backend y como trabajar con ellos en desarrollo.

## Fuente de la inicializacion

La inicializacion se realiza desde:

- `api-coplaca/rest-server/src/main/java/com/coplaca/apirest/config/DataInitializer.java`

## Que datos se crean

## 1) Roles

- `ROLE_CUSTOMER`
- `ROLE_LOGISTICS`
- `ROLE_DELIVERY`
- `ROLE_ADMIN`

## 2) Almacenes iniciales

- Almacen Tenerife
- Almacen Gran Canaria
- Almacen La Palma

## 3) Categorias iniciales

- Frutas Tropicales
- Frutas Subtropicales
- Otras Frutas Frescas
- Ortalizas

## 4) Productos iniciales

Se crean productos de ejemplo por categoria, incluyendo el producto estrella:

- Plátano de Canarias (IGP)

Tambien se inicializan otros productos de prueba para simular catalogo y stock.

## 5) Usuarios iniciales

- Admin
  - Email: `admin@coplaca.local`
  - Password: `Admin12345!`
- Cliente
  - Email: `cliente@example.com`
  - Password: `Cliente123!`
- Cliente
  - Email: `maria@example.com`
  - Password: `Maria123!`
- Repartidor
  - Email: `repartidor@example.com`
  - Password: `Repartidor123!`
- Repartidor
  - Email: `ana@example.com`
  - Password: `Ana123!`
- Logistica
  - Email: `logistica@example.com`
  - Password: `Logistica123!`
- Logistica
  - Email: `alejandro@example.com`
  - Password: `Alejandro123!`

## Condicion para insertar datos

Los datos se insertan solo cuando no existen registros equivalentes.

En practica:

- Si la base ya tiene datos, no se vuelven a crear duplicados.
- Si quieres reiniciar, debes limpiar la base/volumen y volver a iniciar.

## Arranque en desarrollo

## Opcion A: H2 en memoria (por defecto)

```powershell
.\mvnw.cmd -f api-coplaca\pom.xml -pl rest-server -am spring-boot:run
```

## Opcion B: MySQL con Docker

1. Levantar MySQL y phpMyAdmin:

```powershell
cd doker
docker-compose up -d
cd ..
```

2. Arrancar backend apuntando a MySQL:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/proyecto?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:DB_USER="root"
$env:DB_PASSWORD="1234qwerty"
$env:DB_DRIVER="com.mysql.cj.jdbc.Driver"
.\mvnw.cmd -f api-coplaca\pom.xml -pl rest-server -am spring-boot:run
```

## Verificacion rapida

Si todo arranca bien, veras logs del `DataInitializer` y el servidor en `http://localhost:8080`.

## Prueba de login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "cliente@example.com",
    "password": "Cliente123!"
  }'
```

## Personalizar admin bootstrap

Puedes cambiar el admin inicial por propiedad:

```properties
app.bootstrap.admin.email=tu_nuevo_email@coplaca.local
app.bootstrap.admin.password=TuNuevaContrasena123!
```

En entorno tambien aplica (binding relajado):

```bash
export APP_BOOTSTRAP_ADMIN_EMAIL=admin@example.com
export APP_BOOTSTRAP_ADMIN_PASSWORD=TuContrasena123!
```

## Acceso a DB

- phpMyAdmin: `http://localhost:8081`
- H2 Console: `http://localhost:8080/h2-console`

## Notas

- Las credenciales de este documento son solo para desarrollo/demo.
- No usar estas contrasenas en produccion.
- Si hay cambios en `DataInitializer`, este documento debe actualizarse en el mismo commit.

---

Ultima actualizacion: Marzo 2026
