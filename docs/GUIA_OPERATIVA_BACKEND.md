# Guia Operativa del Backend

Guia de operacion diaria para ejecutar, validar y mantener API COPLACA en entorno local y de pruebas.

## 1. Resumen tecnico

- Arquitectura: monolito modular Maven.
- Runtime: Java 21 + Spring Boot.
- Seguridad: JWT Bearer + control por roles.
- Datos: H2 en memoria por defecto, MySQL opcional.
- Documentacion runtime: Swagger UI y OpenAPI JSON.

## 2. Estructura funcional

Modulos:

- product-domain
- user-domain
- order-domain
- recommendation-domain
- rest-server

Punto de entrada de aplicacion:

- api-coplaca/rest-server/src/main/java/com/coplaca/apirest/ApirestApplication.java

## 3. Requisitos del entorno

- JDK 21.
- PowerShell en Windows.
- Docker Desktop para escenario MySQL.

Verificacion rapida:

```powershell
java -version
docker --version
```

## 4. Variables de entorno

Obligatoria:

- JWT_SECRET

Opcionales:

- JWT_EXPIRATION_MS
- DB_URL
- DB_USER
- DB_PASSWORD
- DB_DRIVER
- APP_BOOTSTRAP_ADMIN_EMAIL
- APP_BOOTSTRAP_ADMIN_PASSWORD

Ejemplo minimo:

```powershell
$env:JWT_SECRET="dev-jwt-secret-change-me"
```

## 5. Arranque operativo

### Escenario A: H2 rapido

```powershell
$env:JWT_SECRET="dev-jwt-secret-change-me"
.\mvnw.cmd -f api-coplaca\pom.xml -pl rest-server -am spring-boot:run
```

### Escenario B: MySQL persistente

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

## 6. Validacion post-arranque

1. Confirmar que responde http://localhost:8080.
2. Abrir Swagger UI.
3. Ejecutar login y consumir endpoint protegido.

URLs utiles:

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- H2 Console: http://localhost:8080/h2-console
- phpMyAdmin: http://localhost:8081

## 7. Build y pruebas

```powershell
# Build completo
.\mvnw.cmd -f api-coplaca\pom.xml clean verify

# Pruebas
.\mvnw.cmd -f api-coplaca\pom.xml test

# Compilar sin pruebas
.\mvnw.cmd -f api-coplaca\pom.xml -DskipTests compile
```

## 8. Seguridad operativa

### Autenticacion

- Login: POST /auth/login
- Signup cliente: POST /auth/signup

### Rutas con prefijo funcional

La API de negocio usa mayoritariamente prefijo /api/v1.

Ejemplos:

- /api/v1/products
- /api/v1/orders
- /api/v1/users
- /api/v1/admin

### CORS por defecto

- http://localhost:4200
- http://localhost:4201
- http://localhost:5173

Configurable con app.cors.allowed-origins.

## 9. Operacion de datos semilla

DataInitializer crea datos base en primer arranque sin duplicar existentes:

- Roles
- Almacenes
- Categorias
- Productos
- Usuarios de demo

Detalle completo en docs/DATOS_INICIALES_BOOTSTRAP.md.

## 10. Runbook de troubleshooting

### Falla de arranque por JWT_SECRET

- Sintoma: error al inicializar seguridad JWT.
- Accion: definir JWT_SECRET antes de iniciar.

### CORS bloqueando frontend

- Sintoma: error de preflight o bloqueo por origen.
- Accion: añadir origen al valor app.cors.allowed-origins.

### Sin conexion a MySQL

- Verificar contenedores arriba con docker-compose ps.
- Verificar DB_URL, DB_USER y DB_PASSWORD.
- Confirmar puerto 3306 libre.

### Puerto 8080 ocupado

- Cerrar proceso previo o levantar backend en otro puerto.

### Credenciales semilla no funcionan

- Revisar si el usuario ya existia con otra password hash.
- Reiniciar volumen MySQL si se requiere estado limpio.

## 11. Checklist de cambios backend

1. Implementar cambio en dominio y/o rest-server.
2. Compilar reactor y ejecutar pruebas.
3. Actualizar docs afectados.
4. Verificar OpenAPI runtime.
5. Si aplica, alinear docs/contracts/v1.

## 12. Documentacion vinculada

- docs/INDICE_DOCUMENTACION.md
- docs/GUIA_ARRANQUE_RAPIDO.md
- docs/ARQUITECTURA_MODULAR.md
- docs/MAPA_BACKEND.md
- docs/REFERENCIA_API.md
- docs/PRUEBAS_DATOS_AVANZADOS.md
- docs/ESTADO_BACKEND.md
- docs/PRESENTACION_TECNICA_BACKEND.md
