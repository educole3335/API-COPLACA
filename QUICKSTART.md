# 🚀 COPLACA API - Guía Rápida de Inicio

## ⚡ Inicio Rápido (30 segundos)

```bash
# 1. Iniciar base de datos
cd dokersito
docker-compose up -d

# 2. Esperar 15 segundos (MySQL se está iniciando)

# 3. Ejecutar aplicación
cd ..
mvn spring-boot:run
```

**¡Listo!** La aplicación estará en http://localhost:8080

---

## 📊 Datos que se Cargan Automáticamente

✅ **3 Almacenes** en Canarias (Tenerife, Gran Canaria, La Palma)  
✅ **5 Categorías** de productos  
✅ **20+ Productos** con precios y stock  
✅ **1 Admin** + **2 Clientes** + **2 Repartidores**  
✅ **Todas las direcciones** georeferenciadas con GPS  

---

## 🔑 Credenciales de Ejemplo

### Admin (Acceso Total)
```
📧 admin@coplaca.local
🔐 Admin12345!
```

### Cliente 1
```
📧 cliente@example.com
🔐 Cliente123!
```

### Cliente 2
```
📧 maria@example.com
🔐 Cliente123!
```

### Repartidor 1
```
📧 repartidor@example.com
🔐 Repartidor123!
```

### Repartidor 2
```
📧 ana@example.com
🔐 Repartidor123!
```

---

## 🧪 Probar la API

### Con cURL
```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "cliente@example.com",
    "password": "Cliente123!"
  }'
```

### Con Postman
1. Importa la colección: `postman-collection.json` (si existe)
2. Usa las credenciales de arriba en el endpoint `/api/auth/login`

---

## 🗄️ Acceso a Base de Datos

### PhpMyAdmin (GUI)
- **URL:** http://localhost:8081
- **Usuario:** root
- **Contraseña:** 1234qwerty
- **BD:** proyecto

```bash
# O directamente con MySQL CLI
docker exec -it dokersito-db-1 mysql -uroot -p1234qwerty proyecto
```

---

## 📚 Documentación Completa

Para más detalles sobre:
- ✅ Qué datos se cargan
- ✅ Cómo personalizar datos
- ✅ Scripts SQL manuales
- ✅ Troubleshooting

👉 **Ver:** [DATABASE_INIT_README.md](./DATABASE_INIT_README.md)

---

## 🛑 Problemas Comunes

### ❌ "Cannot connect to MySQL"
```bash
# Reinicia Docker
docker-compose down
docker-compose up -d
# Espera 30 segundos
```

### ❌ "Port 3306 already in use"
```bash
# Cambiar puerto en dokersito/docker-compose.yml
# Línea: - "3306:3306" → - "3307:3306"
```

### ❌ "Hibernate error: table not found"
```bash
# Recrear tablas
mvn clean install
```

---

## 📦 Stack Tecnológico

- **Java 21** + **Spring Boot 4.0.2**
- **MySQL 8.0** (con Docker)
- **JPA/Hibernate** para ORM
- **JWT** para autenticación
- **Lombok** para reducir boilerplate

---

## 🔧 Configuración de Base de Datos

En `application.properties`:

**Desarrollo (H2 en memoria):**
```properties
# (Por defecto)
spring.datasource.url=jdbc:h2:mem:coplaca
```

**Producción (MySQL en Docker):**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/proyecto
spring.datasource.username=usuario_proyecto
spring.datasource.password=1234qwerty
```

---

## 🚀 Próximos Pasos

1. [ ] Ejecutar la aplicación (`mvn spring-boot:run`)
2. [ ] Probar login con una credencial
3. [ ] Explorar endpoints en Swagger (si está habilitado): http://localhost:8080/swagger-ui.html
4. [ ] Revisar datos en PhpMyAdmin: http://localhost:8081
5. [ ] Leer [DATABASE_INIT_README.md](./DATABASE_INIT_README.md) para personalizar

---

## 📞 Contacto

Para problemas o preguntas:
- Revisar logs: `mvn spring-boot:run`
- Ver Docker logs: `docker-compose logs`
- Consultar [DATABASE_INIT_README.md](./DATABASE_INIT_README.md)

---

**Happy coding! 🎉**
