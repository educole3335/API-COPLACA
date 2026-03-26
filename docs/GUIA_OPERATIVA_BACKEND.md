# Guia Operativa del Backend - API COPLACA

Guia consolidada para instalar, configurar, ejecutar y mantener el backend modular de COPLACA.

## 1) Resumen del proyecto

- Tipo: backend de e-commerce (frutas y hortalizas)
- Arquitectura: Maven multi-modulo
- Java: 21
- Framework: Spring Boot
- Seguridad: JWT + Spring Security
- Documentacion API: OpenAPI 3 + Swagger UI
- Base de datos por defecto: H2 (dev)
- Base de datos opcional: MySQL 8 con Docker

## 2) Estructura del repositorio

```text
API-COPLACA/
  api-coplaca/
    pom.xml                    # Parent del reactor Maven
    product-domain/            # Productos, categorias, ofertas
    user-domain/               # Usuarios, roles, direcciones, saldo
    order-domain/              # Pedidos, estados y pagos
    recommendation-domain/     # Landing y recomendaciones
    rest-server/               # API HTTP, seguridad, config y bootstrap
  docs/                        # Documentacion funcional y tecnica
  doker/                       # Docker Compose para MySQL + phpMyAdmin
  docs/GUIA_ARRANQUE_RAPIDO.md      # Arranque rapido
  docs/DATOS_INICIALES_BOOTSTRAP.md # Datos iniciales y bootstrap
  README.md                    # Vista general
```

Arquitectura de transformacion de datos:

- Mappers por dominio para convertir Entity <-> DTO.
- Ubicacion principal: `api-coplaca/*-domain/src/main/java/com/coplaca/apirest/mapper/`.
- Ejemplos: `ProductMapper`, `UserMapper`, `OrderMapper`, `AddressMapper`.

## 3) Requisitos e instalaciones

## 3.1 Software base

- Git 2.40+
- JDK 21
- VS Code (ultimo estable)
- Docker Desktop (solo si usaras MySQL)

## 3.2 Verificaciones rapidas

```powershell
git --version
java -version
docker --version
```

## 3.3 Dependencias del proyecto

No necesitas instalar Maven globalmente; el proyecto usa Maven Wrapper:

- Windows: `mvnw.cmd`
- Linux/macOS: `./mvnw`

## 3.4 Librerias clave del backend

- Lombok: reduce boilerplate (`@Data`, `@Builder`, `@NoArgsConstructor`, etc.).
- springdoc-openapi: genera especificacion OpenAPI y Swagger UI en runtime.

## 4) Extensiones recomendadas para VS Code

Imprescindibles:

- `vscjava.vscode-java-pack`
- `vmware.vscode-spring-boot`
- `redhat.vscode-yaml`
- `ms-azuretools.vscode-docker`

Utiles para pruebas y API:

- `humao.rest-client`
- `rangav.vscode-thunder-client`

Utiles para productividad:

- `eamodio.gitlens`
- `mhutchie.git-graph`

## 5) Configuracion minima de entorno

El backend requiere `JWT_SECRET` para arrancar correctamente.

Ejemplo en PowerShell:

```powershell
$env:JWT_SECRET="dev-jwt-secret-change-me"
```

Opcional:

- `JWT_EXPIRATION_MS` (por defecto: `86400000`)

## 6) Formas de iniciar el proyecto

## 6.1 Opcion A: H2 en memoria (desarrollo rapido)

Desde la raiz del repositorio:

```powershell
$env:JWT_SECRET="dev-jwt-secret-change-me"
.\mvnw.cmd -f api-coplaca\pom.xml -pl rest-server -am spring-boot:run
```

Servicios disponibles:

- API: `http://localhost:8080`
- H2 Console: `http://localhost:8080/h2-console`

## 6.2 Opcion B: MySQL con Docker (persistencia)

1. Levantar infraestructura:

```powershell
cd doker
docker-compose up -d
cd ..
```

2. Exportar variables y arrancar backend:

```powershell
$env:JWT_SECRET="dev-jwt-secret-change-me"
$env:DB_URL="jdbc:mysql://localhost:3306/proyecto?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:DB_USER="root"
$env:DB_PASSWORD="1234qwerty"
$env:DB_DRIVER="com.mysql.cj.jdbc.Driver"
.\mvnw.cmd -f api-coplaca\pom.xml -pl rest-server -am spring-boot:run
```

Servicios:

- API: `http://localhost:8080`
- phpMyAdmin: `http://localhost:8081`

## 7) Compilacion, test y calidad

Compilar reactor completo:

```powershell
.\mvnw.cmd -f api-coplaca\pom.xml clean verify
```

Solo tests:

```powershell
.\mvnw.cmd -f api-coplaca\pom.xml test
```

Solo compilar sin tests:

```powershell
.\mvnw.cmd -f api-coplaca\pom.xml -DskipTests compile
```

## 8) Seguridad, CORS y autenticacion

## 8.1 JWT

