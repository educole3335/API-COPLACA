# Arquitectura Modular de API COPLACA

## Objetivo

Documentar la arquitectura backend actual tras la modularizacion Maven, explicando responsabilidades por modulo, capas internas y criterios tecnicos de mantenimiento.

## Alcance

- Estructura de modulos en el reactor Maven.
- Reglas de separacion por dominio.
- Flujo tecnico desde HTTP hasta persistencia.
- Beneficios, riesgos y lineamientos para evolucion futura.

## Vista de alto nivel

El backend mantiene un despliegue unico, pero con separacion interna por dominios de negocio y una capa web desacoplada.

Modulos actuales:

- product-domain
- user-domain
- order-domain
- recommendation-domain
- rest-server

## Responsabilidad por modulo

### product-domain

- Gestiona catalogo, categorias y ofertas estacionales.
- Contiene reglas de negocio de disponibilidad, precio y stock.

### user-domain

- Gestiona usuarios, roles, direcciones y estados operativos de reparto.
- Centraliza operaciones de perfil y administracion funcional de cuentas.

### order-domain

- Gestiona ciclo de vida de pedidos.
- Controla transiciones de estado, asignacion de reparto y reglas de cancelacion.

### recommendation-domain

- Provee contenido y recomendaciones para landing.
- Mantiene logica de seleccion de elementos destacados.

### rest-server

- Expone API HTTP y configura seguridad JWT.
- Aloja controllers, filtros, OpenAPI, bootstrap de datos y configuracion de infraestructura de aplicacion.

## Patrón de capas

Cada dominio sigue una estructura por capas orientada a mantenibilidad.

| Capa | Ubicacion tipica | Responsabilidad |
|------|------------------|-----------------|
| Presentacion | rest-server/controller | Endpoints HTTP, serializacion y codigos de respuesta |
| Aplicacion | domain/service | Casos de uso y reglas de negocio |
| Persistencia | domain/repository | Acceso a datos con Spring Data JPA |
| Modelo | domain/entity | Entidades y relaciones ORM |
| Transferencia | domain/dto y mapper | Contratos de entrada/salida y conversion Entity-DTO |

## Flujo tecnico

```text
Cliente HTTP
  -> Controller (rest-server)
  -> Service de dominio
  -> Repository JPA
  -> Base de datos
  -> Mapper + DTO
  -> Response HTTP
```

## Seguridad y fronteras

- La autenticacion se resuelve con JWT Bearer.
- El control de acceso se define por rol con anotaciones y reglas centralizadas de seguridad.
- Los endpoints publicos se limitan a autenticacion, landing y consultas de catalogo.
- El resto de operaciones requiere token valido y rol compatible.

## Beneficios obtenidos

### Menor acoplamiento

Los cambios en un dominio impactan menos al resto del sistema.

### Mejor mantenibilidad

Cada modulo tiene responsabilidad clara, facilitando refactorizaciones seguras.

### Escalabilidad de equipo

Permite trabajo paralelo por dominio, reduciendo conflictos y tiempos de integracion.

### Diagnostico mas rapido

Al compilar y probar por reactor, los errores quedan mejor localizados por modulo.

### Base para evolucion

La estructura deja preparado el camino para extraer dominios a servicios independientes si el crecimiento de carga o negocio lo requiere.

## Riesgos a vigilar

- Duplicacion de logica entre dominios si no se respetan fronteras.
- Acoplamiento accidental desde rest-server hacia detalles internos de persistencia.
- Deriva de contratos DTO sin actualizar OpenAPI y pruebas.

## Reglas de evolucion

- Todo endpoint nuevo debe mapearse a un caso de uso de dominio.
- Evitar exponer entidades JPA directamente en la API publica.
- Mantener mappers y DTOs versionados de forma controlada.
- Actualizar documentacion y contrato versionado en el mismo commit funcional.

## Resumen ejecutivo

API COPLACA opera como monolito modular: despliegue unico, fronteras claras por dominio y una capa web centralizada. Esta arquitectura mejora mantenibilidad, permite evolucion incremental y soporta crecimiento funcional sin reescritura estructural inmediata.
