# Guia Swagger/OpenAPI de API COPLACA

Guia practica para entender, mantener y ampliar la documentacion Swagger del backend.

## 1. Objetivo

El objetivo de esta configuracion es que Swagger no sea solo una lista de rutas, sino una vista util para:

- encontrar rapido endpoints por dominio;
- entender que hace cada operacion;
- ver que datos necesita y que devuelve;
- probar autenticacion JWT desde Swagger UI;
- mantener alineada la documentacion con el codigo.

## 2. Donde esta definido

Los puntos clave estan repartidos asi:

- [api-coplaca/rest-server/src/main/java/com/coplaca/apirest/config/OpenApiConfig.java](../api-coplaca/rest-server/src/main/java/com/coplaca/apirest/config/OpenApiConfig.java)
- [api-coplaca/rest-server/src/main/resources/application.properties](../api-coplaca/rest-server/src/main/resources/application.properties)
- Controladores del modulo `rest-server`.
- DTOs y entidades de `user-domain`, `product-domain`, `order-domain` y `recommendation-domain`.

## 3. Como esta organizado

Swagger UI esta preparado para mostrar la API por grupos funcionales:

- Autenticacion y sesion.
- Landing y recomendaciones.
- Catalogo y ofertas.
- Pedidos y ETA.
- Usuarios y almacenes.
- Administracion.

Eso se define en `OpenApiConfig` con `GroupedOpenApi` y hace que Swagger no mezcle todos los endpoints en una sola lista larga.

## 4. Como usar Swagger UI

1. Arranca el backend.
2. Abre `http://localhost:8080/swagger-ui/index.html`.
3. Usa el selector de grupos para entrar en el dominio que te interesa.
4. Activa el filtro de busqueda si quieres localizar una ruta por nombre, recurso o verbo HTTP.
5. Ejecuta primero `POST /auth/login` si necesitas probar rutas protegidas.
6. Usa el boton `Authorize` para pegar el JWT Bearer token.

## 5. Como documentar un controlador nuevo

Cuando añadas un endpoint, documenta estas piezas en el controlador:

- `@Tag` para identificar el dominio.
- `@Operation` para explicar la accion.
- `@Parameter` para path, query o header parameters.
- `@ApiResponses` para dejar claras las respuestas esperadas.
- `@RequestBody` documentado con el DTO correcto.
- `@Schema` en el DTO de entrada y salida.

Regla practica: el nombre del endpoint debe contar la accion, y la descripcion debe contar la intencion de negocio.

Ejemplo de enfoque correcto:

- mal: "Obtener datos".
- bien: "Listar pedidos pendientes del almacen".

## 6. Como documentar DTOs

Los DTOs deben explicar el contrato real de la API.

Recomendaciones:

- usa `@Schema(description = ...)` en campos importantes;
- marca con `requiredMode = REQUIRED` los campos obligatorios;
- añade `example` para valores tipicos;
- documenta listas con `@ArraySchema` cuando el contenido lo justifique;
- si un campo tiene enum, explica el significado funcional, no solo el nombre tecnico.

## 7. Como documentar entidades y enums

Hay entidades del dominio que tambien aparecen documentadas porque Swagger las usa en ejemplos o respuestas internas.

Hazlo asi:

- documenta el proposito del objeto, no solo el nombre del campo;
- en enums, explica el contexto de uso de cada valor;
- evita descripciones redundantes o demasiado obvias;
- si el enum representa estados de negocio, describe la transicion o el impacto operativo.

## 8. Dependencias necesarias

Si un modulo usa anotaciones de Swagger como `@Schema`, debe tener disponible la libreria comun de springdoc en ese modulo.

En esta base de codigo eso es importante porque las anotaciones se usan en modulos que no son `rest-server`.

Regla:

- `rest-server` mantiene la UI y la configuracion principal;
- los modulos de dominio deben tener acceso a las anotaciones si sus clases las usan.

## 9. Como mantener el Swagger "bonito"

La parte visual vive en la configuracion de Swagger UI:

- ordenar tags alfabeticamente;
- ordenar operaciones alfabeticamente;
- expandir por lista para evitar ruido inicial;
- mostrar duracion de requests;
- activar filtro de busqueda.

Si añades nuevos grupos, manten la misma logica de nombres para que el panel siga siendo facil de navegar.

## 10. Flujo recomendado al crear o mejorar un endpoint

1. Crea o ajusta el DTO.
2. Define el contrato de entrada y salida.
3. Documenta el controlador con `@Operation` y respuestas.
4. Revisa si el DTO necesita `@Schema` o ejemplos.
5. Si el cambio toca varios dominios, revisa los grupos de `OpenApiConfig`.
6. Actualiza `docs/REFERENCIA_API.md` si cambia el contrato funcional.
7. Si el endpoint es publico o versionado, sincroniza `docs/contracts/v1/openapi.yaml`.
8. Valida Swagger UI en runtime.

## 11. Checklist rapido antes de dar por bueno un cambio Swagger

- El endpoint aparece en el grupo correcto.
- La descripcion dice que hace la operacion.
- Las respuestas 200/201/400/401/403/404 estan claras.
- Los DTOs muestran campos y ejemplos utiles.
- El endpoint protegido permite probarse con Bearer token.
- No hay anotaciones Swagger rotas por dependencia faltante.
- La documentacion oficial del repositorio sigue alineada.

## 12. Errores comunes

### Swagger UI no abre

Verifica que el backend este levantado y que `springdoc-openapi-starter-webmvc-ui` siga en `rest-server`.

### Falta `@Schema` en un modulo de dominio

Comprueba que el modulo tenga acceso a `springdoc-openapi-starter-common`.

### El endpoint sale sin clasificar

Revisa que tenga `@Tag` y que la ruta encaje en alguno de los `GroupedOpenApi`.

### No funciona la autenticacion en la UI

Usa `POST /auth/login`, copia el JWT y registralo en `Authorize` como `Bearer <token>`.

## 13. Relacion con el resto de la documentacion

Esta guia complementa:

- [docs/GUIA_OPERATIVA_BACKEND.md](GUIA_OPERATIVA_BACKEND.md)
- [docs/REFERENCIA_API.md](REFERENCIA_API.md)
- [docs/FLUJO_API_PASO_A_PASO.md](FLUJO_API_PASO_A_PASO.md)
- [docs/INDICE_DOCUMENTACION.md](INDICE_DOCUMENTACION.md)
