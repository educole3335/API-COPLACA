# Indice Maestro de Documentacion - API COPLACA

Este indice resume que contiene cada documento, para quien esta pensado y cuando usarlo.

| Documento | Proposito | Audiencia | Cuando usarlo |
|---|---|---|---|
| `docs/GUIA_ARRANQUE_RAPIDO.md` | Levantar la API en minutos (H2 o MySQL) | Desarrollo/QA | Primer arranque local o smoke test rapido |
| `docs/GUIA_OPERATIVA_BACKEND.md` | Guia operativa completa del backend | Desarrollo/DevOps | Configuracion, ejecucion, troubleshooting y operacion diaria |
| `docs/REFERENCIA_API.md` | Referencia funcional y tecnica de endpoints | Frontend/Backend/QA | Consultar contratos de endpoints, roles y flujo de uso |
| `docs/DATOS_INICIALES_BOOTSTRAP.md` | Datos semilla y bootstrap de entorno | Desarrollo/QA | Revisar usuarios base, roles, carga inicial y personalizacion del bootstrap |
| `docs/ARQUITECTURA_MODULAR.md` | Explicacion de la modularizacion por dominios | Desarrollo/Arquitectura | Entender estructura modular y responsabilidades por capa |
| `docs/MAPA_BACKEND.md` | Mapa de modulos, piezas clave y rutas de codigo | Desarrollo | Ubicar rapidamente clases, modulos y puntos de entrada |
| `docs/ESTADO_BACKEND.md` | Estado funcional/técnico actual del backend | Producto/Desarrollo | Revisar capacidades implementadas y brechas actuales |
| `docs/PRUEBAS_DATOS_AVANZADOS.md` | Datos avanzados y casos de prueba SQL/API | QA/Desarrollo | Preparar escenarios de prueba y validaciones avanzadas |
| `docs/contracts/v1/openapi.yaml` | Contrato OpenAPI versionado | Frontend/Backend/Integraciones | Integracion basada en contrato y control de cambios de API |
| `docs/contracts/v1/coplaca-api-v1.postman_collection.json` | Coleccion Postman versionada | QA/Desarrollo | Ejecucion manual o automatizada de pruebas de endpoints |

## Regla de mantenimiento

- Si se crea, renombra o elimina documentacion, actualizar este indice en el mismo commit.

## Orden recomendado de lectura

1. `docs/GUIA_ARRANQUE_RAPIDO.md`
2. `docs/GUIA_OPERATIVA_BACKEND.md`
3. `docs/REFERENCIA_API.md`
4. `docs/MAPA_BACKEND.md`
