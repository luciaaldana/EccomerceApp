# 🛍️ EcommerceApp

Una aplicación completa de e-commerce desarrollada con **Kotlin**, **Jetpack Compose** y arquitectura **MVVM**. La app simula una tienda online con todas las funcionalidades esenciales de un comercio electrónico moderno.

<img width="1920" height="1080" alt="Purple Pink Gradient Mobile Application Presentation" src="https://github.com/user-attachments/assets/090075c2-fc90-4222-99ec-e7ba3c46320e" />

## 📱 Características Principales

EcommerceApp permite a los usuarios:

- Registrarse e iniciar sesión.
- Explorar un catálogo de productos actualizable vía API.
- Agregar productos al carrito con control de cantidad.
- Visualizar un resumen del pedido.
- Confirmar la compra (orden simulada).
- Editar su perfil y ver historial de pedidos.

### 🔐 Sistema de Autenticación
- **Registro de usuarios** con validación completa de formularios
- **Inicio de sesión** con credenciales persistentes
- **Gestión de perfiles** con edición de información personal
- **Validaciones en tiempo real** para email y contraseñas
- **Usuario de prueba**: `test@demo.com` / `12345678` (*) usando la misma api de los docs.
- **Imágenes se guardan en Claudinary** si se carga la foto de perfil en el registro.

<img width="1920" height="1080" alt="Purple Pink Gradient Mobile Application Presentation (1)" src="https://github.com/user-attachments/assets/6091f246-57e4-45e8-95cd-b55ef4faf83c" />

<img width="1920" height="1080" alt="Purple Pink Gradient Mobile Application Presentation (2)" src="https://github.com/user-attachments/assets/8b7aedf4-7a72-4817-b487-a5e5045cba7d" />

### 🛒 Catálogo de Productos
- **Visualización en grilla** de productos con imágenes
- **Búsqueda en tiempo real** por nombre y descripción
- **Filtrado por categorías** con dropdown interactivo
- **Vista detallada** de productos con información completa
- **Indicadores especiales** para productos con bebida incluida
- **Integración con API** para obtener productos remotos
- Sincronización automática de productos con **WorkManager**

<img width="1920" height="1080" alt="Purple Pink Gradient Mobile Application Presentation (3)" src="https://github.com/user-attachments/assets/7ab8a847-4391-43fa-aa2e-3a34cf11a6ca" />

<img width="1920" height="1080" alt="Purple Pink Gradient Mobile Application Presentation (4)" src="https://github.com/user-attachments/assets/73b7529c-8642-4bbd-906c-acbf47849071" />

### 🛍️ Carrito de Compras
- **Gestión completa** de items con cantidades
- **Cálculo automático** de totales en tiempo real
- **Controles de cantidad** con botones de incremento/decremento
- **Eliminación individual** o limpieza completa del carrito
- **Resumen visual** con imágenes y descripciones
- **Navegación fluida** entre catálogo y carrito

<img width="1920" height="1080" alt="Purple Pink Gradient Mobile Application Presentation (5)" src="https://github.com/user-attachments/assets/68a977a5-3bea-4f00-9215-34895630bf20" />

### 📦 Sistema de Pedidos
- **Confirmación de compra** con resumen detallado
- **Historial completo** de pedidos con timestamps
- **Identificación única** de órdenes con UUID
- **Visualización cronológica** con formato de tiempo relativo
- **Gestión de historial** con opción de limpieza

<img width="1920" height="1080" alt="Purple Pink Gradient Mobile Application Presentation (6)" src="https://github.com/user-attachments/assets/49954925-e53c-4152-a5af-58967e159ce6" />

### Perfil del usuario
- **Datos del usuario** con opción de editar
- **Imágenes se guardan en Claudinary** si se carga o edita la foto de perfil en el perfil.
- **Acceso al historial** de pedidos
- **Seleccionar tema de la aplicación** entre light, dark y system mode (system por default).
- logout

<img width="1920" height="1080" alt="Purple Pink Gradient Mobile Application Presentation (7)" src="https://github.com/user-attachments/assets/da7f1d17-c45b-48e0-a85e-004ee287b7b6" />

### 🧭 Navegación Intuitiva
- **Bottom Navigation** con acceso rápido a secciones principales
- **Navegación contextual** con botones de retroceso
- **Flujo de navegación optimizado** para la experiencia de compra
- **Estados de navegación** que preservan el contexto del usuario

