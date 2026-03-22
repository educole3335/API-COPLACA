# API COPLACA

Backend de e-commerce para catalogo, usuarios, pedidos y logistica de reparto.

## Estado actual

- Arquitectura: Maven multi-modulo
- Java: 21
- Framework: Spring Boot 4.0.2
- Seguridad: JWT + Spring Security
- Base de datos por defecto: H2 en memoria
- Base de datos opcional: MySQL 8 (Docker)

## Estructura del proyecto

```text
api-coplaca/
  pom.xml                  # Parent del reactor Maven
  product-domain/          # Dominio de productos y categorias
  user-domain/             # Dominio de usuarios, roles y direcciones
  order-domain/            # Dominio de pedidos y flujo logistico
  recommendation-domain/   # Dominio de recomendaciones y landing
  rest-server/             # Capa HTTP, seguridad y bootstrap de datos
```

## Requisitos

- JDK 21
- Docker Desktop (solo si usaras MySQL)

## Ejecutar rapido (H2 en memoria)

Desde la raiz del repositorio (`API-COPLACA`):

```powershell
.\mvnw.cmd -f api-coplaca\pom.xml -pl rest-server -am spring-boot:run
```

La API inicia en:

- http://localhost:8080

Consola H2 (en modo desarrollo):

- http://localhost:8080/h2-console

## Ejecutar con MySQL (Docker)

1. Levanta MySQL y phpMyAdmin:

```powershell
cd doker
docker-compose up -d
cd ..
```

2. Arranca el backend apuntando a MySQL:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/proyecto?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:DB_USER="root"
$env:DB_PASSWORD="1234qwerty"
$env:DB_DRIVER="com.mysql.cj.jdbc.Driver"
.\mvnw.cmd -f api-coplaca\pom.xml -pl rest-server -am spring-boot:run
```

phpMyAdmin:

- http://localhost:8081

## Credenciales iniciales (DataInitializer)

- Admin
  - Email: admin@coplaca.local
  - Password: Admin12345!
- Cliente
  - Email: cliente@example.com
  - Password: Cliente123!
- Cliente
  - Email: maria@example.com
  - Password: Maria123!
- Repartidor
  - Email: repartidor@example.com
  - Password: Repartidor123!
- Repartidor
  - Email: ana@example.com
  - Password: Ana123!
- Logistica
  - Email: logistica@example.com
  - Password: Logistica123!
- Logistica
  - Email: alejandro@example.com
  - Password: Alejandro123!

## Endpoints base

- `POST /auth/login`
- `POST /auth/signup`
- `GET /products/**` (publico)
- `GET /offers/**` (publico)
- `GET /warehouses/**` (publico)
- `GET/POST /orders/**` (autenticado, segun rol)
- `GET/PUT /users/**` (autenticado)
- `GET/POST /admin/**` (admin)

## Build y tests

Compilar todo el reactor:

```powershell
.\mvnw.cmd -f api-coplaca\pom.xml clean verify
```

Ejecutar pruebas:

```powershell
.\mvnw.cmd -f api-coplaca\pom.xml test
```

## Documentacion interna

- `QUICKSTART.md`: guia corta para levantar el proyecto.
- `DATABASE_INIT_README.md`: detalle de datos de arranque y bootstrap.
- `README_MODULARIZACION.md`: contexto de la modularizacion.
- `docs/BACKEND_AVANCE.md`: estado funcional y tecnico del backend.
- `docs/BACKEND_MAPA_ARCHIVOS.md`: mapa actual de modulos y capas.
- `docs/DATOS_AVANZADOS.md`: ejemplos SQL/API para pruebas avanzadas.
