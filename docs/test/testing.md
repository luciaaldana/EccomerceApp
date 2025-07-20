# 📑 Testing Guide for EcommerceApp

Esta guía resume **qué librerías se usan, cómo ejecutar las pruebas unitarias** y el **reporte de cobertura** en el proyecto *EcommerceApp*.

## 🛠️ Stack de Testing

### Dependencias principales

| Propósito                    | Librería (alias en `libs.versions.toml`)                    |
| ---------------------------- | ----------------------------------------------------------- |
| Runner de tests              | **JUnit 4** (`junit`)                                       |
| Coroutines controladas       | **kotlinx‑coroutines‑test** (`coroutines-test`)             |
| Asserts sobre Flow/StateFlow | **Turbine** (`turbine`)                                     |
| Framework de mocks           | **MockK** (`mockk`) y **Mockito Kotlin** (`mockito-kotlin`) |
| DI en tests (Hilt)           | **hilt-android-testing** + `kaptTest(libs.hilt.compiler)`   |
| Reporte de cobertura         | **Kover** (plugin `org.jetbrains.kotlinx.kover`)            |

> 👉 Todas las versiones están centralizadas en `gradle/libs.versions.toml`.

### Versiones actuales

```toml
[versions]
junit = "4.13.2"
coroutinesTest = "1.8.0"
turbine = "1.0.0"
mockk = "1.13.8"
mockitoKotlin = "5.2.1"
```

## 📁 Estructura de Tests

```
app/
└── src/
    └── test/
        └── java/
            └── com/luciaaldana/eccomerceapp/
                ├── testutils/
                │   └── MainDispatcherRule.kt
                └── viewmodel/
                    ├── ProductsViewModelTest.kt
                    └── CartViewModelTest.kt
```

### Utilidades de Testing

- **MainDispatcherRule.kt**: Sustituye `Dispatchers.Main` por un `TestDispatcher` para controlar corrutinas en pruebas
- **Test Data Builders**: Objetos de prueba reutilizables (products, cart items, etc.)

## 🚀 Comandos de Ejecución

### Tests Unitarios

| Comando                            | Descripción                                                               |
| ---------------------------------- | ------------------------------------------------------------------------- |
| `./gradlew :app:testDebugUnitTest` | Ejecuta tests unitarios de la variante *debug* del módulo `app`          |
| `./gradlew test`                   | Ejecuta todos los tests de todos los módulos                             |
| `./gradlew :app:test --tests "*CartViewModelTest*"` | Ejecuta tests específicos de CartViewModel      |

### Desde Android Studio

- **Ejecutar todos**: *Run ▸ All Tests*
- **Ejecutar con cobertura**: Clic derecho ▸ *Run with Coverage*
- **Ejecutar test específico**: Clic en el ícono ▶️ junto al test

## 📊 Reporte de Cobertura

### Generar reporte HTML

```bash
# 1. Ejecutar tests (si no se hizo antes)
./gradlew :app:testDebugUnitTest

# 2. Generar reporte HTML
./gradlew :app:koverHtmlReportDebug

# 3. Abrir reporte
# macOS:
open app/build/reports/kover/debug/html/index.html

# Windows/Linux:
# Navegar hasta app/build/reports/kover/debug/html/index.html
```

### Configuración de cobertura

El proyecto está configurado para generar reportes detallados con Kover:
- **Formato**: HTML interactivo
- **Inclusiones**: Todo el código de `src/main`
- **Exclusiones**: Generated code, data classes simples

## ✅ Tests Implementados

### ViewModels Testeados

- **ProductsViewModel**: Tests completos de carga, búsqueda, filtrado y manejo de errores (22 tests)
- **ProductRepositoryImpl**: Tests de operaciones CRUD, sincronización API y Flow behavior (17 tests)

#### ProductList Testing - Casos de prueba implementados

```kotlin
// ProductsViewModel Tests
@Test fun `initial state should have empty products and screen loading true`()
@Test fun `should load products from repository on init`()
@Test fun `search query should filter products by name`()
@Test fun `category filter should filter products correctly`()
@Test fun `filteredProducts should emit correct sequence when search changes`() // Turbine
@Test fun `error StateFlow should emit correctly on repository failure`() // Turbine

// ProductRepositoryImpl Tests  
@Test fun `getProducts should return flow of products from dao`()
@Test fun `syncProductsFromApi should fetch from api and save to dao`()
@Test fun `getProducts Flow should emit updated products when dao changes`() // Turbine
```