## 🏗️ Arquitectura Multi-Módulo

### 📋 Arquitectura Clean (Feature → Domain → Data → Core)
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Feature       │────│     Domain      │────│      Data       │────│      Core       │
│   UI + VM       │    │   Use Cases     │    │  Repositories   │    │  Models + Utils │
│   (Compose)     │    │  (Interfaces)   │    │ (Implementation)│    │   (Shared)      │
└─────────────────┘    └─────────────────┘    └─────────────────┘    └─────────────────┘
```

**Reglas de Dependencias (solo "hacia abajo"):**
- `Feature` → `Domain` → `Core`
- `Data` implementa `Domain`, pero NO depende de `Feature`
- `Core` no depende de nadie (compartido)

### 🧩 Módulos por Capa

**🎨 Feature Modules (UI + ViewModels):**
- `:feature:login` - Autenticación de usuarios
- `:feature:register` - Registro de nuevos usuarios  
- `:feature:home` - Catálogo y detalles de productos
- `:feature:cart` - Carrito de compras
- `:feature:profile` - Perfil y historial de pedidos

**🔗 Domain Modules (Interfaces de negocio):**
- `:domain:auth` - Interfaces de autenticación
- `:domain:product` - Interfaces de productos
- `:domain:cart` - Interfaces de carrito y pedidos

**💾 Data Modules (Implementaciones):**
- `:data:auth` - Implementación de autenticación
- `:data:product` - Implementación de productos (API + mappers)
- `:data:cart` - Implementación de carrito y pedidos

**⚙️ Core Modules (Compartidos):**
- `:core:model` - Modelos de datos y utilidades
- `:core:ui` - Componentes UI reutilizables y theme
- `:core:navigation` - Componentes de navegación

**📱 App Module:**
- `:app` - Configuración principal, navegación y DI

### 🎯 Beneficios de la Modularización
- **Compilación paralela** - Mejores tiempos de build
- **Separación de responsabilidades** - Cada módulo tiene un propósito claro
- **Reutilización** - Core modules compartidos entre features
- **Testing aislado** - Cada módulo se puede testear independientemente
- **Escalabilidad** - Fácil agregar nuevas features sin afectar existentes

### 🌐 Capa de Red
- **Retrofit** para comunicación con APIs REST
- **Moshi** para serialización/deserialización JSON (KSP)
- **OkHttp** con interceptores para logging
- **DTOs y Mappers** para transformación API → Domain

### 💾 Gestión de Estado
- **StateFlow** para estado reactivo y type-safe
- **Repository pattern** implementado en módulos `data:`
- **In-memory storage** con persistencia durante la sesión
- **Reactive UI updates** basadas en cambios de estado

## 📁 Estructura Multi-Módulo del Proyecto

```
EccomerceApp/
├── 📱 app/                          # Módulo principal
│   └── src/main/java/com/luciaaldana/eccomerceapp/
│       ├── navigation/              # Configuración de navegación
│       │   └── AppNavGraph.kt
│       ├── di/                      # Configuración de red
│       │   └── NetworkModule.kt
│       ├── MainActivity.kt          # Punto de entrada
│       └── EccomerceApp.kt          # Application class
│
├── 🎨 feature/                      # Módulos de características (UI + ViewModels)
│   ├── login/
│   │   └── src/main/java/.../feature/login/
│   │       ├── LoginScreen.kt       # UI con Compose
│   │       └── LoginViewModel.kt    # Lógica de presentación
│   ├── register/
│   │   └── src/main/java/.../feature/register/
│   │       ├── RegisterScreen.kt
│   │       └── RegisterViewModel.kt
│   ├── home/
│   │   └── src/main/java/.../feature/home/
│   │       ├── ProductListScreen.kt
│   │       ├── DetailScreen.kt
│   │       └── ProductsViewModel.kt
│   ├── cart/
│   │   └── src/main/java/.../feature/cart/
│   │       ├── CartScreen.kt
│   │       └── CartViewModel.kt
│   └── profile/
│       └── src/main/java/.../feature/profile/
│           ├── ProfileScreen.kt
│           ├── OrderHistoryScreen.kt
│           ├── OrderConfirmationScreen.kt
│           ├── ProfileViewModel.kt
│           └── OrderHistoryViewModel.kt
│
├── 🔗 domain/                       # Módulos de lógica de negocio (Interfaces)
│   ├── auth/
│   │   └── src/main/java/.../domain/auth/
│   │       └── AuthRepository.kt    # Interface
│   ├── product/
│   │   └── src/main/java/.../domain/product/
│   │       └── ProductRepository.kt # Interface
│   └── cart/
│       └── src/main/java/.../domain/cart/
│           ├── CartItemRepository.kt
│           └── OrderHistoryRepository.kt
│
├── 💾 data/                         # Módulos de implementación de datos
│   ├── auth/
│   │   └── src/main/java/.../data/auth/
│   │       ├── AuthRepositoryImpl.kt    # Implementación
│   │       └── di/AuthModule.kt         # DI específico
│   ├── product/
│   │   └── src/main/java/.../data/product/
│   │       ├── ProductRepositoryImpl.kt
│   │       ├── network/ProductApi.kt    # API endpoints
│   │       ├── dto/ProductDto.kt        # DTOs de red
│   │       ├── mapper/ProductMapper.kt  # Mappers DTO → Domain
│   │       └── di/ProductModule.kt
│   └── cart/
│       └── src/main/java/.../data/cart/
│           ├── CartItemRepositoryImpl.kt
│           ├── OrderHistoryRepositoryImpl.kt
│           └── di/CartModule.kt
│
└── ⚙️ core/                         # Módulos compartidos (sin dependencias)
    ├── model/
    │   └── src/main/java/.../core/model/
    │       ├── Product.kt           # Modelos de dominio
    │       ├── CartItem.kt
    │       ├── Order.kt
    │       ├── User.kt
    │       └── utils/               # Extensiones y utilidades
    │           ├── DateExtensions.kt
    │           └── FormatExtensions.kt
    ├── ui/
    │   └── src/main/java/.../core/ui/
    │       ├── components/          # Componentes reutilizables
    │       │   ├── BottomNavBar.kt
    │       │   └── Header.kt
    │       └── theme/               # Theme y estilos
    │           ├── Color.kt
    │           ├── Theme.kt
    │           └── Type.kt
    └── navigation/
        └── src/main/java/.../core/navigation/
            └── [Navegación compartida]
