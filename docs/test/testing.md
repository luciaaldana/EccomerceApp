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

- **ProductsViewModel**: Tests de carga de productos
- **CartViewModel**: Tests completos de operaciones CRUD

#### CartViewModel - Casos de prueba

```kotlin
@Test fun `init should load cart items from repository`()
@Test fun `cartItems should be empty initially when repository is empty`()
@Test fun `add should call repository addProduct and reload cart`()
@Test fun `remove should call repository removeProduct and reload cart`()
@Test fun `updateQuantity should call repository updateQuantity and reload cart`()
@Test fun `clearCart should call repository clearCart and reload cart`()
@Test fun `totalAmount should be calculated correctly with multiple items`()
@Test fun `totalAmount should update when cart items change`()
@Test fun `multiple operations should maintain state consistency`()
```

## 🎯 Patrones de Testing Aplicados

### 1. Given-When-Then
```kotlin
@Test
fun `add should call repository addProduct and reload cart`() = runTest {
    // GIVEN
    every { cartItemRepository.getCartItems() } returnsMany listOf(emptyList(), listOf(item))
    val testViewModel = CartViewModel(cartItemRepository)
    
    // WHEN
    testViewModel.add(product)
    advanceUntilIdle()
    
    // THEN
    verify { cartItemRepository.addProduct(product) }
    assertEquals(listOf(item), testViewModel.cartItems.value)
}
```

### 2. Fresh ViewModel Pattern
```kotlin
// ✅ Crear ViewModel fresco por test
@Test
fun `specific test`() = runTest {
    every { repository.getCartItems() } returnsMany listOf(...)
    val testViewModel = CartViewModel(repository) // Estado limpio
    // ...
}
```

### 3. Mock Sequencing
```kotlin
// Simular estados secuenciales
every { repository.getCartItems() } returnsMany listOf(
    emptyList(),              // Primera llamada (init)
    listOf(item1),           // Segunda llamada (después de add)
    listOf(item1, item2)     // Tercera llamada (después de otro add)
)
```

### 4. StateFlow Testing con Turbine
```kotlin
// Para verificar emisiones reactivas
viewModel.totalAmount.test {
    assertEquals(0.0, awaitItem())        // Emisión inicial
    
    viewModel.add(product)
    
    assertEquals(31.98, awaitItem())      // Nueva emisión
}
```

## 🚦 Buenas Prácticas

### ✅ Hacer

- **Nombres descriptivos**: `add should call repository addProduct and reload cart`
- **Un test = un comportamiento**: Verificar una sola funcionalidad por test
- **Usar `advanceUntilIdle()`**: Después de operaciones asíncronas con corrutinas
- **Aislar dependencias**: Usar mocks para repositorios y servicios externos
- **Test data builders**: Reutilizar objetos de prueba consistentes

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

### MockK vs Mockito

El proyecto usa **MockK** como framework principal de mocking:

```kotlin
// MockK (recomendado para Kotlin)
every { repository.getCartItems() } returns listOf(item1, item2)
verify { repository.addProduct(product) }

// Sintaxis más natural para Kotlin
// Mejor soporte para corrutinas
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

- **[CartViewModel Testing Detallado](cartviewmodel-testing.md)**: Guía completa de testing de ViewModels
- **[Hilt Dependencies](../dependencias_hilt.md)**: Configuración de inyección de dependencias
