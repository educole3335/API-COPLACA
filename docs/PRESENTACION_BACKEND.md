# Presentacion Tecnica del Backend COPLACA

Esta presentacion esta pensada para explicar el backend de COPLACA con una narrativa tecnica clara, sin limitarse a una lista de puntos. El objetivo es que cualquier audiencia tecnica pueda entender no solo que componentes existen, sino tambien por que se diseñaron de esa manera y como se conectan con la operacion real del negocio.

## Objetivo de la sesion

La sesion busca construir una vision completa del backend. Primero se enmarca el problema de negocio que se resuelve. Despues se explica la arquitectura y la implementacion real. Finalmente se revisan riesgos, calidad y plan de evolucion para alinear decisiones tecnicas.

## Audiencia objetivo

El contenido esta orientado a equipos de desarrollo backend y frontend, QA, analistas tecnicos y liderazgo tecnico o de producto que necesita contexto de arquitectura y operacion.

## Enfoque de la presentacion

La estructura esta organizada en diapositivas tematicas con tres partes en cada una:

- mensaje principal que debe quedar claro
- contenido recomendado para mostrar
- guion explicativo para exponer con fluidez

## Slide 1. Portada y contexto

- Mensaje principal:
  - Presentar el backend como una plataforma modular en evolucion.
- Contenido recomendado:
  - Nombre del sistema, estado tecnico actual y foco de la sesion.
- Guion explicativo:
  - La presentacion abre estableciendo que no se trata solo de una revision funcional. Es una lectura de madurez tecnica: donde estamos, que funciona de forma robusta y que decisiones tenemos por delante para escalar con control.

## Slide 2. Problema de negocio y tecnico

- Mensaje principal:
  - El backend conecta venta digital con operacion logistica real.
- Contenido recomendado:
  - Catalogo de frutas y hortalizas, pedidos, reparto y trazabilidad por rol.
- Guion explicativo:
  - Esta diapositiva debe explicar que el sistema no solo expone una API, sino que coordina procesos reales entre cliente, logistica y reparto. Por eso la arquitectura prioriza consistencia operativa y control de reglas de negocio.

## Slide 3. Alcance funcional del backend

- Mensaje principal:
  - El backend cubre los flujos centrales de la operacion.
- Contenido recomendado:
  - Auth, catalogo, ofertas, pedidos, ETA, usuarios y administracion.
- Guion explicativo:
  - Conviene explicar que el alcance actual permite un ciclo completo de pedido, desde autenticacion hasta entrega y cierre. Tambien se aclara que los permisos por rol son parte integral del diseño funcional.

## Slide 4. Vision de arquitectura

- Mensaje principal:
  - Se adopto una estrategia de monolito modular.
- Contenido recomendado:
  - Maven multi-modulo, separacion por dominios y capa web centralizada.
- Guion explicativo:
  - Esta decision permite equilibrio entre velocidad de entrega y orden estructural. No se introduce complejidad distribuida prematura, pero se mantienen fronteras claras para evolucionar por dominio.

## Slide 5. Mapa de modulos

- Mensaje principal:
  - Cada modulo representa una responsabilidad funcional concreta.
- Contenido recomendado:
  - product-domain, user-domain, order-domain, recommendation-domain y rest-server.
- Guion explicativo:
  - Se explica que los dominios concentran negocio y datos, mientras rest-server concentra controllers, seguridad y configuracion transversal. Este reparto reduce acoplamiento y facilita mantenimiento.

## Slide 6. Capas tecnicas internas

- Mensaje principal:
  - El backend usa capas para desacoplar API, negocio y persistencia.
- Contenido recomendado:
  - Flujo Controller -> Service -> Repository -> Entity y uso de DTO/Mapper.
- Guion explicativo:
  - Es importante remarcar que DTO y Mapper no son decorativos; son una frontera estable para proteger contratos API frente a cambios del modelo interno.

## Slide 7. Flujo de una solicitud

- Mensaje principal:
  - El recorrido tecnico de una peticion es trazable de extremo a extremo.
- Contenido recomendado:

```text
Cliente -> Controller -> Service -> Repository -> DB -> DTO -> Response
```

- Guion explicativo:
  - Esta diapositiva funciona como referencia mental para diagnostico. Si hay incidencia, ayuda a ubicar rapidamente en que capa se origina el problema.

## Slide 8. Modelo de pedidos y estados

- Mensaje principal:
  - El ciclo de pedidos esta gobernado por estados y reglas por actor.
- Contenido recomendado:
  - Estados principales y transiciones habilitadas por rol.
- Guion explicativo:
  - Se debe explicar que no todas las transiciones son libres. El modelo impone restricciones para preservar trazabilidad operativa y evitar inconsistencias de despacho o entrega.

## Slide 9. Seguridad: autenticacion y sesion

- Mensaje principal:
  - Seguridad stateless con JWT para operaciones protegidas.
- Contenido recomendado:
  - Token Bearer, filtro JWT y sesion sin estado en servidor.
- Guion explicativo:
  - Esta estrategia simplifica escalado y reduce dependencia de almacenamiento de sesion. El token se convierte en el mecanismo principal de contexto de identidad.

## Slide 10. Seguridad: autorizacion por rol

