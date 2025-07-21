# 📚 Tecnologías utilizadas

Este proyecto fue desarrollado siguiendo los **patrones de modularización oficiales de Android** con arquitectura **Clean Architecture** que sigue el flujo `Feature → Domain → Data → Core`.

La estructura multi-módulo mejora los tiempos de compilación, permite testing aislado y facilita la escalabilidad del proyecto.

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

## 🧠 Arquitectura Multi-Módulo

- **Clean Architecture (Feature → Domain → Data → Core)**  
  > Separación clara de responsabilidades con reglas de dependencia estrictas.

- **Modularización por características**  
  > Cada feature (login, cart, profile) en su propio módulo para compilación paralela.

- **MVVM + Repository Pattern**  
  > ViewModels en módulos `feature:`, repositorios implementados en módulos `data:`.

- **StateFlow**  
  > Flujo reactivo para emitir cambios desde el ViewModel a la UI de forma segura.

- **Inyección de dependencias distribuida**  
  > Cada módulo `data:` tiene su propio módulo Hilt para DI aislada.

## 💉 Inyección de dependencias

- **Hilt**  
  > Framework oficial de Google para DI en Android. Simplifica la provisión de dependencias y ViewModels.

## 💾 Persistencia local

- **In-memory storage**  
  > Almacenamiento en memoria para desarrollo rápido. Cada repositorio mantiene su estado durante la sesión.

## 🌐 Capa de Red

- **Retrofit + Moshi**  
  > Cliente HTTP type-safe con serialización JSON moderna usando KSP.

- **DTOs y Mappers**  
  > Transformación clara entre modelos de red (API) y modelos de dominio.

- **OkHttp + Interceptors**  
  > Cliente HTTP con logging automático para debugging.

## 🖼️ Imágenes

- **Coil**  
  > Carga eficiente de imágenes desde URL en Compose.

- **Cloudinary**  
  > Almacenamiento y carga de imágenes en la nube desde la galería o cámara del dispositivo.

## 🛠️ Herramientas de Build y Testing

- **Gradle Kotlin DSL**  
  > Configuración de build type-safe con `libs.versions.toml` centralizado.

- **KSP (Kotlin Symbol Processing)**  
  > Procesamiento de anotaciones más eficiente que Kapt para Moshi.

- **Kover**  
  > Análisis de cobertura de tests por módulo.

- **JUnit 4 + MockK + Turbine**  
  > Stack completo de testing con soporte para StateFlow y corrutinas.

## 🎯 Beneficios de la Modularización

- **Compilación paralela**: Los módulos se compilan en paralelo mejorando tiempos de build
- **Separación de responsabilidades**: Cada módulo tiene un propósito específico
- **Testing aislado**: Se pueden testear módulos independientemente
- **Reutilización**: Módulos `core:` compartidos entre features
- **Escalabilidad**: Fácil agregar nuevas features sin afectar las existentes
