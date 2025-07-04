# 📝 Documentación Completa: Testing de CartViewModel

Esta documentación explica en detalle cómo funciona el testing del `CartViewModel`, qué herramientas se usan, por qué se usan, y las mejores prácticas aplicadas.

## 🎯 ¿Qué es CartViewModel y por qué testear?

### ¿Qué hace CartViewModel?
`CartViewModel` es la capa de presentación que maneja la lógica del carrito de compras:
- Mantiene la lista de items del carrito (`cartItems`)
- Calcula el total automáticamente (`totalAmount`)
- Proporciona métodos para modificar el carrito (`add`, `remove`, `updateQuantity`, `clearCart`)

### ¿Por qué testear ViewModels?
```kotlin
// Sin tests: ¿Funciona el cálculo del total?
val total = cartItems.sumOf { it.product.price * it.quantity }

// Con tests: Confirmamos que funciona correctamente
@Test
fun `totalAmount should calculate correctly with multiple items`()
```

**Beneficios:**
- ✅ **Confianza**: Sabemos que la lógica funciona
- ✅ **Refactoring seguro**: Los cambios no rompen funcionalidad
- ✅ **Documentación viva**: Los tests explican cómo debe comportarse el código
- ✅ **Detección temprana de bugs**: Errores se encuentran antes de producción

## 🛠️ Herramientas y Frameworks Utilizados

### 1. JUnit 4 - Framework de Testing Base
```kotlin
@Test
fun `test name in backticks for readability`() = runTest {
    // Test implementation
}
```

**¿Por qué JUnit 4?**
- Framework estándar para Android
- Amplio soporte y documentación
- Integración perfecta con Android Studio

### 2. MockK - Framework de Mocking
```kotlin
private lateinit var cartItemRepository: CartItemRepository

@Before
fun setup() {
    cartItemRepository = mockk(relaxed = true)
    every { cartItemRepository.getCartItems() } returns emptyList()
}
```

**¿Qué es mocking?**
- **Mock**: Objeto falso que simula el comportamiento de una dependencia real
- **¿Por qué usar mocks?**: Aislar la unidad bajo test (ViewModel) de sus dependencias

**MockK vs Mockito:**
```kotlin
// MockK (usado en el proyecto)
every { repository.getCartItems() } returns listOf(item1, item2)
verify { repository.addProduct(product) }

// Mockito (alternativa)
when(repository.getCartItems()).thenReturn(listOf(item1, item2))
verify(repository).addProduct(product)
```

**Ventajas de MockK:**
- Diseñado específicamente para Kotlin
- Mejor soporte para corrutinas
- Sintaxis más natural en Kotlin

### 3. Coroutines Testing - Manejo de Asincronía
```kotlin
@get:Rule
val dispatcherRule = MainDispatcherRule()

@Test
fun `test coroutines`() = runTest {
    // Test que maneja corrutinas
    advanceUntilIdle() // Espera que terminen todas las corrutinas
}
```

**¿Por qué es necesario?**
- `CartViewModel` usa corrutinas y StateFlow
- `runTest` proporciona un entorno controlado para tests
- `advanceUntilIdle()` asegura que todas las operaciones asíncronas terminen

### 4. Turbine - Testing de StateFlow/Flow
```kotlin
cartViewModel.totalAmount.test {
    assertEquals(0.0, awaitItem()) // Primera emisión
    
    cartViewModel.add(product)
    
    assertEquals(31.98, awaitItem()) // Segunda emisión
}
```

**¿Qué es Turbine?**
- Librería especializada para testear Kotlin Flows
- Permite verificar emisiones de StateFlow de forma secuencial
- Maneja automáticamente el timing de las emisiones

## 📊 Anatomía de un Test de CartViewModel

### Estructura Básica: Given-When-Then
```kotlin
@Test
fun `add should call repository addProduct and reload cart`() = runTest {
    // GIVEN (Preparación)
    val updatedItems = listOf(sampleCartItem1)
    every { cartItemRepository.getCartItems() } returnsMany listOf(emptyList(), updatedItems)
    val testViewModel = CartViewModel(cartItemRepository)
    advanceUntilIdle()

    // WHEN (Acción)
    testViewModel.add(sampleProduct1)
    advanceUntilIdle()

    // THEN (Verificación)
    verify { cartItemRepository.addProduct(sampleProduct1) }
    assertEquals(updatedItems, testViewModel.cartItems.value)
}
```

