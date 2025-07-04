# 🛍️ EcommerceApp

Una aplicación completa de e-commerce desarrollada con **Kotlin**, **Jetpack Compose** y arquitectura **MVVM**. La app simula una tienda online con todas las funcionalidades esenciales de un comercio electrónico moderno.

## 📱 Características Principales

### 🔐 Sistema de Autenticación
- **Registro de usuarios** con validación completa de formularios
- **Inicio de sesión** con credenciales persistentes
- **Gestión de perfiles** con edición de información personal
- **Validaciones en tiempo real** para email y contraseñas
- **Usuario de prueba**: `test@test.com` / `12345678`

### 🛒 Catálogo de Productos
- **Visualización en grilla** de productos con imágenes
- **Búsqueda en tiempo real** por nombre y descripción
- **Filtrado por categorías** con dropdown interactivo
- **Vista detallada** de productos con información completa
- **Indicadores especiales** para productos con bebida incluida
- **Integración con API** para obtener productos remotos

### 🛍️ Carrito de Compras
- **Gestión completa** de items con cantidades
- **Cálculo automático** de totales en tiempo real
- **Controles de cantidad** con botones de incremento/decremento
- **Eliminación individual** o limpieza completa del carrito
- **Resumen visual** con imágenes y descripciones
- **Navegación fluida** entre catálogo y carrito

### 📦 Sistema de Pedidos
- **Confirmación de compra** con resumen detallado
- **Historial completo** de pedidos con timestamps
- **Identificación única** de órdenes con UUID
- **Visualización cronológica** con formato de tiempo relativo
- **Gestión de historial** con opción de limpieza

### 🧭 Navegación Intuitiva
- **Bottom Navigation** con acceso rápido a secciones principales
- **Navegación contextual** con botones de retroceso
- **Flujo de navegación optimizado** para la experiencia de compra
- **Estados de navegación** que preservan el contexto del usuario

## 🏗️ Arquitectura Técnica

### 📋 Patrón MVVM
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   View (UI)     │◄───│   ViewModel     │◄───│  Repository     │
│ Jetpack Compose │    │   StateFlow     │    │  Data Layer     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### 🎯 Inyección de Dependencias (Hilt)
- **Módulos especializados** para cada capa de la aplicación
- **Scoped dependencies** para gestión eficiente de memoria
- **Testing support** con módulos de prueba dedicados

### 🌐 Capa de Red
- **Retrofit** para comunicación con APIs REST
- **Moshi** para serialización/deserialización JSON
- **OkHttp** con interceptores para logging
- **Mappers dedicados** para transformación DTO → Domain

### 💾 Gestión de Estado
- **StateFlow** para estado reactivo y type-safe
- **Repository pattern** para abstracción de datos
- **In-memory storage** con persistencia durante la sesión
- **Reactive UI updates** basadas en cambios de estado

## 📁 Estructura del Proyecto

```
app/src/main/java/com/luciaaldana/eccomerceapp/
├── 📱 ui/
│   ├── components/          # Componentes reutilizables
│   │   ├── BottomNavBar.kt
│   │   └── Header.kt
│   ├── screen/              # Pantallas principales
│   │   ├── LoginScreen.kt
│   │   ├── RegisterScreen.kt
│   │   ├── ProductListScreen.kt
│   │   ├── DetailScreen.kt
│   │   ├── CartScreen.kt
│   │   ├── OrderConfirmationScreen.kt
│   │   ├── OrderHistoryScreen.kt
│   │   └── ProfileScreen.kt
│   └── theme/               # Theming y estilos
├── 🧠 viewmodel/            # Lógica de presentación
│   ├── ProductsViewModel.kt
│   ├── CartViewModel.kt
│   ├── LoginViewModel.kt
│   ├── RegisterViewModel.kt
│   ├── OrderHistoryViewModel.kt
│   └── ProfileViewModel.kt
├── 📊 model/                # Modelos de datos
│   ├── data/                # Entidades de dominio
│   │   ├── Product.kt
│   │   ├── CartItem.kt
│   │   ├── Order.kt
│   │   └── MockUser.kt
│   └── repository/          # Interfaces y implementaciones
├── 🌐 data/                 # Capa de datos
│   ├── network/             # APIs y DTOs
│   │   ├── ProductApi.kt
│   │   ├── dto/
│   │   └── mapper/
├── 💉 di/                   # Módulos de Hilt
│   ├── NetworkModule.kt
│   ├── ProductModule.kt
│   ├── CartModule.kt
│   ├── AuthModule.kt
│   └── OrderModule.kt
├── 🧭 navigation/           # Configuración de navegación
├── 🛠️ core/utils/          # Utilidades y extensiones
└── 📱 MainActivity.kt       # Punto de entrada
```