## 🎯 Patrones de Testing Aplicados

### 1. Given-When-Then
```kotlin
@Test
fun `search query should filter products by name`() = runTest {
    // GIVEN
    val testProducts = listOf(testProduct1, testProduct2, testProduct3)
    whenever(productRepository.getProducts()).thenReturn(flowOf(testProducts))
    viewModel = ProductsViewModel(productRepository, authRepository)
    
    // WHEN
    viewModel.onSearchQueryChanged("burger")
    advanceUntilIdle()
    
    // THEN
    val filteredProducts = viewModel.filteredProducts.first()
    assertEquals(1, filteredProducts.size)
    assertEquals(testProduct1, filteredProducts[0])
}
```

### 2. Fresh ViewModel Pattern
```kotlin
// ✅ Crear ViewModel fresco por test
@Test
fun `specific test`() = runTest {
    whenever(productRepository.getProducts()).thenReturn(flowOf(...))
    val testViewModel = ProductsViewModel(productRepository, authRepository) // Estado limpio
    // ...
}
```

### 3. Mock Sequencing con Mockito
```kotlin
// Simular estados secuenciales para repositorio
whenever(productRepository.refreshProducts())
    .thenReturn(listOf(product1))      // Primera llamada
    .thenReturn(listOf(product1, product2))  // Segunda llamada
```

### 4. StateFlow Testing con Turbine
```kotlin
// Para verificar emisiones reactivas
viewModel.filteredProducts.test {
    assertEquals(allProducts, awaitItem())        // Emisión inicial
    
    viewModel.onSearchQueryChanged("burger")
    
    assertEquals(filteredProducts, awaitItem())   // Nueva emisión
}
```

## 🚦 Buenas Prácticas

### ✅ Hacer

- **Nombres descriptivos**: `search query should filter products by name`
- **Un test = un comportamiento**: Verificar una sola funcionalidad por test
- **Usar `advanceUntilIdle()`**: Después de operaciones asíncronas con corrutinas
- **Aislar dependencias**: Usar mocks para repositorios y servicios externos
- **Test data builders**: Reutilizar objetos de prueba consistentes
- **Usar Turbine**: Para testing reactivo de StateFlow y Flow

### ❌ Evitar

- **Tests dependientes**: Cada test debe poder ejecutarse independientemente
- **IO real**: No usar bases de datos reales, APIs externas, etc.
- **Threading real**: Usar `TestDispatcher` en lugar de `Dispatchers.Main`
- **Datos aleatorios**: Usar datos determinísticos para tests reproducibles

## 🔧 Configuración de Herramientas

### MainDispatcherRule

```kotlin
@get:Rule
val dispatcherRule = MainDispatcherRule()
```

Esta regla sustituye `Dispatchers.Main` con un `TestDispatcher` controlable.

### Mockito para Mocking

El proyecto usa **Mockito** con extensiones de Kotlin:

```kotlin
// Mockito con kotlin extensions
whenever(productRepository.getProducts()).thenReturn(flowOf(products))
verify(productRepository).refreshProducts()

// Soporte para suspend functions
runBlocking {
    whenever(productRepository.hasLocalProducts()).thenReturn(false)
}
```

## 🎯 Cobertura y Métricas

### Objetivos de cobertura

- **Mínimo objetivo**: 80% de cobertura en ViewModels y lógica de negocio
- **Componentes críticos**: 90%+ cobertura (carrito, cálculos, autenticación)
- **Data classes simples**: Pueden tener menor cobertura

### Verificación automática

```kotlin
// En build.gradle.kts (futuro)
kover {
    verify {
        rule {
            bound {
                minValue = 80
                valueType = kotlinx.kover.api.VerificationValueType.COVERED_LINES_PERCENTAGE
            }
        }
    }
}
```

## 📚 Documentación Adicional

- **[ProductList Testing Detallado](productlist-testing.md)**: Guía completa de testing de ProductList con TDD, Mockito y Turbine
- **[Hilt Dependencies](../dependencias_hilt.md)**: Configuración de inyección de dependencias