### ¿Por qué Given-When-Then?
- **Claridad**: Separa preparación, acción y verificación
- **Legibilidad**: Cualquier desarrollador entiende qué hace el test
- **Mantenibilidad**: Fácil modificar cada sección independientemente

## 🔄 Patrones de Testing Aplicados

### 1. Test Data Builders - Datos de Prueba Reutilizables
```kotlin
private val sampleProduct1 = Product(
    id = "1",
    name = "Hamburguesa",
    description = "Deliciosa hamburguesa",
    price = 15.99,
    imageUrl = "https://example.com/burger.jpg",
    category = "Comida",
    includesDrink = false
)

private val sampleCartItem1 = CartItem(product = sampleProduct1, quantity = 2)
```

**Ventajas:**
- **Reutilización**: Mismo objeto en múltiples tests
- **Consistencia**: Datos predecibles y controlados
- **Mantenimiento**: Cambio en un lugar afecta todos los tests

### 2. Fresh ViewModel Pattern - ViewModel Fresco por Test
```kotlin
@Test
fun `specific test case`() = runTest {
    // Configurar mock específico para este test
    every { cartItemRepository.getCartItems() } returnsMany listOf(...)
    
    // Crear ViewModel fresco con esta configuración
    val testViewModel = CartViewModel(cartItemRepository)
    advanceUntilIdle()
    
    // Test específico con estado limpio
}
```

**¿Por qué no reutilizar el ViewModel del @Before?**
```kotlin
// ❌ PROBLEMÁTICO: ViewModel del setup
@Before
fun setup() {
    cartViewModel = CartViewModel(cartItemRepository) // Estado ya inicializado
}

@Test
fun `test`() {
    every { cartItemRepository.getCartItems() } returns newData
    // ❌ El ViewModel ya tiene el estado anterior, el mock no surte efecto
}

// ✅ CORRECTO: ViewModel fresco por test
@Test
fun `test`() {
    every { cartItemRepository.getCartItems() } returns newData
    val testViewModel = CartViewModel(cartItemRepository) // Estado limpio
}
```

### 3. Mock Sequencing - Simulación de Estados Secuenciales
```kotlin
// ✅ CORRECTO: returnsMany con lista
every { cartItemRepository.getCartItems() } returnsMany listOf(
    emptyList(),              // Primera llamada (init)
    listOf(sampleCartItem1)   // Segunda llamada (después de add)
)

// ❌ INCORRECTO: Mezclar sintaxis
every { cartItemRepository.getCartItems() } returnsMany 
    emptyList() andThen listOf(sampleCartItem1)
```

**¿Por qué returnsMany?**
- Simula el comportamiento real del repositorio a través del tiempo
- Cada llamada al método devuelve el siguiente valor en la secuencia
- Perfecto para testear operaciones que modifican estado

## 🧪 Tipos de Tests Implementados

### 1. Tests de Inicialización
```kotlin
@Test
fun `init should load cart items from repository`() = runTest {
    val expectedItems = listOf(sampleCartItem1, sampleCartItem2)
    every { cartItemRepository.getCartItems() } returns expectedItems
    
    val viewModel = CartViewModel(cartItemRepository)
    
    viewModel.cartItems.test {
        assertEquals(expectedItems, awaitItem())
    }
}
```

**¿Qué verifica?**
- El ViewModel carga datos del repositorio al inicializarse
- El estado inicial es correcto

### 2. Tests de Operaciones CRUD
```kotlin
@Test
fun `add should call repository addProduct and reload cart`()
@Test
fun `remove should call repository removeProduct and reload cart`()
@Test
fun `updateQuantity should call repository updateQuantity and reload cart`()
@Test
fun `clearCart should call repository clearCart and reload cart`()
```

**¿Qué verifican?**
- **Interacción**: Se llama al método correcto del repositorio
- **Estado**: El estado del ViewModel se actualiza correctamente
- **Parámetros**: Se pasan los parámetros correctos

