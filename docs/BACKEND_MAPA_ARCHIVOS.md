# Backend Coplaca - Mapa de archivos (actualizado)

## Objetivo

Este documento describe la estructura real del backend despues de la modularizacion Maven.

## 1) Raiz del repositorio

- `README.md`: guia principal del proyecto.
- `QUICKSTART.md`: arranque rapido.
- `DATABASE_INIT_README.md`: detalle de bootstrap de datos.
- `docs/API_REFERENCIA.md`: referencia funcional completa de endpoints.
- `doker/docker-compose.yml`: MySQL 8 + phpMyAdmin para entorno local.
- `mvnw` y `mvnw.cmd`: Maven Wrapper.

## 2) Reactor Maven

El parent del reactor esta en:

- `api-coplaca/pom.xml`

Modulos declarados:

- `api-coplaca/product-domain`
- `api-coplaca/user-domain`
- `api-coplaca/order-domain`
- `api-coplaca/recommendation-domain`
- `api-coplaca/rest-server`

## 3) Modulo rest-server (capa web y seguridad)

Ruta base:

- `api-coplaca/rest-server`

Piezas principales:

- `src/main/java/com/coplaca/apirest/ApirestApplication.java`: punto de arranque.
- `src/main/java/com/coplaca/apirest/config/SecurityConfig.java`: reglas de seguridad.
- `src/main/java/com/coplaca/apirest/config/DataInitializer.java`: carga de datos iniciales.
- `src/main/java/com/coplaca/apirest/security/JwtTokenProvider.java`: generacion y validacion JWT.
- `src/main/java/com/coplaca/apirest/security/JwtAuthenticationFilter.java`: autenticacion por token.
- `src/main/resources/application.properties`: configuracion de datasource/JPA/JWT.

Controladores:

- `controller/AuthController.java` -> `/auth`
- `controller/ProductController.java` -> `/products`
- `controller/SeasonalOfferController.java` -> `/offers`
- `controller/WarehouseController.java` -> `/warehouses`
- `controller/OrderController.java` -> `/orders`
- `controller/UserController.java` -> `/users`
- `controller/AdminController.java` -> `/admin`

Pruebas:

- `src/test/java/com/coplaca/apirest/**`

## 4) Dominios por modulo

### product-domain

- Entidades y repositorios de catalogo (productos, categorias, ofertas).
- Servicios de negocio de producto y ofertas.

### user-domain

- Entidades de usuario, roles y direcciones.
- Reglas de usuarios internos, clientes y estados de repartidor.

### order-domain

- Entidades de pedido y lineas.
- Reglas de ciclo de pedido, asignacion y transiciones por rol.

### recommendation-domain

- Componentes de recomendaciones y contenido de landing.

## 5) Flujo tecnico simplificado

```text
HTTP Request
  -> rest-server/controller
  -> servicio de dominio (modulo correspondiente)
  -> repositorio JPA
  -> base de datos
```

## 6) Comandos de trabajo

Compilar todo el reactor:

```powershell
.\mvnw.cmd -f api-coplaca\pom.xml clean verify
```

Ejecutar backend:

```powershell
.\mvnw.cmd -f api-coplaca\pom.xml -pl rest-server -am spring-boot:run
```

Ejecutar tests:

```powershell
.\mvnw.cmd -f api-coplaca\pom.xml test
```
