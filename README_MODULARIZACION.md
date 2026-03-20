# Modularizacion de API-COPLACA

## Que hicimos

Transformamos el proyecto de una estructura monolitica por paquetes a una estructura Maven multi-modulo por dominios de negocio.

Modulos creados:

- product-domain
- order-domain
- recommendation-domain
- rest-server

## Para que sirve esta modularizacion

### 1) Menos acoplamiento

Cada dominio vive en su propio modulo. Esto evita que un cambio en una parte del sistema rompa facilmente otras partes no relacionadas.

### 2) Mejor mantenibilidad

Cada modulo tiene responsabilidades claras:

- service: logica de negocio
- repository: acceso a base de datos con JPA
- dto (api): contratos de entrada/salida
- entity: modelo persistente

El modulo rest-server concentra la capa HTTP (controllers, config y security).

### 3) Mejor escalabilidad del equipo

Permite que varias personas trabajen en paralelo en modulos distintos con menos conflictos.

### 4) Build y pruebas mas confiables

Maven compila y valida por modulos dentro del reactor. Es mas facil localizar errores y saber en que capa o dominio ocurren.

### 5) Base para evolucion futura

Con esta separacion es mas sencillo:

- exponer nuevos endpoints
- extraer un dominio a microservicio en el futuro
- cambiar implementaciones internas sin impactar toda la app

## Estructura objetivo

- product-domain: catalogo de productos, categorias y ofertas
- order-domain: pedidos, usuarios, almacenes y logistica
- recommendation-domain: recomendaciones y contenido de landing
- rest-server: capa web y seguridad

## Resultado validado

- Compilacion del reactor completa: OK
- Pruebas del reactor completas: OK

## Estructura de capas en cada dominio

Cada módulo de dominio sigue una arquitectura de capas clara:

| Capa | Paquete | Responsabilidad |
|------|---------|-----------------|
| **Presentación** | rest-server | Controllers que exponen endpoints HTTP |
| **API/Transfer** | /api | DTOs que se lanzan al controller |
| **Negocio** | /service | DomainService que contiene la lógica y llama al repository |
| **Datos** | /repository | Interfaces JPA que acceden a la DB |
| **Persistencia** | /entity | Entidades Hibernate (mapeo O/R con la DB) |

### Flujo de datos

```
Usuario
  ↓
Controller (rest-server)
  ↓
DTO (api)
  ↓
DomainService (service) - Lógica de negocio
  ↓
Repository JPA (repo) - Acceso a DB
  ↓
Entity Hibernate (entity) - Mapeo de tablas
  ↓
Base de Datos
```

## Explicación sencilla 

"Pasamos de un monolito por paquetes a un monolito modular por dominios usando Maven multi-modulo.
Ahora cada dominio encapsula su logica, repositorios, DTOs y entidades, mientras rest-server expone solo la capa web.
Con esto reducimos acoplamiento, mejoramos mantenibilidad, aceleramos diagnostico de errores y dejamos la base preparada para crecer sin reescribir todo."

## Explicación tecnica

"El parent pom ahora maneja un reactor con cuatro modulos. Los servicios y repositorios se movieron a modulos de dominio segun responsabilidad funcional.
Los controllers y configuracion web quedaron en rest-server, que depende de los modulos de dominio.
Se eliminaron placeholders Domain* para evitar duplicidad y deuda tecnica.
La migracion se verifico con compile y test en todo el reactor, asegurando consistencia funcional."