### 3. Tests de Cálculos Derivados
```kotlin
@Test
fun `totalAmount should be calculated correctly with multiple items`() = runTest {
    val cartItems = listOf(sampleCartItem1, sampleCartItem2)
    every { cartItemRepository.getCartItems() } returns cartItems
    val expectedTotal = (15.99 * 2) + (25.50 * 1) // 57.48
    
    val viewModel = CartViewModel(cartItemRepository)
    
    viewModel.totalAmount.test {
        assertEquals(expectedTotal, awaitItem(), 0.01)
    }
}
```

**¿Qué verifica?**
- Los cálculos matemáticos son correctos
- Los StateFlow derivados se actualizan apropiadamente

### 4. Tests de Flujos Complejos
```kotlin
@Test
fun `multiple operations should maintain state consistency`() = runTest {
    // Simula un usuario real: agregar items, luego remover uno
    every { cartItemRepository.getCartItems() } returnsMany listOf(
        emptyList(),                              // Estado inicial
        listOf(sampleCartItem1),                 // Después de add(product1)
        listOf(sampleCartItem1, sampleCartItem2), // Después de add(product2)
        listOf(sampleCartItem2)                  // Después de remove(product1)
    )
    
    val testViewModel = CartViewModel(cartItemRepository)
    advanceUntilIdle()
    
    // Ejecutar secuencia de operaciones
    testViewModel.add(sampleProduct1)
    advanceUntilIdle()
    assertEquals(listOf(sampleCartItem1), testViewModel.cartItems.value)
    
    testViewModel.add(sampleProduct2)
    advanceUntilIdle()
    assertEquals(listOf(sampleCartItem1, sampleCartItem2), testViewModel.cartItems.value)
    
    testViewModel.remove(sampleProduct1)
    advanceUntilIdle()
    assertEquals(listOf(sampleCartItem2), testViewModel.cartItems.value)
    
    // Verificar todas las interacciones
    verify { cartItemRepository.addProduct(sampleProduct1) }
    verify { cartItemRepository.addProduct(sampleProduct2) }
    verify { cartItemRepository.removeProduct(sampleProduct1) }
}
```

**¿Qué verifica?**
- Múltiples operaciones secuenciales funcionan correctamente
- El estado se mantiene consistente entre operaciones
- Simula un flujo de usuario real

## ⚡ Manejo de StateFlow y Timing

### El Problema del Timing
```kotlin
// ❌ PROBLEMÁTICO: StateFlow puede no estar actualizado
cartViewModel.add(product)
assertEquals(expectedItems, cartViewModel.cartItems.value) // Puede fallar

// ✅ SOLUCIÓN 1: advanceUntilIdle()
cartViewModel.add(product)
advanceUntilIdle() // Espera que todas las corrutinas terminen
assertEquals(expectedItems, cartViewModel.cartItems.value)

// ✅ SOLUCIÓN 2: Turbine para StateFlow reactivo
cartViewModel.cartItems.test {
    skipItems(1) // Saltar emisión inicial
    cartViewModel.add(product)
    assertEquals(expectedItems, awaitItem()) // Espera próxima emisión
}
```

### ¿Por qué ocurre esto?
```kotlin
// En CartViewModel.kt
val totalAmount: StateFlow<Double> = cartItems.map { list ->
    list.sumOf { it.product.price * it.quantity }
}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
```

- `stateIn()` crea un StateFlow que se actualiza asíncronamente
- Las corrutinas necesitan tiempo para propagarse
- En tests, debemos esperar explícitamente

## 🎨 Testing de StateFlow con Turbine

### Enfoque Directo (.value)
```kotlin
// Funciona para verificaciones simples después de operaciones síncronas
assertEquals(expectedValue, stateFlow.value)
```

### Enfoque Reactivo (Turbine)
```kotlin
// Mejor para verificar emisiones secuenciales
stateFlow.test {
    assertEquals(initialValue, awaitItem())   // Primera emisión
    
    performSomeAction()
    
    assertEquals(updatedValue, awaitItem())   // Segunda emisión
}
```

### ¿Cuándo usar cada uno?

