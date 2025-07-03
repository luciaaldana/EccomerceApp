# 📑 Testing Guide for EcommerceApp

Esta guía resume **qué librerías se usa, y cómo ejecutar las pruebas unitarias** y el **reporte de cobertura** en el proyecto *EcommerceApp*.

## Dependencias de testing

| Propósito                    | Librería (alias en `libs.versions.toml`)                    |
| ---------------------------- | ----------------------------------------------------------- |
| Runner de tests              | **JUnit 4** (`junit`)                                       |
| Coroutines controladas       | **kotlinx‑coroutines‑test** (`coroutines-test`)             |
| Asserts sobre Flow/StateFlow | **Turbine** (`turbine`)                                     |
| Framework de mocks           | **Mockito Kotlin** (`mockito-kotlin`) o **MockK** (`mockk`) |
| DI en tests (si hay Hilt)    | **hilt-android-testing** + `kaptTest(libs.hilt.compiler)`   |
| Reporte de cobertura         | **Kover** (plugin `org.jetbrains.kotlinx.kover`)            |

> 👉 Todos los números de versión viven en `` para mantenerlas centralizadas.


## Estructura de carpetas de tests

```
app/
└── src/
    └── test/
        └── kotlin/
            └── com/luciaaldana/eccomerceapp/
                ├── testutils/
                │   └── MainDispatcherRule.kt
                └── viewmodel/
                    └── ProductsViewModelTest.kt
```

- **MainDispatcherRule.kt** sustituye `Dispatchers.Main` por un `TestDispatcher` para controlar corutinas en pruebas.
- Cada feature tiene su carpeta de tests propia.

## Cómo ejecutar los tests

| Comando                            | Qué hace                                                                    |
| ---------------------------------- | --------------------------------------------------------------------------- |
| `./gradlew :app:testDebugUnitTest` | Corre **solo** los tests unitarios de la variante *debug* del módulo `app`. |
| `./gradlew test`                   | Corre todas las unidades de todos los módulos.                              |
| Desde Android Studio               | *Run ▸ Tests in ‘…’* o clic derecho ▸ *Run with Coverage*.                  |

## Generar reporte de cobertura

> Kover crea tareas por variante. Usamos *debug* para reflejar lo que ejecutan los tests.

1. **Ejecuta primero los tests** (si no lo hiciste):
   ```bash
   ./gradlew :app:testDebugUnitTest
   ```
2. **Genera el HTML**:
   ```bash
   ./gradlew :app:koverHtmlReportDebug
   ```
3. **Abre el reporte**
   - macOS: `open app/build/reports/kover/debug/html/index.html`
   - Windows/Linux: navega hasta esa ruta y abre `index.html` con tu navegador.

## Buenas prácticas

- ✨ **Un test = un comportamiento.** Manténlos cortos y descriptivos.
- 🚦 `` después de instanciar un ViewModel que hace trabajo en `init {}`.
- 🔌 **Evita IO real**; reemplaza red/BD por mocks o fakes.
- 🎯 **Cobertura mínima** sugerida: `80 %` líneas en lógica crítica (configurable en `kover { verify { … } }`).
- 🛑 Falla el CI si la cobertura baja: añade una `rule` en Kover.


