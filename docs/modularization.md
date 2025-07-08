# 🧩 Arquitectura Multi-Módulo

Este documento explica la estrategia de modularización implementada en EcommerceApp, basada en los **patrones oficiales de Android** y las **mejores prácticas de Clean Architecture**.

## 🎯 Objetivos de la Modularización

### ⚡ Mejora en Tiempos de Build
- **Compilación paralela**: Los módulos se compilan simultáneamente
- **Builds incrementales**: Solo se recompilan los módulos modificados
- **Cache de Gradle**: Mejor aprovechamiento del cache entre builds

### 🧪 Testing Mejorado
- **Testing aislado**: Cada módulo se testea independientemente
- **Mocks específicos**: Interfaces claras facilitan la creación de test doubles
- **Cobertura granular**: Análisis de cobertura por módulo

### 📈 Escalabilidad
- **Nuevas features**: Fácil agregar características sin afectar existentes
- **Trabajo en equipo**: Diferentes desarrolladores pueden trabajar en módulos distintos
- **Reutilización**: Módulos core compartidos entre features

## 🏗️ Arquitectura Implementada

### 📋 Patrón Clean Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Feature       │────│     Domain      │────│      Data       │────│      Core       │
│   UI + VM       │    │   Interfaces    │    │ Implementations │    │    Shared       │
│  (Compose +     │    │ (Use Cases +    │    │ (Repositories + │    │   (Models +     │
│   ViewModels)   │    │  Repositories)  │    │   APIs + DTOs)  │    │   Utilities)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘    └─────────────────┘
```

### 🔗 Reglas de Dependencias

**Flujo unidireccional (solo "hacia abajo"):**

1. `Feature` → `Domain` → `Core`
2. `Data` implementa `Domain`, pero NO depende de `Feature`
3. `Core` no depende de nadie (módulos leaf)
4. `App` orquesta todo pero no contiene lógica de negocio

## 📦 Estructura de Módulos

### 🎨 Feature Modules (`feature:*`)

**Propósito**: UI + Lógica de Presentación

```
feature:login/
├── LoginScreen.kt          # UI con Jetpack Compose
├── LoginViewModel.kt       # Lógica de presentación
└── build.gradle.kts

Dependencies:
├── domain:auth            # Interfaces de negocio
├── core:model            # Modelos compartidos
└── core:ui               # Componentes UI reutilizables
```

**Características:**
- **ViewModels** con `@HiltViewModel`
- **Screens** con Jetpack Compose
- **Navigation** entre pantallas
- **State management** con StateFlow

### 🔗 Domain Modules (`domain:*`)

**Propósito**: Interfaces de Lógica de Negocio

```
domain:auth/
├── AuthRepository.kt       # Interface del repositorio
└── build.gradle.kts

Dependencies:
└── core:model             # Solo modelos, sin implementaciones
```

**Características:**
- **Solo interfaces** - Sin implementaciones
- **Use cases** (si la lógica de negocio lo requiere)
- **Repository interfaces** que definen contratos
- **Sin dependencias de Android** (pure Kotlin)

### 💾 Data Modules (`data:*`)

**Propósito**: Implementaciones + Fuentes de Datos

```
data:product/
├── ProductRepositoryImpl.kt    # Implementación del repositorio
├── network/
│   └── ProductApi.kt           # API de Retrofit
├── dto/
│   └── ProductDto.kt           # DTOs de red
├── mapper/
│   └── ProductMapper.kt        # Transformación DTO → Domain
├── di/
│   └── ProductModule.kt        # Módulo de Hilt
└── build.gradle.kts

Dependencies:
├── domain:product         # Implementa las interfaces
└── core:model            # Modelos de dominio
```

**Características:**
- **Repository implementations** con `@Inject`
- **API clients** (Retrofit + Moshi)
- **DTOs y Mappers** para transformación de datos
- **Módulos de Hilt** para DI específica
- **Data sources** (local, remote, cache)

### ⚙️ Core Modules (`core:*`)

**Propósito**: Código Compartido (Leaf Modules)

```
core:model/
├── Product.kt              # Modelos de dominio
├── CartItem.kt
├── Order.kt
├── MockUser.kt
├── utils/
│   ├── DateExtensions.kt   # Extensiones de utilidad
│   └── FormatExtensions.kt
└── build.gradle.kts

core:ui/
├── components/
│   ├── BottomNavBar.kt     # Componentes reutilizables
│   └── Header.kt
├── theme/
│   ├── Color.kt            # Sistema de colores
│   ├── Theme.kt            # Tema de la app
│   └── Type.kt             # Tipografía
└── build.gradle.kts

