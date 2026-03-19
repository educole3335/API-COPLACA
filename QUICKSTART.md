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
- Usa las credenciales de arriba en el endpoint `/api/auth/login`

---

## 🗄️ Acceso a Base de Datos

### PhpMyAdmin (GUI)
- **URL:** http://localhost:8081
- **Usuario:** root
- **Contraseña:** 1234qwerty
- **BD:** proyecto
---

## 📚 Documentación Completa

Para más detalles sobre:
- ✅ Qué datos se cargan
- ✅ Cómo personalizar datos
- ✅ Scripts SQL manuales
- ✅ Troubleshooting

👉 **Ver:** [DATABASE_INIT_README.md](./DATABASE_INIT_README.md)