- Login: `POST /auth/login`
- Signup publico: `POST /auth/signup` (solo clientes)
- Rutas protegidas requieren: `Authorization: Bearer <token>`

## 8.2 CORS actual

Orgenes permitidos por defecto:

- `http://localhost:4200`
- `http://localhost:4201`

Si el frontend usa otro puerto (por ejemplo 5173), actualizar `SecurityConfig`.

## 8.3 OpenAPI y Swagger

Documentacion interactiva disponible con la app levantada:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Contrato versionado adicional en repositorio:

- `docs/contracts/v1/openapi.yaml`
- `docs/contracts/v1/coplaca-api-v1.postman_collection.json`

## 8.4 Como iniciar y usar OpenAPI (paso a paso)

1. Arranca la API con H2 o MySQL (seccion 6).
2. Verifica que el backend este en `http://localhost:8080`.
3. Abre Swagger UI: `http://localhost:8080/swagger-ui/index.html`.
4. Si necesitas el contrato crudo, usa: `http://localhost:8080/v3/api-docs`.
5. Para endpoints privados, primero haz login en `/auth/login` y usa el token JWT en Swagger (`Authorize`).

## 9) Datos iniciales

Al arrancar, `DataInitializer` crea (si no existen):

- Roles: CUSTOMER, LOGISTICS, DELIVERY, ADMIN
- Almacenes iniciales
- Categorias y productos base
- Usuarios de ejemplo (admin, clientes, repartidores, logistica)

Credenciales de referencia en `DATOS_INICIALES_BOOTSTRAP.md`.

Credenciales resumidas para pruebas rapidas:

- Admin: `admin@coplaca.local` / `Admin12345!`
- Cliente: `cliente@example.com` / `Cliente123!`
- Cliente: `maria@example.com` / `Maria123!`
- Repartidor: `repartidor@example.com` / `Repartidor123!`
- Repartidor: `ana@example.com` / `Ana123!`
- Logistica: `logistica@example.com` / `Logistica123!`
- Logistica: `alejandro@example.com` / `Alejandro123!`

Prueba rapida de login:

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "cliente@example.com",
    "password": "Cliente123!"
  }'
```

## 10) Mejoras funcionales recientes

- Perfil de usuario con soporte de saldo e inicial de avatar.
- Recarga de saldo en backend con metodos habilitados.
- Checkout con metodos de pago: `PRESENTIAL`, `CARD`, `BALANCE`.
- Endpoint para obtener metodos de pago de checkout: `GET /orders/payment-methods`.
- Endpoints de saldo:
  - `GET /users/me/balance/top-up-methods`
  - `POST /users/me/balance/top-up`
- Busqueda de productos mejorada: si `query` esta vacia en `GET /products/search`, retorna catalogo activo completo.

## 11) Endpoints base de referencia

- `POST /auth/login`
- `POST /auth/signup`
- `GET /products/**`
- `GET /offers/**`
- `GET /warehouses/**`
- `GET/POST /orders/**` (segun rol)
- `GET/PUT /users/**`
- `GET/POST /admin/**`

Referencia completa: `docs/REFERENCIA_API.md`.

## 12) Troubleshooting rapido

Error: falta `JWT_SECRET`

- Causa: variable no definida
- Solucion: exportar `JWT_SECRET` antes de arrancar

Error CORS en frontend

- Causa: origen no permitido
- Solucion: agregar origen en `SecurityConfig`

Error de conexion MySQL

- Verificar `docker-compose up -d`
- Verificar `DB_URL`, `DB_USER`, `DB_PASSWORD`
- Verificar puerto 3306 libre

Puerto 8080 ocupado

- Detener proceso previo del backend
- O arrancar en otro puerto con propiedades de Spring

## 13) Documentacion relacionada

## 13.1 Documentacion activa (mantener actualizada)

- `docs/INDICE_DOCUMENTACION.md`
- `README.md`
- `docs/GUIA_ARRANQUE_RAPIDO.md`
- `docs/DATOS_INICIALES_BOOTSTRAP.md`
- `docs/ARQUITECTURA_MODULAR.md`
- `docs/REFERENCIA_API.md`
- `docs/ESTADO_BACKEND.md`
- `docs/MAPA_BACKEND.md`
- `docs/PRUEBAS_DATOS_AVANZADOS.md`
- `docs/contracts/v1/openapi.yaml`
- `docs/contracts/v1/coplaca-api-v1.postman_collection.json`

## 13.2 Politica de limpieza documental

- Evitar documentos duplicados de estado temporal en ramas de trabajo.
- Consolidar siempre la guia operativa en `docs/GUIA_OPERATIVA_BACKEND.md`.
- Si un documento deja de aportar valor, eliminarlo en el mismo commit donde se consolida su contenido.

Documentos de trabajo temporal (como planes o resumentes de refactor) se permiten solo durante ejecucion de una rama; al cerrar la rama deben archivarse o eliminarse.
