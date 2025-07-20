# WorkManager - Sincronización en Segundo Plano

## ¿Qué es WorkManager?

WorkManager es la librería de Android que permite ejecutar tareas en segundo plano de manera confiable, incluso cuando la app no está abierta. Es perfecto para tareas como sincronizar datos, subir archivos o hacer limpieza de cache.

## Implementación en el Proyecto

### 🔄 Flujo de Sincronización de Productos con Caché Local

```
PRIMERA VEZ (sin datos en caché):
1. Usuario abre ProductList/DetailScreen
   ↓
2. ProductsViewModel llama a refreshProducts()
   ↓
3. ProductRepository.refreshProducts() → API call
   ↓
4. Datos se guardan en Room
   ↓
5. UI se actualiza (observa Room via Flow)

SIGUIENTES VECES (con caché):
1. Usuario abre ProductList/DetailScreen
   ↓
2. ProductsViewModel observa ProductRepository.getProducts()
   ↓
3. Room devuelve datos locales instantáneamente
   ↓
4. UI se actualiza inmediatamente (sin llamada a API)

SINCRONIZACIÓN EN SEGUNDO PLANO:
1. WorkManager ejecuta cada 6 horas
   ↓
2. ProductSyncWorker → ProductRepository.syncProductsFromApi()
   ↓
3. API call → actualiza Room
   ↓
4. UI se actualiza automáticamente (observa Room via Flow)
```

### Estructura de Archivos

```
feature/productlist/
└── worker/
    ├── ProductSyncWorker.kt         # El worker que hace la sincronización
    └── ProductSyncScheduler.kt      # Programa cuándo ejecutar el worker

data/database/
├── entity/
│   └── ProductEntity.kt            # Entidad Room para productos
└── dao/
    └── ProductDao.kt                # DAO para operaciones de productos

data/product/
├── ProductRepositoryImpl.kt        # Implementación con caché local
└── mapper/
    └── ProductEntityMapper.kt       # Mappers entre Entity y Domain
```

### Componentes Principales

#### 1. ProductSyncWorker
- **Qué hace**: Ejecuta la sincronización de productos en segundo plano
- **Cuándo**: Cada 6 horas automáticamente
- **Función**: Llama a `ProductRepository.syncProductsFromApi()` → actualiza Room
- **Requiere**: Conexión a internet

#### 2. ProductSyncScheduler
- **Qué hace**: Programa cuándo ejecutar el worker
- **Configuración**: 6 horas de intervalo, requiere red
- **Política**: KEEP (no duplica si ya existe)

#### 3. ProductRepository con Caché
- **getProducts()**: Devuelve Flow de Room (datos locales)
- **refreshProducts()**: Fuerza actualización desde API
- **syncProductsFromApi()**: Llamada API + actualizar Room
- **getProductById()**: Busca producto específico en caché local

#### 4. Room Database
- **ProductEntity**: Entidad con timestamp de sincronización
- **ProductDao**: Operaciones CRUD + observación via Flow
- **AppDatabase**: Incluye tablas User y Product (version 2)

## Ventajas del Sistema de Caché

### Performance
- **Carga instantánea**: UI se actualiza inmediatamente con datos locales
- **Sin esperas**: No hay loading states al navegar entre ProductList/Detail
- **Offline-first**: App funciona sin conexión

### UX Mejorada
- **Navegación fluida**: Cambios de pantalla sin delays
- **Menos consumo datos**: API calls solo cada 6 horas
- **Actualización transparente**: WorkManager sincroniza en segundo plano

### Sincronización Inteligente
- **Primera vez**: Carga inicial desde API
- **Uso normal**: Datos desde caché local
- **Background**: WorkManager mantiene datos frescos

## Cómo Agregar un Nuevo CoroutineWorker

### Paso 1: Crear el Worker
```kotlin
@HiltWorker
class MiNuevoWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val miRepository: MiRepository // Inyección con Hilt
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Tu lógica aquí
            miRepository.hacerAlgo()
            
            Result.success()
        } catch (exception: Exception) {
            Result.retry() // o Result.failure()
        }
    }
}
```

### Paso 2: Crear el Scheduler
```kotlin
@Singleton
class MiWorkerScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val workManager = WorkManager.getInstance(context)

    fun programarTarea() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<MiNuevoWorker>(
            12, TimeUnit.HOURS // Cada 12 horas
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "mi_trabajo_unico",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }
}
```

### Paso 3: Programar en la App
```kotlin
// En EccomerceApp.onCreate()
@Inject
lateinit var miWorkerScheduler: MiWorkerScheduler

override fun onCreate() {
    super.onCreate()
    miWorkerScheduler.programarTarea()
}
```

## Tipos de Trabajo

### PeriodicWorkRequest
- **Uso**: Tareas que se repiten (como sincronización)
- **Intervalo mínimo**: 15 minutos
- **Ejemplo**: Sincronizar cada 6 horas

### OneTimeWorkRequest
- **Uso**: Tareas que se ejecutan una vez
- **Ejemplo**: Subir una foto, enviar analytics

## Configuraciones Útiles

### Restricciones (Constraints)
```kotlin
val constraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED)     // Requiere internet
    .setRequiresBatteryNotLow(true)                    // Batería no baja
    .setRequiresCharging(true)                         // Solo si está cargando
    .setRequiresDeviceIdle(true)                       // Solo si device idle
    .build()
```

### Políticas de Conflicto
- **KEEP**: Mantiene el trabajo existente
- **REPLACE**: Reemplaza el trabajo existente
- **APPEND**: Agrega al final de la cola
