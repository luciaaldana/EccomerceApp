# Sistema de Autenticación - Documentación para Desarrolladores

## Tabla de Contenidos
1. [Visión General](#visión-general)
2. [Arquitectura del Sistema](#arquitectura-del-sistema)
3. [Flujo de Datos](#flujo-de-datos)
4. [Estructura de Módulos](#estructura-de-módulos)
5. [Implementación Paso a Paso](#implementación-paso-a-paso)
6. [Protección de Rutas](#protección-de-rutas)
7. [Gestión de Estado](#gestión-de-estado)
8. [API y Ejemplos de Respuesta](#api-y-ejemplos-de-respuesta)

## Visión General

El sistema de autenticación de la aplicación sigue los principios de **Clean Architecture** con una clara separación de responsabilidades entre las capas de dominio, datos y UI. Utiliza **Hilt** para inyección de dependencias y **Jetpack Compose** para la interfaz de usuario.

### Funcionalidades Principales
- ✅ Registro de usuarios
- ✅ Inicio de sesión
- ✅ Actualización de perfil
- ✅ Cierre de sesión
- ✅ Protección de rutas
- ✅ Gestión de estado de autenticación

## Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────┐
│                        UI LAYER                             │
│  ┌─────────────┐  ┌──────────────┐  ┌─────────────┐         │
│  │ LoginScreen │  │RegisterScreen│  │ProfileScreen│         │
│  │     +       │  │      +       │  │      +      │         │
│  │ LoginVM     │  │ RegisterVM   │  │ ProfileVM   │         │
│  └─────────────┘  └──────────────┘  └─────────────┘         │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      DOMAIN LAYER                           │
│                ┌─────────────────────┐                      │
│                │   AuthRepository    │                      │
│                │    (Interface)      │                      │
│                └─────────────────────┘                      │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                       DATA LAYER                            │
│  ┌──────────────────┐  ┌─────────────┐  ┌─────────────────┐ │
│  │AuthRepositoryImpl│  │   UserApi   │  │   UserDao       │ │
│  │                  │  │ (Network)   │  │ (Database)      │ │
│  └──────────────────┘  └─────────────┘  └─────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

## Flujo de Datos

### 1. Flujo de Login
```
LoginScreen → LoginViewModel → AuthRepository → AuthRepositoryImpl
     ↓              ↓               ↓                    ↓
  UI Events    State Updates    Business Logic    Network + DB
     ↑              ↑               ↑                    ↑
Navigation ← UI State Update ← Success/Error ← API Response
```

### 2. Flujo de Registro
```
RegisterScreen → RegisterViewModel → AuthRepository → Network API
      ↓                 ↓                  ↓              ↓
   Form Data      Validation        Business Rules   HTTP Request
      ↑                 ↑                  ↑              ↑
   Navigate ←    Update UI State ←    Process Result ← Response
```

### 3. Flujo de Actualización de Perfil
```
ProfileScreen → ProfileViewModel → AuthRepository → API + Local Storage
     ↓               ↓                   ↓                    ↓
  Form Changes   State Management   Update Logic      Persistence
     ↑               ↑                   ↑                    ↑
  UI Update ←   Success Message ←   Validate Result ←   Store Data
```

## Estructura de Módulos

### 📁 Domain Layer (`domain/auth/`)
```kotlin
// AuthRepository.kt - Interfaz que define las operaciones de autenticación
interface AuthRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun register(user: User): Boolean
    suspend fun updateUserProfile(user: User): Boolean
    fun logout()
    fun getCurrentUser(): User?
}
```

### 📁 Data Layer (`data/auth/`)
```
data/auth/
├── AuthRepositoryImpl.kt      # Implementación del repositorio
├── network/
│   ├── UserApi.kt            # Interfaz Retrofit
│   └── dto/                  # Data Transfer Objects
│       ├── LoginDto.kt
│       ├── LoginResponse.kt
│       ├── UserRegistrationDto.kt
│       └── UpdateUserDto.kt
├── utils/
│   └── PasswordEncoder.kt    # Codificación de contraseñas
└── di/
    └── AuthModule.kt         # Configuración de Hilt
```

### 📁 UI Layer (`feature/`)
```
feature/
├── login/
│   ├── LoginScreen.kt        # Interfaz de usuario
│   └── LoginViewModel.kt     # Lógica de presentación
├── register/
│   ├── RegisterScreen.kt
│   └── RegisterViewModel.kt
└── profile/
    ├── ProfileScreen.kt
    └── ProfileViewModel.kt
```

## Implementación Paso a Paso

### Paso 1: Definir el Modelo de Usuario
```kotlin
// core/model/User.kt
@Parcelize
data class User(
    val _id: String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    @Json(name = "encryptedPassword") val password: String = "",
    val nationality: String,
    val userImageUrl: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val __v: Int? = null
) : Parcelable
```

### Paso 2: Crear la Interfaz del Repositorio
```kotlin
// domain/auth/AuthRepository.kt
interface AuthRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun register(user: User): Boolean
    suspend fun updateUserProfile(user: User): Boolean
    fun logout()
    fun updateCurrentUser(updatedUser: User)
    fun getCurrentUser(): User?
}
```

### Paso 3: Implementar el Repositorio
```kotlin
// data/auth/AuthRepositoryImpl.kt
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val userDao: UserDao
) : AuthRepository {
    
    private var loggedInUser: User? = null
    
    override suspend fun login(email: String, password: String): Boolean {
        return try {
            val encodedPassword = PasswordEncoder.encodePassword(password)
            val loginDto = LoginDto(email.trim(), encodedPassword)
            val response = userApi.loginUser(loginDto)
            
            when (response.code()) {
                200 -> {
                    loggedInUser = response.body()?.user
                    loggedInUser?.let { userDao.insertUser(it.toUserEntity()) }
                    true
                }
                else -> false
            }
        } catch (e: Exception) {
            false
        }
    }
}
```

### Paso 4: Crear el ViewModel
```kotlin
// feature/login/LoginViewModel.kt
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoggedIn by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)
    
    fun onLoginClick() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            val success = authRepository.login(email, password)
            
            if (success) {
                isLoggedIn = true
            } else {
                errorMessage = "Email o contraseña incorrectos"
            }
            
            isLoading = false
        }
    }
}
```

### Paso 5: Implementar la Pantalla
```kotlin
// feature/login/LoginScreen.kt
@Composable
fun LoginScreen(navController: NavController) {
    val viewModel: LoginViewModel = hiltViewModel()
    
    // Navegación automática después del login exitoso
    if (viewModel.isLoggedIn) {
        LaunchedEffect(Unit) {
            navController.navigate("productList") {
                popUpTo("login") { inclusive = true }
            }
        }
    }
    
    Column(modifier = Modifier.padding(32.dp)) {
        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text("Email") }
        )
        
        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )
        
        Button(
            onClick = { viewModel.onLoginClick() },
            enabled = !viewModel.isLoading
        ) {
            Text(if (viewModel.isLoading) "Cargando..." else "Ingresar")
        }
        
        // Botón Cancelar
        OutlinedButton(
            onClick = { 
                navController.navigate("productList") {
                    popUpTo("login") { inclusive = true }
                }
            }
        ) {
            Text("Cancelar")
        }
    }
}
```

## Protección de Rutas

### Implementación en Navigation Graph
```kotlin
// navigation/AppNavGraph.kt
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "productList") {
        
        // Rutas públicas
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("productList") { ProductListScreen(navController) }
        
        // Rutas protegidas
        composable("profile") { 
            val viewModel: ProductsViewModel = hiltViewModel()
            if (viewModel.isUserLoggedIn()) {
                ProfileScreen(navController)
            } else {
                navController.navigate("login") {
                    popUpTo("profile") { inclusive = true }
                }
            }
        }
        
        // Protección en operaciones específicas
        composable("detail/{productId}") { backStackEntry ->
            // ... código para obtener el producto
            DetailScreen(
                onAddToCart = { 
                    if (viewModel.isUserLoggedIn()) {
                        cartViewModel.add(product)
                    } else {
                        navController.navigate("login")
                    }
                }
            )
        }
    }
}
```

## Gestión de Estado

### Estado Global de Autenticación
```kotlin
// El estado se mantiene en AuthRepositoryImpl
class AuthRepositoryImpl {
    private var loggedInUser: User? = null  // Estado en memoria
    private val registeredUsers = mutableListOf<User>()  // Cache local
    
    fun getCurrentUser(): User? = loggedInUser
    
    fun logout() {
        loggedInUser = null  // Limpiar estado
    }
}
```

### Estado Reactivo en ViewModels
```kotlin
// ProfileViewModel.kt
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    // Estados reactivos con StateFlow
    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName.asStateFlow()
    
    private val _isUpdating = MutableStateFlow(false)
    val isUpdating: StateFlow<Boolean> = _isUpdating.asStateFlow()
    
    init {
        // Cargar datos del usuario actual
        authRepository.getCurrentUser()?.let { user ->
            _firstName.value = user.firstName
            // ... cargar otros campos
        }
    }
}
```

## API y Ejemplos de Respuesta

### Base URL
```
http://10.0.2.2:10000 // o la url de render
```

### Endpoints de Autenticación

#### 1. Login (POST /users/login)

**Request Body:**
```json
{
  "email": "usuario@ejemplo.com",
  "encryptedPassword": "cGFzc3dvcmQxMjM="
}
```

**Respuesta Exitosa (200):**
```json
{
  "message": "Login exitoso",
  "user": {
    "_id": "645a1234567890abcdef1234",
    "email": "usuario@ejemplo.com",
    "firstName": "Juan",
    "lastName": "Pérez",
    "nationality": "Colombia",
    "userImageUrl": "https://ejemplo.com/imagen.jpg",
    "createdAt": "2023-05-09T10:30:00.000Z",
    "updatedAt": "2023-05-09T10:30:00.000Z"
  }
}
```

**Error - Usuario no encontrado (404):**
```json
{
  "message": "Usuario no encontrado"
}
```

**Error - Contraseña incorrecta (401):**
```json
{
  "message": "Contraseña incorrecta"
}
```

#### 2. Registro (POST /users/register)

**Request Body:**
```json
{
  "email": "nuevousuario@ejemplo.com",
  "firstName": "María",
  "lastName": "García",
  "nationality": "México",
  "encryptedPassword": "cGFzc3dvcmQxMjM="
}
```

**Respuesta Exitosa (201):**
```json
{
  "_id": "645a1234567890abcdef1234",
  "email": "nuevousuario@ejemplo.com",
  "firstName": "María",
  "lastName": "García",
  "nationality": "México",
  "encryptedPassword": "cGFzc3dvcmQxMjM=",
  "createdAt": "2023-05-09T10:30:00.000Z",
  "updatedAt": "2023-05-09T10:30:00.000Z",
  "__v": 0
}
```

**Error - Usuario existe (409):**
```json
{
  "message": "El usuario ya existe"
}
```

#### 3. Actualizar Perfil (PUT /users/update/:email)

**Request Body:**
```json
{
  "firstName": "Juan Carlos",
  "lastName": "Pérez González",
  "nationality": "Colombia",
  "userImageUrl": "https://nuevo-ejemplo.com/nueva-imagen.jpg"
}
```

**Respuesta Exitosa (200):**
```json
{
  "_id": "645a1234567890abcdef1234",
  "email": "usuario@ejemplo.com",
  "firstName": "Juan Carlos",
  "lastName": "Pérez González",
  "nationality": "Colombia",
  "encryptedPassword": "cGFzc3dvcmQxMjM=",
  "userImageUrl": "https://nuevo-ejemplo.com/nueva-imagen.jpg",
  "createdAt": "2023-05-09T10:30:00.000Z",
  "updatedAt": "2023-05-09T15:45:00.000Z",
  "__v": 0
}
```

**Error - Usuario no encontrado (404):**
```json
{
  "message": "Usuario no encontrado"
}
```

### Manejo de Errores en el Cliente

```kotlin
// En AuthRepositoryImpl.kt
when (response.code()) {
    200 -> {
        // Login exitoso
        val loginResponse = response.body()
        loginResponse?.user?.let { user ->
            loggedInUser = user
            println("Login successful for ${user.email}")
        }
        true
    }
    401 -> {
        // Contraseña incorrecta
        println("Login failed - invalid credentials")
        false
    }
    404 -> {
        // Usuario no encontrado
        println("Login failed - user not found")
        false
    }
    else -> {
        // Error del servidor u otro error
        println("Login failed - server error: ${response.code()}")
        false
    }
}
```

### Configuración de Red

```kotlin
// En NetworkModule.kt
@Provides @Singleton
fun provideOkHttp(): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .connectTimeout(8, TimeUnit.SECONDS)    // Tiempo para establecer conexión
        .readTimeout(8, TimeUnit.SECONDS)       // Tiempo para leer respuesta
        .writeTimeout(8, TimeUnit.SECONDS)      // Tiempo para enviar datos
        .callTimeout(10, TimeUnit.SECONDS)      // Tiempo total de la llamada
        .build()

@Provides @Singleton
fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit =
    Retrofit.Builder()
        .baseUrl(BuildConfig.RENDER_BASE_URL) // http://10.0.2.2:10000/
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
```

## ¿Por qué son Necesarios los Timeouts?

Los timeouts en la configuración de red son **esenciales** para una aplicación robusta y con buena experiencia de usuario.

### **Prevenir Bloqueo Indefinido**

```kotlin
// Sin timeout - ¡PELIGROSO!
// La app se puede congelar para siempre esperando respuesta
val response = api.login() // Puede esperar infinitamente

// Con timeout - SEGURO
.connectTimeout(8, TimeUnit.SECONDS) // Máximo 8s para conectar
```

### **Mejor Experiencia de Usuario**
- **Sin timeout**: Usuario espera indefinidamente sin feedback
- **Con timeout**: Después de 8-10s muestra error claro "No se pudo conectar"

### **Tipos de Timeout en la App**

```kotlin
.connectTimeout(8, TimeUnit.SECONDS)    // Establecer conexión TCP
.readTimeout(8, TimeUnit.SECONDS)       // Leer respuesta completa del servidor
.writeTimeout(8, TimeUnit.SECONDS)      // Enviar datos al servidor
.callTimeout(10, TimeUnit.SECONDS)      // Tiempo total de toda la operación
```

### **Escenarios Reales donde son Críticos**

#### **Red Lenta/Inestable:**
- WiFi público débil
- Datos móviles con mala señal
- Servidor sobrecargado

#### **Problemas de Servidor:**
- API caída o lenta
- Base de datos bloqueada
- Mantenimiento no planeado

#### **Problemas de Conectividad:**
- Usuario en túnel/elevador
- Cambio de red (WiFi → Datos)
- Firewall/proxy bloqueando

### **Impacto Sin Timeouts**

```kotlin
// MALO - Sin timeouts
fun login() {
    // Usuario toca "Ingresar"
    val response = api.login() // Se queda esperando...
    // App congelada, usuario frustrado
    // Batería drenándose, memoria ocupada
}

// BUENO - Con timeouts  
fun login() {
    try {
        val response = api.login() // Máximo 8-10s
        handleSuccess(response)
    } catch (e: SocketTimeoutException) {
        showError("Conexión lenta, intenta de nuevo")
    }
}
```

### **¿Por qué 8 segundos?**

La app usa **8 segundos** porque:

- ✅ **Autenticación es crítica**: Login/registro son operaciones **síncronas** donde el usuario espera activamente
- ✅ **Primeras impresiones**: Sin timeout = app parece "rota"
- ✅ **Feedback rápido**: 8s es suficiente para redes lentas pero no frustra al usuario
- ✅ **Gestión de recursos**: Libera conexiones y memoria más rápido

### **Gestión de Recursos**

Los timeouts también:
- **Liberan conexiones** del pool de OkHttp
- **Evitan memory leaks** por operaciones colgadas  
- **Mejoran performance** general de la app
- **Permiten reintentos** más rápidos