**Usar .value cuando:**
- Verificas estado final después de `advanceUntilIdle()`
- El StateFlow es simple y no derivado
- Solo necesitas verificar el valor actual

**Usar Turbine cuando:**
- Verificas secuencia de emisiones
- StateFlow es derivado (usa `map`, `stateIn`, etc.)
- Quieres verificar el comportamiento reactivo

## 🚨 Problemas Comunes y Soluciones

### 1. Mock no surte efecto
```kotlin
// ❌ PROBLEMA: Configurar mock después de crear ViewModel
val viewModel = CartViewModel(repository)
every { repository.getCartItems() } returns newData // ¡Muy tarde!

// ✅ SOLUCIÓN: Configurar mock antes de crear ViewModel
every { repository.getCartItems() } returns newData
val viewModel = CartViewModel(repository)
```

### 2. StateFlow no se actualiza
```kotlin
// ❌ PROBLEMA: No esperar corrutinas
viewModel.add(product)
assertEquals(expected, viewModel.cartItems.value) // Puede fallar

// ✅ SOLUCIÓN: Esperar corrutinas
viewModel.add(product)
advanceUntilIdle()
assertEquals(expected, viewModel.cartItems.value)
```

### 3. Tests flakey (fallan aleatoriamente)
```kotlin
// ❌ PROBLEMA: Timing inconsistente
viewModel.totalAmount.test {
    viewModel.add(product)
    assertEquals(expected, awaitItem()) // Puede recibir valor anterior
}

// ✅ SOLUCIÓN: Manejar emisiones secuencialmente
viewModel.totalAmount.test {
    assertEquals(0.0, awaitItem()) // Valor inicial
    
    viewModel.add(product)
    
    assertEquals(expected, awaitItem()) // Nuevo valor
}
```

## 📋 Checklist para Tests de ViewModel

### ✅ Antes de escribir tests:
- [ ] ¿Entiendo qué hace cada método del ViewModel?
- [ ] ¿Qué dependencias necesito mockear?
- [ ] ¿Qué datos de prueba necesito?

### ✅ Al escribir tests:
- [ ] ¿Uso Given-When-Then?
- [ ] ¿Tengo nombres descriptivos para los tests?
- [ ] ¿Configuro mocks antes de crear ViewModel?
- [ ] ¿Uso `advanceUntilIdle()` después de operaciones asíncronas?

### ✅ Al verificar tests:
- [ ] ¿Verifico tanto las llamadas al repositorio como el estado final?
- [ ] ¿Manejo correctamente las emisiones de StateFlow?
- [ ] ¿Los tests son determinísticos (no flakey)?

## 🎓 Conceptos Clave Aprendidos

### 1. Isolation (Aislamiento)
```kotlin
// Cada test debe ser independiente
@Test
fun `test A`() {
    val testViewModel = CartViewModel(mockRepository) // Estado limpio
}

@Test  
fun `test B`() {
    val testViewModel = CartViewModel(mockRepository) // Estado limpio
}
```

### 2. Determinismo
```kotlin
// Tests deben tener el mismo resultado siempre
every { repository.getCartItems() } returns FIXED_DATA
// No usar datos aleatorios o dependientes del tiempo
```

### 3. Fast Feedback
```kotlin
// Tests rápidos = feedback inmediato
@Test
fun `simple validation`() = runTest {
    // Evitar operaciones lentas como I/O real
    // Usar mocks para simular dependencias
}
```

### 4. Readability
```kotlin
// Nombres descriptivos explican qué hace el test
@Test
fun `add should call repository addProduct and reload cart`()

// No usar nombres genéricos
@Test  
fun testAdd() // ❌ No explica qué verifica
```

## 🚀 Próximos Pasos

Se puede considerar:


3. **UI Tests**: Verificar que la UI refleja cambios del ViewModel
4. **Performance Tests**: Verificar que operaciones son eficientes

## 📚 Recursos Adicionales

- [Documentación oficial de Testing Android](https://developer.android.com/training/testing)
- [MockK Documentation](https://mockk.io/)
- [Turbine GitHub](https://github.com/cashapp/turbine)
- [Kotlin Coroutines Testing](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/)
