# Guia de Arranque Rapido

Guia minima para levantar API COPLACA en local en menos de 10 minutos.

## Requisitos

- JDK 21 instalado.
- Docker Desktop solo si usaras MySQL.
- PowerShell en Windows.

## Opcion 1: Arranque rapido con H2

Recomendado para desarrollo funcional rapido.

```powershell
$env:JWT_SECRET="dev-jwt-secret-change-me"
.\mvnw.cmd -f api-coplaca\pom.xml -pl rest-server -am spring-boot:run
```

Variables opcionales:

- JWT_EXPIRATION_MS: por defecto 86400000 milisegundos.

## Opcion 2: Arranque con MySQL Docker

Recomendado para pruebas con persistencia.

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

## Verificacion de arranque

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- H2 Console: http://localhost:8080/h2-console
- phpMyAdmin (si aplica): http://localhost:8081

## Prueba funcional minima

1. Ejecutar login.
2. Confirmar respuesta con token JWT.
3. Invocar endpoint protegido con cabecera Authorization.

Ejemplo login:

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "cliente@example.com",
    "password": "Cliente123!"
  }'
```

## Credenciales de desarrollo

- Admin: admin@coplaca.local / Admin12345!
- Cliente: cliente@example.com / Cliente123!
- Cliente: maria@example.com / Maria123!
- Repartidor: repartidor@example.com / Repartidor123!
- Repartidor: ana@example.com / Ana123!
- Logistica: logistica@example.com / Logistica123!
- Logistica: alejandro@example.com / Alejandro123!

Usuarios adicionales de reparto bootstrap:

- luis.reparto@example.com / Reparto123!
- carmen.reparto@example.com / Reparto123!

## Datos semilla cargados automaticamente

- 4 roles base.
- 3 almacenes en Canarias.
- 4 categorias.
- 12 productos iniciales.
- Usuarios de demo para todos los perfiles operativos.

## Comandos utiles

```powershell
# Build completo
.\mvnw.cmd -f api-coplaca\pom.xml clean verify

# Ejecutar pruebas
.\mvnw.cmd -f api-coplaca\pom.xml test
```

## Siguientes lecturas

- docs/GUIA_OPERATIVA_BACKEND.md
- docs/DATOS_INICIALES_BOOTSTRAP.md
- docs/REFERENCIA_API.md

