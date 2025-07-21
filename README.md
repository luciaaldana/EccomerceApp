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

#### 📱 **Capa de Presentación (UI)**
- **`:app`** - Módulo principal con MainActivity y configuración DI
- **`:feature:*`** - Módulos de características específicas:
  - `:feature:login` - Pantalla de inicio de sesión con validaciones
  - `:feature:register` - Pantalla de registro con upload de imágenes
  - `:feature:home` - Lista de productos, búsqueda y pantalla de detalle
  - `:feature:cart` - Carrito de compras con gestión de cantidades
  - `:feature:profile` - Perfil de usuario, historial de órdenes y configuración
  - `:feature:productlist` - WorkManager para sincronización en background

#### 🎯 **Capa de Dominio (Business Logic)**
- **`:domain:auth`** - Reglas de negocio de autenticación y validaciones
- **`:domain:product`** - Reglas de negocio de productos y catálogo
- **`:domain:cart`** - Reglas de negocio del carrito y gestión de órdenes

#### 💾 **Capa de Datos (Data Layer)**
- **`:data:auth`** - Implementación de autenticación (API + DTOs + validaciones)
- **`:data:product`** - Implementación de productos (API + Room + Mappers + cache)
- **`:data:cart`** - Implementación del carrito (API + DTOs + persistencia local)
- **`:data:database`** - Base de datos Room con DAOs y entities

