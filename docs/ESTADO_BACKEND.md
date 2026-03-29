# Estado del Backend COPLACA

Estado tecnico consolidado del backend modular, con foco en capacidades activas, cobertura actual y prioridades de evolucion.

## Resumen ejecutivo

- Arquitectura modular Maven en produccion de desarrollo local.
- Seguridad stateless basada en JWT y autorizacion por roles.
- API REST operativa con contrato OpenAPI en runtime.
- Bootstrap de datos funcional para pruebas de negocio.
- Base estable para continuar con pruebas de integracion y endurecimiento operativo.

## Estado por dimension

### Arquitectura

Estado: Completa

- Reactor Maven multi-modulo funcional.
- Separacion de responsabilidades entre dominios y capa web.
- Paquetes y dependencias alineados a la modularizacion.

### Seguridad

Estado: Operativa

- Login y registro publico de cliente activos.
- JWT requerido para operaciones privadas.
- Control de acceso por rol en endpoints de negocio.
- CORS configurable por propiedad de aplicacion.

### API y contrato

Estado: Operativa

- Endpoints principales de autenticacion, catalogo, usuarios, pedidos y administracion activos.
- Swagger UI y OpenAPI JSON disponibles en runtime.
- Contrato versionado bajo docs/contracts/v1 para integraciones.

### Datos y bootstrap

Estado: Operativa

- DataInitializer crea datos base idempotentes para desarrollo.
- Roles, almacenes, categorias, productos y usuarios semilla disponibles.

### Calidad y pruebas

Estado: Parcial

- Suite de pruebas existente en rest-server.
- Falta ampliar cobertura de integracion end-to-end en flujos criticos.

## Capacidades implementadas

### Identidad y acceso

- Autenticacion con email y password.
- Emision de token JWT.
- Registro publico de clientes con direccion obligatoria.

### Catalogo

- Consulta de productos activos y detalle por identificador.
- Filtrado por categoria.
- Busqueda textual en catalogo.
- Gestion de stock y precio para roles internos.

### Pedidos

- Creacion de pedidos por cliente.
- Consulta por usuario, cliente, almacen y repartidor.
- Asignacion de pedidos a agentes de entrega.
- Transiciones de estado controladas por rol.
- Soporte de consulta de ETA por pedido.

### Administracion

- Gestion de usuarios internos.
- Activacion, desactivacion y reasignacion de roles.
- Endpoints de estadisticas operativas.

## Indicadores tecnicos actuales

- Build: ejecutable por reactor Maven.
- Test: ejecutable en proyecto completo.
- Documentacion: disponible en docs y contrato versionado.
- Entorno local: soporta H2 y MySQL con Docker.

## Riesgos y brechas

### Cobertura de pruebas

- Falta ampliar pruebas de integracion HTTP y escenarios de autorizacion por rol.

### Consistencia documental

- Toda alta/cambio de endpoint debe sincronizar referencia API y contrato versionado.

### Rendimiento y concurrencia

- Recomendado validar escenarios concurrentes en stock y flujo de pedidos.

## Prioridades recomendadas

1. Incrementar pruebas de integracion de auth, pedidos y administracion.
2. Incorporar pruebas de regresion de permisos por rol.
3. Formalizar validacion de contrato OpenAPI contra endpoints reales en CI.
4. Añadir metricas operativas y health checks ampliados para despliegue.

## Comandos base de verificacion

```powershell
.\mvnw.cmd -f api-coplaca\pom.xml clean verify
.\mvnw.cmd -f api-coplaca\pom.xml test
```

## Fecha de corte

- Marzo 2026
