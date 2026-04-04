# Mapa del Backend COPLACA

## Objetivo

Servir como guia de navegacion tecnica del repositorio para localizar rapidamente modulos, componentes clave y puntos de entrada del backend.

## Vista de repositorio

```text
API-COPLACA/
  api-coplaca/
    pom.xml
    product-domain/
    user-domain/
    order-domain/
    recommendation-domain/
    rest-server/
  docs/
  doker/
  mvnw
  mvnw.cmd
```

## Elementos raiz relevantes

- README.md: entrada principal del proyecto.
- docs/: documentacion funcional y tecnica.
- doker/docker-compose.yml: infraestructura local MySQL + phpMyAdmin.
- api-coplaca/pom.xml: parent del reactor Maven.
- mvnw y mvnw.cmd: wrapper Maven sin instalacion global.

## Reactor Maven y dependencias internas

Modulos del reactor:

- api-coplaca/product-domain
- api-coplaca/user-domain
- api-coplaca/order-domain
- api-coplaca/recommendation-domain
- api-coplaca/rest-server

Patron de ensamblado:

- Los modulos de dominio encapsulan negocio y persistencia.
- rest-server expone HTTP y depende de los dominios.

## Mapa del modulo rest-server

Ruta base:

- api-coplaca/rest-server

Piezas clave:

- src/main/java/com/coplaca/apirest/ApirestApplication.java: bootstrap Spring Boot.
- src/main/java/com/coplaca/apirest/config/SecurityConfig.java: autenticacion/autorizacion y CORS.
- src/main/java/com/coplaca/apirest/config/OpenApiConfig.java: metadata OpenAPI.
- src/main/java/com/coplaca/apirest/config/DataInitializer.java: datos semilla.
- src/main/java/com/coplaca/apirest/security/JwtTokenProvider.java: emision y validacion de JWT.
- src/main/java/com/coplaca/apirest/security/JwtAuthenticationFilter.java: filtro de autenticacion.
- src/main/resources/application.properties: configuracion base de runtime.

## Controladores y rutas base

- AuthController: /auth
- LandingPageController: /landing
- ProductController: /api/v1/products
- CategoryController: /api/v1/categories
- SeasonalOfferController: /api/v1/offers
- WarehouseController: /api/v1/warehouses
- UserController: /api/v1/users
- OrderController: /api/v1/orders
- ETAController: /api/v1/eta
- AdminController: /api/v1/admin

## Dominios funcionales

### product-domain

- Modela productos, categorias y ofertas.
- Gestiona reglas de stock, precio y disponibilidad.

### user-domain

- Modela usuarios, roles y direcciones.
- Gestiona estados de reparto y ciclo de vida de cuentas.

### order-domain

- Modela pedidos y lineas.
- Gestiona flujo operativo de despacho y entrega.

### recommendation-domain

- Gestiona logica de contenido de landing y recomendaciones.

## Donde buscar cada tipo de cambio

### Nuevo endpoint

1. Controller en rest-server.
2. Service del dominio correspondiente.
3. Repository/entity si requiere persistencia.
4. DTO y mapper para contrato estable.
5. Actualizacion de docs/REFERENCIA_API.md y contrato v1.

### Cambio de seguridad

1. SecurityConfig para reglas globales.
2. @PreAuthorize en metodos de controller.
3. Validacion de rol en pruebas y docs.

### Cambio de datos iniciales

1. DataInitializer.
2. DATOS_INICIALES_BOOTSTRAP.md.
3. Casos de prueba asociados.

## Flujo operativo de una peticion

```text
Request HTTP
  -> Controller (rest-server)
  -> Service de dominio
  -> Repository
  -> Entity / DB
  -> Mapper + DTO
  -> Response HTTP
```

## Comandos frecuentes

```powershell
# Build completo
.\mvnw.cmd -f api-coplaca\pom.xml clean verify

# Tests
.\mvnw.cmd -f api-coplaca\pom.xml test

# Arranque backend
$env:JWT_SECRET="dev-jwt-secret-change-me"
.\mvnw.cmd -f api-coplaca\pom.xml -pl rest-server -am spring-boot:run
```

## Enlaces operativos

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