## 🚀 Configuración de Desarrollo

### 📋 Requisitos
- **Android Studio** Iguana o superior
- **JDK 17** configurado
- **Android SDK** nivel 24-35
- **Kotlin** 2.0.21
- **Gradle** 8.9.2

### ⚙️ Variables de Entorno
Crear `local.properties` en la raíz del proyecto:
```properties
RENDER_BASE_URL=https://tu-api.render.com
```

### 🔧 Comandos de Desarrollo

```bash
# Construcción del proyecto
./gradlew build

# Instalación en dispositivo
./gradlew installDebug

# Ejecución de tests unitarios
./gradlew :app:testDebugUnitTest

# Generación de reporte de cobertura
./gradlew :app:koverHtmlReportDebug

# Limpieza del proyecto
./gradlew clean
```

## 🧪 Testing y Calidad

### 📊 Cobertura de Tests
- **Framework**: JUnit 4 con Kotlin
- **Mocking**: Mockito-Kotlin y MockK
- **Coroutines**: Soporte completo para testing asíncrono
- **Flow Testing**: Turbine para validación de StateFlow
- **Coverage**: Kover para reportes detallados

### 🎯 Estrategia de Testing
```bash
# Ejecutar todos los tests
./gradlew test

# Ver reporte de cobertura
open app/build/reports/kover/debug/html/index.html
```

### 🔍 Utilidades de Test
- **MainDispatcherRule** para control de corrutinas
- **Test doubles** para repositories
- **Flujo completo** de testing desde ViewModels hasta UI

## 🛠️ Stack Tecnológico

### 📱 Frontend
- **Jetpack Compose** - UI declarativa moderna
- **Material 3** - Design system de Google
- **Navigation Compose** - Navegación type-safe
- **Coil** - Carga asíncrona de imágenes

### 🔧 Backend/Data
- **Retrofit** - Cliente HTTP type-safe
- **Moshi** - Serialización JSON con Kotlin
- **Hilt** - Inyección de dependencias
- **StateFlow** - Gestión de estado reactiva

### 🧪 Testing
- **JUnit 4** - Framework de testing
- **Mockito/MockK** - Frameworks de mocking
- **Turbine** - Testing de Flows
- **Kover** - Análisis de cobertura

## 🚀 Funcionalidades Implementadas

- ✅ **Autenticación completa** (registro, login, logout)
- ✅ **Catálogo de productos** con búsqueda y filtros
- ✅ **Carrito de compras** con gestión de cantidades
- ✅ **Sistema de pedidos** con confirmación e historial
- ✅ **Gestión de perfil** con edición de datos
- ✅ **Navegación fluida** con bottom navigation
- ✅ **Integración con API** para productos remotos
- ✅ **Testing unitario** con cobertura de código
- ✅ **Arquitectura escalable** con separación de responsabilidades

## 📚 Documentación Adicional

- [📖 Tecnologías Utilizadas](docs/tecnologias.md)
- [🔗 Dependencias Hilt](docs/dependencias_hilt.md)

### 🧪 Testing

- [🧪 Guía de Testing](docs/test/testing.md) - Configuración general y comandos
- [📋 CartViewModel Testing](docs/test/cartviewmodel-testing.md) - Guía detallada de testing de ViewModels
