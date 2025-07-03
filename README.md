# 🛍️ EcommerceApp

> **Jetpack Compose · Modular Clean Architecture · Kotlin 2.0**
>
> Aplicación de e-commerce con Catálogo, carrito y perfil de usuario con navegación bottom‑bar, persistencia local y pruebas unitarias con cobertura Kover.

## 📱 Descripción

La app permite simular una tienda online donde el usuario puede:

- Registrarse o iniciar sesión.
- Ver un catálogo de productos (con imagen, nombre, descripción y precio).
- Agregar productos al carrito y modificar cantidades.
- Confirmar una compra y ver un historial de pedidos.
- Ver y editar su perfil de usuario.
- Navegar mediante un Bottom Navigation bar entre Catálogo, Carrito y Perfil.

Actualmente, el proyecto utiliza datos **mockeados**, sin conexión a una API real.

## 👤 ¿Qué puede hacer el usuario?

- 🛒 Explorar productos por nombre o categoría.
- 📦 Agregar productos al carrito, modificar cantidad y eliminar.
- 🧾 Confirmar compras (simuladas).
- 🕒 Ver historial de pedidos.
- 👤 Gestionar su perfil e imagen.
- 🔐 Registrarse o loguearse (simulación).

## 🧱 Tecnologías principales

- Kotlin
- Jetpack Compose (Material 3)
- Navigation Compose
- Hilt (para inyección de dependencias)
- Room (persistencia local)
- ViewModel + StateFlow
- Coil (para carga de imágenes)
- WorkManager (simulado para tareas futuras)
- Arquitectura modular (app, feature, domain, data, core)

## 🧪 Estructura

El proyecto sigue una estructura modular clara. Ejemplo:

```
ecommerce-app/
├── app/
│   ├── ui/ (pantallas y componentes visuales)
│   ├── navigation/
│   └── MainActivity.kt
├── feature/
│   ├── login/
│   ├── register/
│   ├── productlist/
│   ├── cart/
│   └── detail/
├── domain/
│   └── usecase/ y repository/
├── data/
│   └── datasource/ y repository/
├── core/
│   ├── model/
│   ├── navigation/
│   └── utils/
```

## 🚀 Cómo clonar y ejecutar la app

### 🔧 Requisitos previos

- Android Studio (Arctic Fox o superior)
- Kotlin + Gradle con DSL Kotlin
- Java 17 configurado
- Emulador o dispositivo Android

### 📦 Clonar el repositorio

```bash
git clone https://github.com/luciaaldana/ecommerce-app.git
cd ecommerce-app
```

### ▶️ Levantar la app

1. Abrí el proyecto con Android Studio.
2. Esperá que se sincronicen las dependencias.
3. Elegí un emulador o dispositivo físico.
4. Ejecutá el proyecto (`Run > Run app` o Shift + F10).

## 📸 Capturas de pantalla

> 🚧

## 🛠️ Estado

✅ Funcionalidad base completa (login simulado, productos, carrito, confirmación, historial).  
🧪 Pendiente: conexión con API real, validaciones avanzadas, testing, styles.  
📦 Datos actualmente simulados con repositorios fake.


Documentación:
- [Tecnologias](/docs/tecnologias.md)
- [Test](/docs/testing.md)
