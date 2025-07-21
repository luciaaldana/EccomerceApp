# 🚀 API Setup Guide para Frontend Developers

Esta guía te ayudará a levantar la API de que usa la app para conectarla con tu frontend.

## 📋 Requisitos Previos

### Node.js Version
- **Node.js 16.x o 18.x**
- Verificar versión: `node --version`

### MongoDB Atlas
- Cuenta gratuita en [MongoDB Atlas](https://www.mongodb.com/atlas/database)
- Cluster creado (M0 Tier - gratuito)
- Usuario con permisos de lectura/escritura
- Network Access configurado para `0.0.0.0/0`

---

## 🛠️ Instalación Paso a Paso

### 1. Clonar el Repositorio
```bash
git clone git@github.com:luciaaldana/EccomerceApi.git
cd EccomerceApi
```

### 2. Instalar Dependencias
```bash
npm install
```

### 3. Configurar Variables de Entorno
Crear archivo `.env` en la raíz del proyecto:
```bash
MONGODB_URI=mongodb+srv://tu-usuario:tu-password@cluster0.xxxxxx.mongodb.net/PeyaDB?retryWrites=true&w=majority
PORT=10000
```

### 4. Poblar la Base de Datos
```bash
npm run seed
```

### 5. Levantar el Servidor
```bash
npm run dev
```

---

## ⚠️ Solución de Problemas Comunes

### Error: "Unexpected token '??='"
**Causa:** Versión de MongoDB/Mongoose muy nueva para tu Node.js

**Solución:**
```bash
npm uninstall mongodb mongoose
npm install mongodb@4.17.2 mongoose@6.12.7
```

### Error: "Object.hasOwn is not a function"
**Causa:** Express 5.x no es compatible con Node.js 16-18

**Solución:**
```bash
npm uninstall express
npm install express@4.18.2
```

### Error: "EADDRINUSE: address already in use"
**Causa:** Puerto ocupado

**Solución:**
```bash
# macOS/Linux
lsof -ti:4000 | xargs kill -9

# Windows
netstat -ano | findstr :10000
taskkill /PID <PID> /F
```

### Error: "DB connection error"
**Causa:** Problemas con MongoDB Atlas

**Solución:**
1. Verificar que `MONGODB_URI` esté correctamente configurado
2. Asegurar que tu IP esté en Network Access de MongoDB Atlas
3. Verificar usuario/contraseña en la cadena de conexión

---

## 🔗 Endpoints Disponibles

### Base URL
```
http://localhost:4000
```

### Foods (Productos)
- `GET /foods` - Obtener todos los productos
- `POST /foods` - Crear producto
- `GET /foods/:id` - Obtener producto por ID
- `GET /foods/categories` - Obtener lista de categorías de todos los productos
- `PUT /foods/:id` - Actualizar producto
- `DELETE /foods/:id` - Eliminar producto

### Users (Usuarios)
- `POST /users/register` - Registrar usuario
- `POST /users/login` - Login de usuario
- `GET /users/:email` - Obtener usuario por email

### Orders (Pedidos)
- `POST /orders` - Crear pedido
- `GET /orders` - Obtener todos los pedidos

### Documentación
- `GET /api-docs` - Swagger UI (documentación interactiva)
- `GET /ping` - Health check

---

## 📊 Ejemplo de Uso desde Frontend

### Obtener productos
```javascript
fetch('http://localhost:10000/foods')
  .then(response => response.json())
  .then(data => console.log(data));
```

### Crear pedido
```javascript
fetch('http://localhost:10000/orders', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    orderId: 'ORDER_123',
    productIds: [
      {
        name: 'Pizza Margarita',
        description: 'Pizza con mozzarella y tomate',
        imageUrl: 'https://example.com/pizza.jpg',
        price: 12.99,
        hasDrink: true,
        quantity: 2
      }
    ],
    total: 25.98,
    timestamp: Date.now()
  })
})
.then(response => response.json())
.then(data => console.log(data));
```

---

## 🔧 Configuración para Producción

### Render (Recomendado)
1. Crear cuenta en [Render](https://render.com)
2. Conectar repositorio de GitHub
3. Configurar:
   - **Build Command:** `npm install`
   - **Start Command:** `npm start`
   - **Environment Variables:**
     - `MONGODB_URI`: tu cadena de conexión
     - `PORT`: se asigna automáticamente

### Variables de Entorno en Producción
```
MONGODB_URI=mongodb+srv://...
PORT=10000
```

---

## 📚 Versiones Compatibles

### Package.json Recomendado
```json
{
  "dependencies": {
    "cors": "^2.8.5",
    "dotenv": "^16.5.0",
    "express": "^4.18.2",
    "mongodb": "^4.17.2",
    "mongoose": "^6.12.7",
    "swagger-jsdoc": "^6.2.8",
    "swagger-ui-express": "^5.0.1"
  },
  "devDependencies": {
    "nodemon": "^3.1.10"
  }
}
```

---

## 📞 Troubleshooting

### Si el servidor no arranca
1. Verificar Node.js version: `node --version`
2. Verificar puerto disponible: `lsof -i:10000`
3. Verificar variables de entorno en `.env`
4. Verificar conexión a MongoDB Atlas

### Si hay errores de dependencias
```bash
rm -rf node_modules package-lock.json
npm install
```

### Para ver logs detallados
```bash
DEBUG=* npm run dev
```

---

## ✅ Checklist de Verificación

- [ ] Node.js 16.x o 18.x instalado
- [ ] MongoDB Atlas cluster creado
- [ ] Archivo `.env` configurado
- [ ] `npm install` ejecutado exitosamente
- [ ] `npm run seed` ejecutado sin errores
- [ ] `npm run dev` levanta el servidor
- [ ] `http://localhost:10000/ping` responde "pong"
- [ ] `http://localhost:10000/api-docs` muestra Swagger UI
- [ ] `http://localhost:10000/foods` devuelve productos

---

## 📱 Configuración para Android (Emulador vs Dispositivo Físico)

### 🖥️ **Emulador de Android**

Si estás usando el **emulador de Android Studio**, configura en `local.properties`:

```properties
RENDER_BASE_URL=http://10.0.2.2:10000
```

**¿Por qué `10.0.2.2`?**
- En el emulador, `localhost` se refiere al emulador mismo, no a tu computadora
- Android Studio mapea automáticamente `10.0.2.2` → `localhost` de tu computadora
- `10.0.2.2:10000` en el emulador = `localhost:10000` en tu computadora

### 📱 **Dispositivo Físico**

Si estás usando un **dispositivo físico conectado**, usa tu IP local:

```properties
RENDER_BASE_URL=http://XXX.XXX.X.XXX:10000
```

**Para encontrar tu IP local:**

```bash
# En macOS/Linux
ifconfig | grep "inet " | grep -v 127.0.0.1

# En Windows
ipconfig | findstr "IPv4"
```

Busca una IP que empiece con `XXX.XXX.x.x` o `10.x.x.x`.

### 🔧 **Configuración del Servidor para Android**

Asegúrate de que tu servidor escuche en todas las interfaces:

```javascript
// ✅ CORRECTO - Escucha en todas las interfaces
app.listen(10000, '0.0.0.0', () => {
    console.log('Server running on port 10000');
});

// ❌ INCORRECTO - Solo escucha en localhost
app.listen(10000, 'localhost', () => {
    console.log('Server running on port 10000');
});
```

### 🚫 **Problemas Comunes con Android**

#### ❌ **"Failed to connect" con emulador**
- **Problema**: Usaste `localhost` en lugar de `10.0.2.2`
- **Solución**: Cambiar a `http://10.0.2.2:10000`

#### ❌ **"Connection refused" con dispositivo físico**
- **Problema**: IP incorrecta o firewall bloqueando
- **Solución**: 
  1. Verificar IP con `ifconfig`
  2. Asegurar que dispositivo y computadora están en la misma red WiFi
  3. Verificar que no hay firewall bloqueando el puerto

#### ❌ **"Network security config" error**
- **Problema**: Android bloquea HTTP en producción
- **Solución**: El proyecto ya tiene configurado `network_security_config.xml` para permitir HTTP en desarrollo

### 🔄 **Después de Cambiar la Configuración**

1. **Clean y Rebuild el proyecto:**
   ```bash
   ./gradlew clean
   ./gradlew build
   ```

2. **En Android Studio:**
   - **Build** → **Clean Project**
   - **Build** → **Rebuild Project**

3. **Ejecutar la app nuevamente**

### 🧪 **Verificar Conexión desde Android**

**Desde el emulador (usando adb):**
```bash
adb shell
curl http://10.0.2.2:10000/foods
```

**Para debug de conexión, revisa los logs:**
```bash
adb logcat | grep -E "(HTTP|Connection|Network)"
```

---

¡Con estos pasos tu API debería estar funcionando correctamente y lista para conectar con tu frontend! 🎉