Dependencies: NINGUNA (módulos leaf)
```

**Características:**
- **Sin dependencias** de otros módulos del proyecto
- **Modelos de dominio** compartidos
- **Utilidades** y extensiones comunes
- **Componentes UI** reutilizables
- **Theme system** centralizado

### 📱 App Module (`app`)

**Propósito**: Orquestación + Configuración

```
app/
├── EccomerceApp.kt         # Application class
├── MainActivity.kt         # Actividad principal
├── navigation/
│   └── AppNavGraph.kt      # Configuración de navegación
├── di/
│   └── NetworkModule.kt    # Configuración de red
└── build.gradle.kts

Dependencies:
├── feature:* (todas)      # Todas las features para navegación
├── data:* (todas)         # Todas las implementaciones para DI
└── core:ui               # Theme y componentes base
```

**Características:**
- **Application class** con `@HiltAndroidApp`
- **MainActivity** con configuración principal
- **Navegación global** entre features
- **Network configuration** (Retrofit, OkHttp)
- **NO lógica de negocio** - solo configuración

## 🔄 Flujo de Datos

### 📱 User Interaction → UI Update

```
1. User taps button in LoginScreen (feature:login)
2. LoginViewModel calls AuthRepository.login() (domain:auth interface)
3. AuthRepositoryImpl.login() handles authentication (data:auth)
4. Result flows back through StateFlow to LoginScreen
5. UI updates reactively with new state
```

### 🌐 Network Request → Domain Model

```
1. ProductApi.getProducts() returns ProductDto[] (data:product)
2. ProductMapper.toDomain() converts DTO → Product (data:product)
3. ProductRepositoryImpl returns List<Product> (data:product)
4. ProductsViewModel receives domain models (feature:home)
5. UI renders products with domain data
```

## 🛠️ Herramientas de Build

### 📊 Análisis de Dependencias

```bash
# Ver todas las dependencias del proyecto
./gradlew :app:dependencies

# Analizar dependencias de un módulo específico
./gradlew :feature:cart:dependencies

# Verificar dependencias circulares
./gradlew checkDependencies
```

### ⚡ Optimizaciones de Build

```bash
# Compilación paralela (habilitada por defecto)
org.gradle.parallel=true

# Build incremental
./gradlew build --continue

# Cache de configuración
org.gradle.configuration-cache=true
```

### 📈 Métricas de Módulos

```bash
# Listar todos los módulos
./gradlew projects

# Tamaño de cada módulo
./gradlew :feature:cart:assembleDebug --scan

# Tiempo de build por módulo
./gradlew build --profile
```

## ✅ Mejores Prácticas Implementadas

### 🎯 Separation of Concerns
- **Feature modules**: Solo UI y presentación
- **Domain modules**: Solo lógica de negocio (interfaces)
- **Data modules**: Solo implementaciones y acceso a datos
- **Core modules**: Solo utilidades compartidas

### 🔒 Dependency Inversion
- **Features** dependen de **interfaces** (domain), no implementaciones
- **Data** implementa **interfaces** sin conocer quien las usa
- **Testeable**: Fácil mockear dependencias por interfaces

### 🚀 Performance
- **Compilación paralela**: Módulos se compilan simultáneamente
- **Builds incrementales**: Solo se recompilan cambios
- **Lazy loading**: Módulos se cargan según necesidad

### 🧪 Testability
- **Unit testing**: Cada módulo se testea independientemente
- **Mocking**: Interfaces facilitan creación de test doubles
- **Coverage**: Análisis granular por módulo

## 🔍 Debugging Multi-Módulo

### 📋 Comandos Útiles

```bash
# Verificar estructura de módulos
./gradlew projects

# Ver dependencias específicas
./gradlew :feature:login:dependencies --configuration implementation

# Analizar clases duplicadas
./gradlew :app:checkDuplicateClasses

# Profile de build completo
./gradlew build --profile --scan
```

### 🚨 Problemas Comunes

**❌ Dependencia circular:**
```
feature:home → feature:cart → feature:home
```
**✅ Solución:** Extraer funcionalidad común a `core:` o usar navegación

**❌ Acceso directo entre features:**
```
feature:login → feature:cart (directo)
```
**✅ Solución:** Navegar a través de `app:navigation`

**❌ Lógica de negocio en feature:**
```
// En LoginScreen.kt
val isValidEmail = email.contains("@") && email.length > 5
```
**✅ Solución:** Mover validación a `domain:` o `core:`

## 📚 Referencias

- [Android Modularization Guide](https://developer.android.com/topic/modularization)
- [Architecture Components](https://developer.android.com/topic/architecture)
- [Hilt in Multi-Module Apps](https://developer.android.com/training/dependency-injection/hilt-multi-module)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)