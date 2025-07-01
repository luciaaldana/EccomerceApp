# 📚 Tecnologías utilizadas

Este proyecto fue desarrollado siguiendo la arquitectura **MVVM** y emplea una estructura **modularizada** que permite escalar la aplicación fácilmente.

A continuación se detallan las tecnologías utilizadas y su justificación.

## 🔤 Lenguaje y entorno

- **Kotlin**  
  > Lenguaje moderno y oficial para desarrollo Android, con sintaxis concisa y soporte total de Jetpack Compose.

- **Gradle Kotlin DSL**  
  > Permite una configuración más segura y tipada del proyecto.

- **Java 17**  
  > Requerido para compatibilidad con las herramientas modernas de Android.

## 🎨 UI y navegación

- **Jetpack Compose**  
  > Toolkit declarativo para construir interfaces de usuario de forma reactiva.

- **Material 3**  
  > Sistema de diseño de Google adaptado a Compose, con soporte para theming dinámico y componentes modernos.

- **Navigation Compose**  
  > Navegación declarativa y segura entre pantallas usando rutas.

## 🧠 Arquitectura y estado

- **MVVM (Model - View - ViewModel)**  
  > Separa la lógica de presentación, vista y datos. Permite testear y escalar más fácilmente.

- **ViewModel**  
  > Permite manejar el ciclo de vida de la UI y conservar estado.

- **StateFlow**  
  > Flujo reactivo para emitir cambios desde el ViewModel a la UI de forma segura.

## 💉 Inyección de dependencias

- **Hilt**  
  > Framework oficial de Google para DI en Android. Simplifica la provisión de dependencias y ViewModels.

## 💾 Persistencia local

- **Room**  
  > ORM moderno para base de datos SQLite. Permite persistir órdenes y recuperar historial incluso sin conexión.

## 🌐 Red (simulado)

- **Retrofit + Gson (planificado)**  
  > Se utilizará para integrar la API real en fases posteriores. Actualmente se simulan los datos.

## 🖼️ Imágenes

- **Coil**  
  > Carga eficiente de imágenes desde URL en Compose.

## 🛠️ Otros

- **WorkManager**  
  > Planificado para tareas automáticas (actualización de datos, sincronización futura).

## ✅ Conclusión

Esta selección de herramientas permite mantener una arquitectura limpia, escalable y fácil de mantener, ideal para proyectos reales o educativos.