- Mensaje principal:
  - La autorizacion se define de forma explicita por endpoint.
- Contenido recomendado:
  - Reglas con PreAuthorize para admin, logistica, reparto y cliente.
- Guion explicativo:
  - El control de acceso es parte del diseño de negocio. No se limita a configuracion global; se aplica de forma declarativa y auditable en las rutas sensibles.

## Slide 11. API: estrategia de contrato

- Mensaje principal:
  - El contrato se mantiene vivo y versionado.
- Contenido recomendado:
  - Swagger runtime, OpenAPI versionado y coleccion Postman.
- Guion explicativo:
  - Se explica como se reduce friccion entre equipos manteniendo sincronia entre implementacion, documentacion y pruebas manuales o automatizadas.

## Slide 12. API: dominios principales

- Mensaje principal:
  - La superficie API esta organizada por contexto funcional.
- Contenido recomendado:
  - Auth y landing, luego recursos de negocio bajo api/v1.
- Guion explicativo:
  - Esta organizacion mejora legibilidad y gobernanza de la API. Tambien ayuda a planificar versionado y cambios incrementales con menor impacto.

## Slide 13. Datos iniciales y bootstrap

- Mensaje principal:
  - El entorno puede estar operativo rapidamente gracias al bootstrap.
- Contenido recomendado:
  - DataInitializer, roles, almacenes, productos y usuarios semilla.
- Guion explicativo:
  - Se destaca su valor para onboarding, demos y pruebas funcionales repetibles, evitando dependencia de carga manual inicial.

## Slide 14. Entornos de ejecucion

- Mensaje principal:
  - Existen modos de ejecucion para velocidad o persistencia segun necesidad.
- Contenido recomendado:
  - H2 para desarrollo rapido y MySQL Docker para entorno persistente.
- Guion explicativo:
  - Esta diapositiva debe mostrar criterio operativo: cuando usar cada entorno y que variables son obligatorias para no perder tiempo en arranque.

## Slide 15. Observabilidad y diagnostico

- Mensaje principal:
  - Hay visibilidad base y espacio de mejora en observabilidad avanzada.
- Contenido recomendado:
  - Swagger, OpenAPI, health checks y logs de inicializacion.
- Guion explicativo:
  - Se explica que la base actual permite operar, pero la siguiente fase debe incorporar metricas y trazabilidad mas profunda para incidentes complejos.

## Slide 16. Calidad actual del backend

- Mensaje principal:
  - La base es estable, aunque con brechas claras en pruebas avanzadas.
- Contenido recomendado:
  - Build por reactor, pruebas actuales y vacios de integracion/concurrencia.
- Guion explicativo:
  - Conviene dejar claro que estabilidad no equivale a cobertura completa. La madurez real exige reforzar pruebas de permisos, estados y escenarios concurrentes.

## Slide 17. Riesgos tecnicos principales

- Mensaje principal:
  - Los riesgos estan identificados y gestionables.
- Contenido recomendado:
  - Deriva contrato-codigo, cobertura insuficiente y variabilidad por entorno.
- Guion explicativo:
  - Esta parte no debe sonar alarmista; debe transmitir control. El valor esta en transformar cada riesgo en una accion concreta con evidencia de cierre.

## Slide 18. Decisiones tecnicas y trade-offs

- Mensaje principal:
  - Las decisiones actuales priorizan evolucion controlada.
- Contenido recomendado:
  - Monolito modular, JWT stateless y desacople con DTO/Mapper.
- Guion explicativo:
  - Se explican ventajas y limites de cada decision, mostrando que fueron tomadas por contexto del proyecto y no por tendencia tecnologica.

## Slide 19. Plan de evolucion

- Mensaje principal:
  - El roadmap tecnico esta orientado a robustez y velocidad segura.
- Contenido recomendado:
  - Integracion por roles, validacion automatica de contrato, observabilidad y endurecimiento preproduccion.
- Guion explicativo:
  - Esta diapositiva cierra el bloque tecnico con compromiso de ejecucion: prioridades claras, entregables verificables y foco en reducir riesgo operacional.

## Slide 20. Cierre y preguntas

- Mensaje principal:
  - El backend esta en una base solida y preparado para subir su nivel de madurez.
- Contenido recomendado:
  - Resumen de estado, prioridades y decisiones proximas.
- Guion explicativo:
  - El cierre debe consolidar la narrativa completa: arquitectura ordenada, capacidades reales y plan concreto de mejora. Se abre espacio para preguntas con foco en decisiones y riesgos.

## Preguntas esperadas y respuestas orientativas

1. Por que no microservicios ahora?
- Porque el mayor retorno actual esta en fortalecer calidad, contrato y observabilidad sin introducir complejidad distribuida.

2. Cual es el riesgo tecnico mas importante hoy?
- La distancia potencial entre contrato publicado y comportamiento real, junto con cobertura parcial de integracion en flujos criticos.

3. Que permite escalar sin rediseño inmediato?
- La modularidad por dominio, seguridad stateless y contratos desacoplados del modelo persistente.

4. Cual es el entregable tecnico prioritario del siguiente ciclo?
- Pipeline con validacion automatica de contrato y pruebas de integracion clave por rol y estado de pedido.
