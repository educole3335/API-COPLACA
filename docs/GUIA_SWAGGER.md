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

## 5. Mapa completo de endpoints

Esta es la lista completa que deberia verse organizada en Swagger. La idea es que cada grupo coincida con un dominio funcional.

### 5.1 Autenticacion y sesion

- `POST /auth/login` - autenticacion y obtencion de JWT.
- `POST /auth/signup` - registro publico de cliente con direccion.

### 5.2 Landing y recomendaciones

- `GET /landing` - portada general con contenido dinamico.
- `GET /landing/seasonal` - seleccion estacional.
- `GET /landing/recommendations` - recomendaciones personalizadas o anonimas.
- `GET /landing/health` - verificacion basica de la capa landing.

### 5.3 Catalogo y ofertas

#### Productos

- `GET /api/v1/products` - lista de productos activos.
- `GET /api/v1/products/{id}` - detalle de producto.
- `GET /api/v1/products/category/{categoryId}` - productos por categoria.
- `GET /api/v1/products/search?query=texto` - busqueda textual.
- `POST /api/v1/products` - alta de producto.
- `PUT /api/v1/products/{id}` - actualizacion de producto.
- `DELETE /api/v1/products/{id}` - desactivacion funcional.
- `PATCH /api/v1/products/{id}/stock?delta=valor` - ajuste incremental de stock.
- `PATCH /api/v1/products/{id}/price?value=valor` - ajuste de precio.

#### Categorias

- `GET /api/v1/categories` - listado de categorias.
- `GET /api/v1/categories/{id}` - detalle de categoria.
- `POST /api/v1/categories` - crear categoria.
- `PUT /api/v1/categories/{id}` - actualizar categoria.
- `DELETE /api/v1/categories/{id}` - eliminar categoria.

#### Ofertas estacionales

- `GET /api/v1/offers` - listar ofertas activas.
- `GET /api/v1/offers/{id}` - detalle de oferta.
- `POST /api/v1/offers` - crear oferta.
- `PUT /api/v1/offers/{id}` - editar oferta.
- `DELETE /api/v1/offers/{id}` - desactivar oferta.

### 5.4 Pedidos y ETA

#### Pedidos

- `POST /api/v1/orders` - crear pedido.
- `GET /api/v1/orders/me` - pedidos del usuario autenticado.
- `GET /api/v1/orders/{id}` - detalle de pedido con control interno.
- `GET /api/v1/orders/{id}/eta` - consultar ETA del pedido.
- `GET /api/v1/orders/customer/{customerId}` - pedidos por cliente.
- `GET /api/v1/orders/warehouse/{warehouseId}/pending` - pedidos pendientes.
- `GET /api/v1/orders/warehouse/{warehouseId}/all` - pedidos del almacen.
- `GET /api/v1/orders/warehouse/{warehouseId}/confirmed` - pedidos confirmados.
- `GET /api/v1/orders/warehouse/{warehouseId}/in-transit` - pedidos en transito.
- `GET /api/v1/orders/warehouse/{warehouseId}/stats?period=day|week|month` - estadisticas operativas.
- `GET /api/v1/orders/delivery-agent/{deliveryAgentId}` - pedidos de un repartidor.
- `PUT /api/v1/orders/{orderId}/assign/{deliveryAgentId}` - asignar pedido a reparto.
- `PUT /api/v1/orders/{orderId}/status?status=valor` - cambiar estado de pedido.
- `PUT /api/v1/orders/{orderId}/accept` - aceptar pedido asignado.
- `PUT /api/v1/orders/{orderId}/reject?reason=texto` - rechazar pedido asignado.
- `PUT /api/v1/orders/{orderId}/confirm-loaded` - confirmar carga para salida.
- `PUT /api/v1/orders/{orderId}/deliver` - marcar entrega.
- `PUT /api/v1/orders/{orderId}/cancel?reason=texto` - cancelar pedido.

#### ETA

- `GET /api/v1/eta/order/{orderId}/calculate` - calcular ETA actual.
- `GET /api/v1/eta/order/{orderId}` - recuperar ultimo ETA calculado.
- `POST /api/v1/eta/delivery-agent/{deliveryAgentId}/recalculate` - recalcular ETA de un repartidor.

### 5.5 Usuarios y almacenes

#### Usuarios

- `GET /api/v1/users/me` - perfil propio.
- `PUT /api/v1/users/me` - actualizar perfil propio.
- `DELETE /api/v1/users/me` - desactivar cuenta propia.
- `PATCH /api/v1/users/me/delivery-status?status=AT_WAREHOUSE|DELIVERING|OFFLINE` - actualizar estado operativo.
- `GET /api/v1/users/{id}` - detalle de usuario.
- `PUT /api/v1/users/{id}` - actualizacion administrativa.
- `DELETE /api/v1/users/{id}` - desactivar usuario.

#### Almacenes

- `GET /api/v1/warehouses` - listar almacenes.
- `GET /api/v1/warehouses/{id}` - detalle de almacen.
- `GET /api/v1/warehouses/{id}/delivery-agents` - repartidores por almacen.
- `POST /api/v1/warehouses` - crear almacen.
- `PUT /api/v1/warehouses/{id}` - actualizar almacen.

