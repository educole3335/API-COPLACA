# COPLACA API - Guia Rapida

## Inicio rapido con H2 (recomendado para desarrollo)

Desde la raiz del repositorio:

```powershell
.\mvnw.cmd -f api-coplaca\pom.xml -pl rest-server -am spring-boot:run
```

La API quedara disponible en:

- http://localhost:8080

Consola H2:

- http://localhost:8080/h2-console

## Inicio con MySQL (Docker)

Usa este bloque unico para arrancar siempre con persistencia en MySQL (evita H2 en memoria):

```powershell
cd doker
docker-compose up -d
cd ..

$env:DB_URL="jdbc:mysql://localhost:3306/proyecto?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:DB_USER="root"
$env:DB_PASSWORD="1234qwerty"
$env:DB_DRIVER="com.mysql.cj.jdbc.Driver"
.\mvnw.cmd -f api-coplaca\pom.xml -pl rest-server -am spring-boot:run
```

Nota: si la app ya estaba abierta en `:8080`, cierrala antes de ejecutar el bloque.

phpMyAdmin:

- http://localhost:8081

## Credenciales de ejemplo

- Admin: `admin@coplaca.local` / `Admin12345!`
- Cliente: `cliente@example.com` / `Cliente123!`
- Cliente: `maria@example.com` / `Maria123!`
- Repartidor: `repartidor@example.com` / `Repartidor123!`
- Repartidor: `ana@example.com` / `Ana123!`

## Probar login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "cliente@example.com",
    "password": "Cliente123!"
  }'
```

## Datos iniciales que se cargan automaticamente

- 4 roles: CUSTOMER, LOGISTICS, DELIVERY, ADMIN
- 3 almacenes
- 4 categorias de producto
- 12 productos base
- usuarios de ejemplo (admin, clientes, reparto y logistica)

## Comandos utiles

```powershell
# Compilar el reactor
.\mvnw.cmd -f api-coplaca\pom.xml clean verify

# Ejecutar tests
.\mvnw.cmd -f api-coplaca\pom.xml test
```

## Mas informacion

- Ver `DATABASE_INIT_README.md` para detalle de bootstrap de datos.
- Ver `docs/API_REFERENCIA.md` para contratos y endpoints de toda la API.
- Ver `README.md` para vista general del proyecto.

