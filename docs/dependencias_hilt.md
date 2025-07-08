# 🛠️ Configuración de Hilt en Arquitectura Multi-Módulo

Esta documentación explica cómo está configurado Hilt en el proyecto multi-módulo, siguiendo las mejores prácticas de Android para inyección de dependencias distribuida.

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

## ✅ 5. Configuración por Módulo

### 📱 Módulo `:app` (Application)

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt.android) // +
    kotlin("kapt") // +
}

dependencies {
    // Todas las implementaciones de data para DI
    implementation(project(":data:auth"))
    implementation(project(":data:cart"))
    implementation(project(":data:product"))
    
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}
```

### 🎨 Módulos `:feature:*` (UI + ViewModels)

```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt.android) // +
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(project(":domain:auth")) // Solo interfaces
    implementation(project(":core:model"))
    implementation(project(":core:ui"))
    
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
}
```

### 💾 Módulos `:data:*` (Implementaciones + DI)

```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt.android) // +
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(project(":domain:auth")) // Implementa interfaces
    implementation(project(":core:model"))
    
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}
```

### 🔗 Módulos `:domain:*` (Interfaces)

```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

dependencies {
    implementation(project(":core:model"))
    // NO necesita Hilt - solo interfaces
}
```

### ⚙️ Módulos `:core:*` (Compartidos)

```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose) // Solo :core:ui
}

dependencies {
    // Ninguna dependencia externa de otros módulos
    // NO necesita Hilt
}
```

## ✅ 6. Application Class

**En `:app/EccomerceApp.kt`:**

```kotlin
@HiltAndroidApp
class EccomerceApp : Application()
```

**En `:app/AndroidManifest.xml`:**

```xml
<application
    android:name=".EccomerceApp"
    ...>
```

---

## ✅ 7. Módulos de DI Distribuidos

### 🔐 AuthModule (en `:data:auth/di/`)

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl
}
```

### 🛒 CartModule (en `:data:cart/di/`)

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object CartModule {

    @Provides
    @Singleton
    fun provideCartItemRepository(impl: CartItemRepositoryImpl): CartItemRepository = impl

    @Provides
    @Singleton
    fun provideOrderHistoryRepository(impl: OrderHistoryRepositoryImpl): OrderHistoryRepository = impl
}
```

### 📦 ProductModule (en `:data:product/di/`)

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object ProductModule {

    @Provides
    @Singleton
    fun provideProductApi(retrofit: Retrofit): ProductApi = retrofit.create(ProductApi::class.java)

    @Provides
    @Singleton
    fun provideProductRepository(productApi: ProductApi): ProductRepository = ProductRepositoryImpl(productApi)
}
```

### 🌐 NetworkModule (en `:app/di/`)

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides 
    @Singleton
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .build()

    @Provides 
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Provides 
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.RENDER_BASE_URL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}

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

## ✅ Resumen de la Configuración Multi-Módulo

### 📋 Dependencias por Tipo de Módulo

| Módulo | Hilt Plugin | Hilt Dependencies | Propósito |
|--------|-------------|-------------------|-----------|
| `:app` | ✅ Sí | `hilt-android` + `hilt-compiler` | Application class + DI principal |
| `:feature:*` | ✅ Sí | `hilt-android` + `hilt-navigation-compose` | ViewModels con @HiltViewModel |
| `:data:*` | ✅ Sí | `hilt-android` + `hilt-compiler` | Módulos DI + implementaciones |
| `:domain:*` | ❌ No | ❌ Ninguna | Solo interfaces |
| `:core:*` | ❌ No | ❌ Ninguna | Modelos y utilidades compartidas |

### 🔗 Reglas de Dependencias

```
feature:login
├── domain:auth (interfaces)
├── core:model
└── core:ui

data:auth  
├── domain:auth (implementa)
└── core:model

app
├── feature:* (todas)
├── data:* (todas - para DI)
└── core:ui
```

### 🎯 Beneficios

- **DI distribuida**: Cada módulo data maneja su propia inyección
- **Compilación paralela**: Módulos independientes se compilan en paralelo  
- **Testing aislado**: Cada módulo se puede testear con mocks específicos
- **Separación clara**: Interfaces en domain, implementaciones en data