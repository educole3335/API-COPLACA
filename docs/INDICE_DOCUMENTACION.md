# Indice Maestro de Documentacion

Inventario oficial de documentacion del backend API COPLACA.

## Mapa documental

| Documento | Propósito | Audiencia | Cuándo usarlo |
|---|---|---|---|
| docs/GUIA_ARRANQUE_RAPIDO.md | Levantar backend en minutos | Desarrollo, QA | Primer setup local y smoke test inicial |
| docs/GUIA_OPERATIVA_BACKEND.md | Operacion diaria y runbook tecnico | Desarrollo, DevOps, QA | Configuracion, ejecucion, soporte y troubleshooting |
| docs/ARQUITECTURA_MODULAR.md | Diseño arquitectonico y criterios de modularizacion | Arquitectura, Desarrollo | Entender fronteras de dominio y decisiones estructurales |
| docs/MAPA_BACKEND.md | Navegacion por modulos y componentes | Desarrollo | Localizar rapido clases, capas y rutas de codigo |
| docs/REFERENCIA_API.md | Catalogo de endpoints y acceso por rol | Frontend, Backend, QA | Integraciones y validacion funcional de API |
| docs/DATOS_INICIALES_BOOTSTRAP.md | Datos semilla y comportamiento de bootstrap | Desarrollo, QA | Preparar entornos de prueba y reset de datos |
| docs/PRUEBAS_DATOS_AVANZADOS.md | Escenarios avanzados API + SQL | QA, Desarrollo | Pruebas de ciclo completo y validaciones cruzadas |
| docs/ESTADO_BACKEND.md | Estado tecnico consolidado y brechas | Producto, Arquitectura, Desarrollo | Seguimiento de madurez y prioridades |
| docs/PRESENTACION_TECNICA_BACKEND.md | Guion de presentacion tecnica e informativa | Liderazgo tecnico, stakeholders, equipo | Exponer arquitectura, capacidades y roadmap |
| docs/PRESENTACION_BACKEND_1H.md | Presentacion tecnica completa de 60 minutos | Liderazgo tecnico, stakeholders, equipo | Sesion extendida con guion slide por slide y tiempos |
| docs/contracts/v1/openapi.yaml | Contrato OpenAPI versionado | Frontend, Integraciones, QA | Consumo por contrato y control de version API |
| docs/contracts/v1/coplaca-api-v1.postman_collection.json | Coleccion Postman de referencia | QA, Desarrollo | Ejecucion manual y automatizacion de pruebas |

## Regla de mantenimiento

1. Toda modificacion documental debe reflejarse en este indice en el mismo commit.
2. Si cambia un endpoint, actualizar al menos Referencia API y contrato versionado.
3. Evitar documentos duplicados; consolidar en los archivos oficiales de este indice.

## Ruta de lectura sugerida

1. docs/GUIA_ARRANQUE_RAPIDO.md
2. docs/GUIA_OPERATIVA_BACKEND.md
3. docs/ARQUITECTURA_MODULAR.md
4. docs/MAPA_BACKEND.md
5. docs/REFERENCIA_API.md
6. docs/ESTADO_BACKEND.md
7. docs/PRESENTACION_TECNICA_BACKEND.md
8. docs/PRESENTACION_BACKEND_1H.md
