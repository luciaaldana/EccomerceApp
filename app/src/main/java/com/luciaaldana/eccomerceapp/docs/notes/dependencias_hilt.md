# 🛠️ Instrucciones para agregar Hilt al proyecto

Estas instrucciones usan `libs.versions.toml` como base para la configuración.

---

## ✅ 1. Agregar las versiones en `[versions]` de `libs.versions.toml`

```toml
hilt = "2.52"
```

---

## ✅ 2. Agregar las dependencias en `[libraries]`

```toml
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
hilt-android-gradle-plugin = { group = "com.google.dagger", name = "hilt-android-gradle-plugin", version.ref = "hilt" }
dagger-android-processor = { group = "com.google.dagger", name = "dagger-android-processor", version.ref = "hilt" }
androidx-hilt-navigation-compose = { module = "androidx.hilt:hilt-navigation-compose", version = "1.1.0" }
```

---

## ✅ 3. Agregar el plugin en `[plugins]`

```toml
dagger-hilt-android = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
```

---

## ✅ 4. Usarlo en el archivo `build.gradle.kts` del proyecto

```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.dagger.hilt.android) apply false // +
}
```

---

## ✅ 5. Usarlo en el archivo `build.gradle.kts` del módulo `:app`

### Bloque `plugins` y `dependencies`

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt.android) // +
    kotlin("kapt") // +
}

dependencies {
    ...
    implementation(libs.hilt.android)
    implementation(libs.hilt.android.gradle.plugin)
    kapt(libs.dagger.android.processor)
    kapt(libs.hilt.compiler)
}
```

## ✅ 5. Crear `EccomerceApp.kt`

```kotlin
@HiltAndroidApp
class EccomerceApp : Application()
```

Y en `AndroidManifest.xml` agregar:

```xml
<application
    android:name=".EccomerceApp"
    ...>
```

---

## ✅ 6. Crear módulo de Hilt (ej: `AuthModule.kt`)

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl()
}
```

---

## ✅ 7. Importaciones necesarias

```kotlin
import javax.inject.Inject
import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
```

---

## ✅ En resumen:

| Dependencia                        | ¿Para qué sirve?                                   | ¿Es obligatoria? |
|-----------------------------------|----------------------------------------------------|------------------|
| `hilt-android`                    | Base de Hilt, inyección de dependencias            | ✅ Sí            |
| `hilt-compiler`                   | Generación de código con anotaciones               | ✅ Sí            |
| `androidx-hilt-navigation-compose`| Integración con Jetpack Compose                    | 🟡 Recomendado   |