#### 🔧 **Módulos Centrales (Core)**
- **`:core:model`** - Modelos de datos compartidos y extensiones utilitarias
- **`:core:ui`** - Componentes UI reutilizables, tema y design system
- **`:core:navigation`** - Componentes de navegación compartidos
- **`:core:cloudinary`** - Servicio centralizado para manejo de imágenes

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
├── 📱 app/                              # Módulo principal de la aplicación
│   ├── build.gradle.kts                # Configuración de build del app
│   └── src/main/java/com/luciaaldana/eccomerceapp/
│       ├── navigation/                  # Configuración de navegación principal
│       │   └── AppNavGraph.kt          # Grafo de navegación global
│       ├── di/                         # Módulos de inyección de dependencias
│       │   ├── NetworkModule.kt        # Configuración de red (Retrofit, OkHttp)
│       │   └── CloudinaryModule.kt     # Configuración de Cloudinary
│       ├── MainActivity.kt             # Actividad principal
│       ├── MainViewModel.kt            # ViewModel principal
│       └── EccomerceApp.kt             # Application class con Hilt
│
├── 🎨 feature/                         # Módulos de características (UI + ViewModels)
│   ├── login/
│   │   ├── build.gradle.kts
│   │   └── src/main/java/.../feature/login/
│   │       ├── LoginScreen.kt          # UI con Compose y validaciones
│   │       └── LoginViewModel.kt       # Lógica de presentación y estados
│   ├── register/
│   │   ├── build.gradle.kts
│   │   └── src/main/java/.../feature/register/
│   │       ├── RegisterScreen.kt       # UI con upload de imágenes
│   │       └── RegisterViewModel.kt    # Gestión de registro y Cloudinary
│   ├── home/
│   │   ├── build.gradle.kts
│   │   └── src/main/java/.../feature/home/
│   │       ├── ProductListScreen.kt    # Lista con búsqueda y filtros
│   │       ├── DetailScreen.kt         # Detalle de producto
│   │       └── ProductsViewModel.kt    # Gestión de estado de productos
│   ├── cart/
│   │   ├── build.gradle.kts
│   │   └── src/main/java/.../feature/cart/
│   │       ├── CartScreen.kt           # UI del carrito con cantidades
│   │       └── CartViewModel.kt        # Lógica de carrito y totales
│   ├── profile/
│   │   ├── build.gradle.kts
│   │   └── src/main/java/.../feature/profile/
│   │       ├── ProfileScreen.kt        # Perfil con edición de datos
│   │       ├── OrderHistoryScreen.kt   # Historial de pedidos
│   │       ├── OrderConfirmationScreen.kt # Confirmación de compra
│   │       ├── ProfileViewModel.kt     # Gestión de perfil y temas
│   │       └── OrderHistoryViewModel.kt # Gestión de historial
│   └── productlist/
│       ├── build.gradle.kts
│       └── src/main/java/.../feature/productlist/
│           └── worker/                 # Sincronización en background
│               ├── ProductSyncWorker.kt
│               └── ProductSyncScheduler.kt
│
├── 🎯 domain/                          # Módulos de lógica de negocio (Interfaces)
│   ├── auth/
│   │   ├── build.gradle.kts
│   │   └── src/main/java/.../domain/auth/
│   │       └── AuthRepository.kt       # Interface de autenticación
│   ├── product/
│   │   ├── build.gradle.kts
│   │   └── src/main/java/.../domain/product/
│   │       └── ProductRepository.kt    # Interface de productos
│   └── cart/
│       ├── build.gradle.kts
│       └── src/main/java/.../domain/cart/
│           ├── CartItemRepository.kt   # Interface de carrito
│           └── OrderHistoryRepository.kt # Interface de órdenes
│
├── 💾 data/                            # Módulos de implementación de datos
│   ├── auth/
│   │   ├── build.gradle.kts
│   │   └── src/main/java/.../data/auth/
│   │       ├── AuthRepositoryImpl.kt   # Implementación de autenticación
│   │       ├── network/UserApi.kt      # API endpoints de usuarios
│   │       ├── dto/                    # DTOs de red
│   │       │   ├── LoginDto.kt
│   │       │   ├── LoginResponse.kt
│   │       │   ├── UserRegistrationDto.kt
│   │       │   └── UpdateUserDto.kt
│   │       ├── utils/PasswordEncoder.kt # Utilidades de encriptación
│   │       └── di/AuthModule.kt        # DI específico de auth
│   ├── product/
│   │   ├── build.gradle.kts
│   │   └── src/main/java/.../data/product/
│   │       ├── ProductRepositoryImpl.kt # Implementación con cache
│   │       ├── network/ProductApi.kt   # API endpoints de productos
│   │       ├── dto/ProductDto.kt       # DTOs de red
│   │       ├── mapper/                 # Mappers DTO → Domain
│   │       │   ├── ProductMapper.kt
│   │       │   └── ProductEntityMapper.kt
│   │       └── di/ProductModule.kt     # DI específico de productos
│   ├── cart/
│   │   ├── build.gradle.kts
│   │   └── src/main/java/.../data/cart/
│   │       ├── CartItemRepositoryImpl.kt # Implementación de carrito
│   │       ├── OrderHistoryRepositoryImpl.kt # Implementación de órdenes
│   │       ├── network/OrderApi.kt     # API endpoints de órdenes
│   │       ├── dto/OrderDto.kt         # DTOs de órdenes
│   │       ├── mapper/OrderMapper.kt   # Mappers de órdenes
│   │       └── di/CartModule.kt        # DI específico de carrito
│   └── database/
│       ├── build.gradle.kts
│       └── src/main/java/.../data/database/
│           ├── AppDatabase.kt          # Configuración de Room
│           ├── dao/                    # Data Access Objects
│           │   ├── ProductDao.kt
│           │   └── UserDao.kt
│           ├── entity/                 # Entidades de base de datos
│           │   ├── ProductEntity.kt
│           │   └── UserEntity.kt
│           └── di/DatabaseModule.kt    # DI de base de datos
│
└── ⚙️ core/                            # Módulos compartidos (sin dependencias)
    ├── model/
    │   ├── build.gradle.kts
    │   └── src/main/java/.../core/model/
    │       ├── Product.kt              # Modelos de dominio
    │       ├── CartItem.kt
    │       ├── Order.kt
    │       ├── User.kt
    │       └── utils/                  # Extensiones y utilidades
    │           ├── DateExtensions.kt
    │           └── FormatExtensions.kt
    ├── ui/
    │   ├── build.gradle.kts
    │   └── src/main/java/.../core/ui/
    │       ├── components/             # Componentes reutilizables
    │       │   ├── BottomNavBar.kt
    │       │   ├── Header.kt
    │       │   ├── ProductCard.kt
    │       │   ├── CustomButton.kt
    │       │   ├── SearchBar.kt
    │       │   └── [20+ componentes más...]
    │       └── theme/                  # Sistema de temas
    │           ├── Color.kt
    │           ├── Theme.kt
    │           ├── Type.kt
    │           ├── ThemeProvider.kt
    │           └── ThemeRepository.kt
    ├── navigation/
    │   ├── build.gradle.kts
    │   └── src/main/java/.../core/navigation/
    │       └── PlaceholderNavigation.kt # Navegación compartida
    └── cloudinary/
        ├── build.gradle.kts
        └── src/main/java/.../core/cloudinary/
            ├── CloudinaryConfig.kt    # Configuración de Cloudinary
            └── CloudinaryService.kt   # Servicio de upload de imágenes