```

### 📋 Dependencias entre Módulos

```
app
├── feature:* (todas las features)
├── core:ui
└── data:* (todas las implementaciones)

feature:login
├── domain:auth
├── core:model
└── core:ui

feature:home
├── domain:product
├── feature:cart (para agregar al carrito)
├── core:model
└── core:ui

data:product
├── domain:product (implementa)
└── core:model

domain:product
└── core:model

core:model
└── [sin dependencias]
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

### 🔧 Comandos de Desarrollo Multi-Módulo

```bash
# Construcción del proyecto completo
./gradlew build

# Construcción de módulos específicos
./gradlew :feature:cart:build
./gradlew :data:product:build
./gradlew :core:model:build

# Instalación en dispositivo
./gradlew installDebug

# Tests por módulo
./gradlew :feature:cart:test                    # Tests de feature específico
./gradlew :data:auth:test                       # Tests de data layer
./gradlew test                                  # Todos los tests

# Cobertura de tests por módulo
./gradlew :feature:cart:koverHtmlReportDebug    # Cobertura de cart feature
./gradlew :app:koverHtmlReportDebug             # Cobertura general

# Limpieza
./gradlew clean                                 # Limpia todo
./gradlew :feature:home:clean                   # Limpia módulo específico

# Dependencias y análisis
./gradlew :app:dependencies                     # Ver dependencias del app
./gradlew projects                              # Listar todos los módulos
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

### 🏗️ Arquitectura
- [🧩 Arquitectura Multi-Módulo](docs/modularization.md) - Guía completa de modularización
- [📖 Tecnologías Utilizadas](docs/tecnologias.md) - Stack tecnológico y justificaciones
- [🔗 Configuración de Hilt](docs/dependencias_hilt.md) - DI distribuida entre módulos

### 🧪 Testing
- [🧪 Guía de Testing](docs/test/testing.md) - Configuración general y comandos
- [📋 CartViewModel Testing](docs/test/cartviewmodel-testing.md) - Guía detallada de testing de ViewModels

### 🌐 API y Desarrollo
- [🚀 Configuración de API](docs/API.md) - Setup de API local y configuración para Android
