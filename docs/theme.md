# Sistema de Temas - EccomerceApp

## Resumen

El sistema de temas de la aplicación permite alternar entre modo claro, oscuro y seguir las preferencias del sistema. Está basado en Material Design 3 con una paleta de colores personalizada.

## Arquitectura del Sistema

### Componentes Principales

```
├── core/ui/theme/
│   ├── Color.kt              # Paleta de colores personalizada
│   ├── Theme.kt              # Configuración de Material Design 3
│   ├── ThemeRepository.kt    # Persistencia de preferencias
│   └── ThemeProvider.kt      # Provider de estado (opcional)
├── components/
│   └── ThemeToggle.kt        # Componente UI para cambiar tema
└── viewmodels/
    ├── MainViewModel.kt      # Estado global del tema
    └── ProfileViewModel.kt   # Gestión desde perfil
```

## Paleta de Colores

### Color Principal
- **Base**: `oklch(55.3% 0.195 38.402)` → `#8B5A2B`
- Inspirado en tonos cálidos y naturales

### Esquema Completo

#### Modo Claro (Light)
```kotlin
LightPrimary = Color(0xFF8B5A2B)           // Marrón principal
LightOnPrimary = Color(0xFFFFFFFF)         // Texto sobre primary
LightPrimaryContainer = Color(0xFFFFDCC7)  // Contenedor primary suave
LightBackground = Color(0xFFFFFBF7)        // Fondo principal
LightSurface = Color(0xFFFFFBF7)           // Superficies
LightError = Color(0xFFBA1A1A)             // Estados de error
```

#### Modo Oscuro (Dark)
```kotlin
DarkPrimary = Color(0xFFFFB786)            // Primary adaptado para oscuro
DarkOnPrimary = Color(0xFF4A2800)          // Texto sobre primary
DarkPrimaryContainer = Color(0xFF693E15)   // Contenedor primary
DarkBackground = Color(0xFF17120E)         // Fondo oscuro
DarkSurface = Color(0xFF17120E)            // Superficies oscuras
DarkError = Color(0xFFFFB4AB)              // Error en modo oscuro
```

### Accesibilidad
- ✅ Todos los colores cumplen **WCAG AA** para contraste
- ✅ Ratios de contraste > 4.5:1 para texto normal
- ✅ Ratios de contraste > 3:1 para elementos grandes

## Modos de Tema

### ThemeMode Enum
```kotlin
enum class ThemeMode {
    LIGHT,   // Modo claro forzado
    DARK,    // Modo oscuro forzado  
    SYSTEM   // Sigue preferencias del sistema
}
```

### Comportamiento por Modo

| Modo | Comportamiento |
|------|----------------|
| `LIGHT` | Siempre usa esquema claro |
| `DARK` | Siempre usa esquema oscuro |
| `SYSTEM` | Detecta automáticamente con `isSystemInDarkTheme()` |

## Persistencia

### ThemeRepository
Gestiona el almacenamiento de preferencias usando **SharedPreferences**:

```kotlin
@Singleton
class ThemeRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // Guarda en SharedPreferences
    // Expone Flow<ThemeMode> reactivo
}
```

**Archivo**: `theme_preferences.xml`
**Clave**: `theme_mode`
**Valores**: `"LIGHT"`, `"DARK"`, `"SYSTEM"`

## Implementación

### 1. Configuración en MainActivity

```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
            
            EccomerceAppTheme(themeMode = themeMode) {
                // App content
            }
        }
    }
}
```

### 2. Theme Toggle en Perfil

```kotlin
// ProfileScreen.kt
ThemeToggle(
    selectedTheme = currentThemeMode,
    onThemeSelected = { newTheme ->
        viewModel.setThemeMode(newTheme)
    }
)
```

### 3. Componente ThemeToggle

Proporciona una interfaz de radio buttons con iconos para seleccionar el tema:

- 🌞 **Claro** - Modo claro forzado
- 🌙 **Oscuro** - Modo oscuro forzado  
- ⚙️ **Sistema** - Sigue preferencias del dispositivo

## Flujo de Cambio de Tema

### Paso a Paso
1. **Usuario toca opción** en el ThemeToggle del perfil
2. **ProfileViewModel** recibe la selección y llama `setThemeMode()`
3. **ThemeRepository** guarda en SharedPreferences y actualiza el StateFlow
4. **MainViewModel** observa el cambio automáticamente
5. **MainActivity** recibe el nuevo valor y pasa a `EccomerceAppTheme`
6. **Toda la UI se recompone** con los nuevos colores

### Flujo Visual
```
[Perfil] → [ViewModel] → [Repository] → [SharedPrefs + StateFlow]
                                              ↓
[MainActivity] ← [MainViewModel] ← [Flow actualizado]
       ↓
[UI actualizada con nuevos colores]
```

## Configuración vs Dynamic Color

### Dynamic Color Deshabilitado
```kotlin
EccomerceAppTheme(
    themeMode = themeMode,
    dynamicColor = false  // Usa nuestra paleta personalizada
)
```

**Razón**: Para mantener consistencia visual y mostrar nuestra paleta de marca en lugar de colores derivados del wallpaper del usuario.

## Uso en Componentes

### Acceso a Colores del Tema
```kotlin
@Composable
fun MyComponent() {
    val colors = MaterialTheme.colorScheme
    
    Card(
        colors = CardDefaults.cardColors(
            containerColor = colors.surface,
            contentColor = colors.onSurface
        )
    ) {
        Text(
            text = "Ejemplo",
            color = colors.primary
        )
    }
}
```

### Detección del Tema Actual
```kotlin
@Composable
fun MyComponent() {
    val isDark = isSystemInDarkTheme()
    // O acceder al estado global si es necesario
}
```

## Pruebas

### Casos de Prueba Recomendados

1. **Persistencia**: Verificar que la preferencia se guarda correctamente
2. **Cambio de tema**: Confirmar que la UI se actualiza inmediatamente
3. **Modo sistema**: Validar que detecta cambios del sistema
4. **Contraste**: Verificar accesibilidad en ambos modos
5. **Estado inicial**: Confirmar que inicia con `SYSTEM` por defecto

### Ejemplo de Test
```kotlin
@Test
fun `cuando se cambia tema, se persiste correctamente`() = runTest {
    // Given
    val repository = ThemeRepository(context)
    
    // When
    repository.setThemeMode(ThemeMode.DARK)
    
    // Then
    assertEquals(ThemeMode.DARK, repository.themeMode.first())
}
```

## Personalización Futura

### Agregar Nuevos Colores
1. Definir en `Color.kt` para ambos modos
2. Agregar al `lightColorScheme` y `darkColorScheme`
3. Verificar contraste de accesibilidad

### Nuevos Modos de Tema
1. Extender `ThemeMode` enum
2. Actualizar lógica en `Theme.kt`
3. Modificar `ThemeRepository` para persistencia
4. Actualizar `ThemeToggle` UI

## Consideraciones

### Performance
- ✅ Cambios de tema son instantáneos (StateFlow)
- ✅ No hay recomposiciones innecesarias
- ✅ SharedPreferences es eficiente para esta escala

### UX
- ✅ Opción "Sistema" respeta preferencias del usuario
- ✅ Cambios son inmediatos y visibles
- ✅ Estado se mantiene entre sesiones

### Mantenimiento
- ✅ Código centralizado en módulo `core:ui`
- ✅ Separación clara de responsabilidades
- ✅ Fácil extensión para nuevos temas