```

### 📋 Dependencias entre Módulos

```plaintext
:app
├── depends on → :feature:login
├── depends on → :feature:register  
├── depends on → :feature:home
├── depends on → :feature:cart
├── depends on → :feature:profile
├── depends on → :core:ui
├── depends on → :core:navigation
└── depends on → :core:cloudinary

:feature:login
├── depends on → :domain:auth
├── depends on → :data:auth
├── depends on → :core:model
└── depends on → :core:ui

:feature:register
├── depends on → :domain:auth
├── depends on → :data:auth
├── depends on → :core:model
├── depends on → :core:ui
└── depends on → :core:cloudinary

:feature:home
├── depends on → :domain:product
├── depends on → :data:product
├── depends on → :domain:cart (para agregar al carrito)
├── depends on → :data:cart
├── depends on → :core:model
└── depends on → :core:ui

:feature:cart
├── depends on → :domain:cart
├── depends on → :data:cart
├── depends on → :core:model
└── depends on → :core:ui

:feature:profile
├── depends on → :domain:auth
├── depends on → :domain:cart (historial de órdenes)
├── depends on → :data:auth
├── depends on → :data:cart
├── depends on → :core:model
├── depends on → :core:ui
└── depends on → :core:cloudinary

:feature:productlist
├── depends on → :domain:product
├── depends on → :data:product
└── depends on → :core:model

:data:auth
├── depends on → :domain:auth (implementa)
├── depends on → :core:model
└── depends on → :core:cloudinary

:data:product
├── depends on → :domain:product (implementa)
├── depends on → :data:database
└── depends on → :core:model

:data:cart
├── depends on → :domain:cart (implementa)
└── depends on → :core:model

:data:database
└── depends on → :core:model

:domain:auth
└── depends on → :core:model

:domain:product
└── depends on → :core:model

:domain:cart
└── depends on → :core:model

:core:* (todos los módulos core)
└── Sin dependencias entre sí (independientes)
```

#### 🏗️ **Reglas de Arquitectura Implementadas**

**✅ Clean Architecture Flow:**
- **Feature** → **Domain** ← **Data** (inversión de dependencias)
- **Domain** solo define interfaces, **Data** las implementa
- **Feature** no conoce implementaciones específicas de **Data**

**✅ Modularización Correcta:**
- **Core modules** son completamente independientes
- **App module** solo orquesta features y configuración global  
- **Domain** no depende de **Data** (principio de inversión)
- **No dependencias circulares** entre módulos

**✅ Separación de Responsabilidades:**
- **UI/ViewModels** (:feature) → Presentación
- **Interfaces** (:domain) → Reglas de negocio
- **Implementaciones** (:data) → Acceso a datos
- **Modelos compartidos** (:core) → Utilities y modelos

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

CLOUDINARY_CLOUD_NAME=YOUR_CLOUDINARY_CLOUD_NAME
CLOUDINARY_API_KEY=YOUR_CLOUDINARY_API_KEY
CLOUDINARY_API_SECRET=YOUR_CLOUDINARY_API_SECRET
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
- **Cloudinary** – Carga y almacenamiento de imágenes de perfil en la nube

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
- ✅ Lista con textos centralizados en **strings.xml** por módulos

## 📚 Documentación Adicional

### 🏗️ Arquitectura
- [🧩 Arquitectura Multi-Módulo](docs/modularization.md) – Guía completa de modularización
- [📖 Tecnologías Utilizadas](docs/tecnologias.md) – Stack tecnológico y justificaciones
- [🔗 Configuración de Hilt](docs/dependencias_hilt.md) – DI distribuida entre módulos
- [🎨 Diseño y Temas](docs/theme.md) – Personalización visual con Material 3

### 🧪 Testing
- [🧪 Guía de Testing](docs/test/testing.md) – Configuración general y comandos
- [📋 ProductList Testing](docs/test/productlist-testing.md) – Guía detallada de testing de ViewModels

### 🌐 API y Desarrollo
- [🚀 Configuración de API](docs/API.md) – Setup de API local y configuración para Android
- [🔐 Autenticación](docs/autenticacion.md) – Manejo de login, registro y sesión
- [☁️ Cloudinary](docs/cloudinary.md) – Subida y carga de imágenes desde galería o cámara
- [📦 WorkManager](docs/workmanager.md) – Tareas en segundo plano con CoroutineWorker  
