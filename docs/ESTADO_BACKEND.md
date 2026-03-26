# Backend COPLACA - Avance

Estado actualizado del backend modular.

## Resumen

- Arquitectura Maven multi-modulo consolidada.
- Seguridad stateless con JWT y control de acceso por rol.
- API HTTP operativa con documentacion OpenAPI/Swagger.
- Datos base autogenerados con `DataInitializer` para entorno de desarrollo.

## Modulos activos

- `product-domain`: catalogo, categorias, ofertas y reglas de producto.
- `user-domain`: usuarios, roles, direcciones y saldo.
- `order-domain`: pedidos, lineas, estados, checkout y pagos.
- `recommendation-domain`: recomendaciones y contenido de landing.
- `rest-server`: controladores, seguridad, bootstrap y configuracion.

## Capacidades implementadas

- Flujo de autenticacion y autorizacion con JWT.
- Endpoints publicos en catalogo (`/products`, `/offers`, `/warehouses`).
- Flujo de pedidos con validaciones por actor y estados.
- Soporte de pago por `PRESENTIAL`, `CARD` y `BALANCE` en checkout.
- Endpoints de saldo de usuario (`top-up` y metodos disponibles).
- Busqueda de productos con fallback a catalogo activo cuando la consulta esta vacia.

## Calidad y ejecucion

Pruebas unitarias disponibles principalmente en `rest-server`.

```powershell
.\mvnw.cmd -f api-coplaca\pom.xml test
```

OpenAPI en runtime:

- `GET /v3/api-docs`
- `/swagger-ui/index.html`

## Brechas actuales

- Aumentar pruebas de integracion HTTP para endpoints criticos.
- Incorporar pruebas de concurrencia sobre stock y pedidos.
- Mantener alineados contrato versionado (`docs/contracts/v1`) y anotaciones OpenAPI en runtime.
