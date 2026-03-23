# Backend Coplaca - Informe de avance (actualizado)

## 1) Resumen ejecutivo

El backend esta operativo en arquitectura multi-modulo, con autenticacion JWT, gestion de catalogo, usuarios, pedidos y logistica de reparto.

Estado general:

- Arquitectura modular consolidada.
- Seguridad y autorizacion por rol activas.
- Bootstrap de datos de referencia activo.
- Suite de pruebas unitarias disponible en `rest-server`.

## 2) Logros tecnicos principales

- Migracion de estructura a Maven multi-modulo por dominio.
- Separacion de capa HTTP en `rest-server` y logica de negocio por dominios.
- Seguridad stateless con JWT y reglas de acceso por endpoint/rol.
- Soporte de stock decimal para venta por peso.
- Flujo de pedidos con estados y validaciones por actor (cliente, logistica, reparto, admin).

## 3) Arquitectura actual

## Modulos

- `product-domain`: catalogo, categorias, ofertas y reglas de producto.
- `user-domain`: usuarios, roles, direccion, estado de repartidor.
- `order-domain`: pedido, lineas, transiciones y asignaciones.
- `recommendation-domain`: piezas de recomendaciones/landing.
- `rest-server`: controladores HTTP, seguridad, bootstrap y configuracion.

## Seguridad

- Login/signup: `POST /auth/login`, `POST /auth/signup`.
- Endpoints publicos: `GET /products/**`, `GET /offers/**`, `GET /warehouses/**`.
- Resto de endpoints: autenticacion obligatoria.

## 4) Datos de arranque

`DataInitializer` crea automaticamente:

- Roles base: `ROLE_CUSTOMER`, `ROLE_LOGISTICS`, `ROLE_DELIVERY`, `ROLE_ADMIN`.
- Almacenes iniciales en Canarias.
- Categorias y productos base.
- Cuentas iniciales (admin, clientes, reparto y logistica).

## 5) Calidad y pruebas

Cobertura actual de pruebas unitarias (principalmente en `rest-server`):

- Autenticacion y JWT.
- Reglas de negocio de usuarios.
- Reglas de negocio de productos/stock.
- Reglas de pedidos y transiciones.
- Inicializacion de datos de referencia.

Comando de ejecucion:

```powershell
.\mvnw.cmd -f api-coplaca\pom.xml test
```

## 6) Riesgos y brechas abiertas

- Cobertura de integracion HTTP aun limitada en endpoints criticos.
- Pruebas de concurrencia sobre stock/pedidos pendientes.
- Falta especificacion OpenAPI publica para consumidores externos.


## 7) Estado final

El backend esta listo para desarrollo funcional continuo y pruebas de integracion con frontend, con una base modular mas mantenible y segura.