### 5.6 Administracion

#### Gestion de usuarios

- `GET /api/v1/admin/users` - listado de usuarios.
- `GET /api/v1/admin/users/active` - usuarios activos.
- `GET /api/v1/admin/users/disabled` - usuarios deshabilitados.
- `PUT /api/v1/admin/users/{id}/roles` - reasignar roles.
- `POST /api/v1/admin/users/internal` - crear usuario interno.
- `POST /api/v1/admin/users/{id}/reactivate` - reactivar usuario.
- `DELETE /api/v1/admin/users/{id}` - desactivar usuario.

#### Analitica y control

- `GET /api/v1/admin/stats/top-products` - ranking de productos.
- `GET /api/v1/admin/stats/products-detailed` - analitica detallada de productos.
- `GET /api/v1/admin/stats/orders?period=valor` - resumen de pedidos.
- `GET /api/v1/admin/stats/users` - resumen de usuarios.
- `GET /api/v1/admin/orders/today` - pedidos del dia.
- `GET /api/v1/admin/health` - health interno de la capa admin.

## 6. Como documentar un controlador nuevo

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

## 7. Como documentar DTOs

Los DTOs deben explicar el contrato real de la API.

Recomendaciones:

- usa `@Schema(description = ...)` en campos importantes;
- marca con `requiredMode = REQUIRED` los campos obligatorios;
- añade `example` para valores tipicos;
- documenta listas con `@ArraySchema` cuando el contenido lo justifique;
- si un campo tiene enum, explica el significado funcional, no solo el nombre tecnico.

## 8. Como documentar entidades y enums

Hay entidades del dominio que tambien aparecen documentadas porque Swagger las usa en ejemplos o respuestas internas.

Hazlo asi:

- documenta el proposito del objeto, no solo el nombre del campo;
- en enums, explica el contexto de uso de cada valor;
- evita descripciones redundantes o demasiado obvias;
- si el enum representa estados de negocio, describe la transicion o el impacto operativo.

## 9. Dependencias necesarias

Si un modulo usa anotaciones de Swagger como `@Schema`, debe tener disponible la libreria comun de springdoc en ese modulo.

En esta base de codigo eso es importante porque las anotaciones se usan en modulos que no son `rest-server`.

Regla:

- `rest-server` mantiene la UI y la configuracion principal;
- los modulos de dominio deben tener acceso a las anotaciones si sus clases las usan.

## 10. Como mantener el Swagger "bonito"

La parte visual vive en la configuracion de Swagger UI:

- ordenar tags alfabeticamente;
- ordenar operaciones alfabeticamente;
- expandir por lista para evitar ruido inicial;
- mostrar duracion de requests;
- activar filtro de busqueda.

Si añades nuevos grupos, manten la misma logica de nombres para que el panel siga siendo facil de navegar.

## 11. Flujo recomendado al crear o mejorar un endpoint

1. Crea o ajusta el DTO.
2. Define el contrato de entrada y salida.
3. Documenta el controlador con `@Operation` y respuestas.
4. Revisa si el DTO necesita `@Schema` o ejemplos.
5. Si el cambio toca varios dominios, revisa los grupos de `OpenApiConfig`.
6. Actualiza `docs/REFERENCIA_API.md` si cambia el contrato funcional.
7. Si el endpoint es publico o versionado, sincroniza `docs/contracts/v1/openapi.yaml`.
8. Valida Swagger UI en runtime.

## 12. Checklist rapido antes de dar por bueno un cambio Swagger

- El endpoint aparece en el grupo correcto.
- La descripcion dice que hace la operacion.
- Las respuestas 200/201/400/401/403/404 estan claras.
- Los DTOs muestran campos y ejemplos utiles.
- El endpoint protegido permite probarse con Bearer token.
- No hay anotaciones Swagger rotas por dependencia faltante.
- La documentacion oficial del repositorio sigue alineada.

## 13. Errores comunes

### Swagger UI no abre

Verifica que el backend este levantado y que `springdoc-openapi-starter-webmvc-ui` siga en `rest-server`.

### Falta `@Schema` en un modulo de dominio

Comprueba que el modulo tenga acceso a `springdoc-openapi-starter-common`.

### El endpoint sale sin clasificar

Revisa que tenga `@Tag` y que la ruta encaje en alguno de los `GroupedOpenApi`.

### No funciona la autenticacion en la UI

Usa `POST /auth/login`, copia el JWT y registralo en `Authorize` como `Bearer <token>`.

## 14. Relacion con el resto de la documentacion

Esta guia complementa:

- [docs/GUIA_OPERATIVA_BACKEND.md](GUIA_OPERATIVA_BACKEND.md)
- [docs/REFERENCIA_API.md](REFERENCIA_API.md)
- [docs/FLUJO_API_PASO_A_PASO.md](FLUJO_API_PASO_A_PASO.md)
- [docs/INDICE_DOCUMENTACION.md](INDICE_DOCUMENTACION.md)
