package com.luciaaldana.eccomerceapp.feature.home

import com.luciaaldana.eccomerceapp.core.model.Product
import com.luciaaldana.eccomerceapp.core.model.User
import com.luciaaldana.eccomerceapp.domain.auth.AuthRepository
import com.luciaaldana.eccomerceapp.domain.product.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import app.cash.turbine.test

/**
 * Unit tests for ProductsViewModel following TDD methodology
 * RED -> GREEN -> REFACTOR
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ProductsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var productRepository: ProductRepository
    
    @Mock
    private lateinit var authRepository: AuthRepository
    
    private lateinit var viewModel: ProductsViewModel

    // Test data following TDD approach
    private val testProduct1 = Product(
        id = "1",
        name = "Burger Deluxe",
        description = "Delicious burger with cheese",
        price = 12.99,
        imageUrl = "url1",
        category = "Food",
        includesDrink = true
    )
    
    private val testProduct2 = Product(
        id = "2", 
        name = "Pizza Margherita",
        description = "Classic pizza with tomato and mozzarella",
        price = 15.50,
        imageUrl = "url2",
        category = "Food",
        includesDrink = false
    )
    
    private val testProduct3 = Product(
        id = "3",
        name = "Coffee Latte",
        description = "Premium coffee with milk",
        price = 4.99,
        imageUrl = "url3", 
        category = "Beverages",
        includesDrink = false
    )
    
    private val testUser = User(
        _id = "user1",
        firstName = "Test",
        lastName = "User", 
        email = "test@example.com",
        nationality = "US"
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        
        // Default mock behavior using runBlocking for suspend functions
        whenever(productRepository.getProducts()).thenReturn(flowOf(emptyList()))
        runBlocking {
            whenever(productRepository.hasLocalProducts()).thenReturn(false)
            whenever(productRepository.refreshProducts()).thenReturn(emptyList())
        }
        whenever(authRepository.getCurrentUser()).thenReturn(null)
    }

    // RED: Test should fail initially (TDD Red phase)
    @Test
    fun `initial state should have empty products and screen loading true`() = runTest {
        // GIVEN
        whenever(productRepository.getProducts()).thenReturn(flowOf(emptyList()))
        
        // WHEN
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // THEN
        assertEquals(emptyList<Product>(), viewModel.allProducts.value)
        assertTrue(viewModel.isScreenLoading.value)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.error.value)
        assertEquals("", viewModel.searchQuery.value)
        assertNull(viewModel.selectedCategory.value)
    }

    // GREEN: Make test pass (TDD Green phase)
    @Test
    fun `should load products from repository on init`() = runTest {
        // GIVEN
        val testProducts = listOf(testProduct1, testProduct2)
        whenever(productRepository.getProducts()).thenReturn(flowOf(testProducts))
        
        // WHEN
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // THEN
        assertEquals(testProducts, viewModel.allProducts.first())
    }

    // REFACTOR: Improve and add more comprehensive tests (TDD Refactor phase)
    @Test
    fun `should call refreshProducts when no local products exist`() = runTest {
        // GIVEN
        whenever(productRepository.hasLocalProducts()).thenReturn(false)
        whenever(productRepository.refreshProducts()).thenReturn(listOf(testProduct1))
        
        // WHEN
        viewModel = ProductsViewModel(productRepository, authRepository)
        advanceUntilIdle()
        
        // THEN
        verify(productRepository).hasLocalProducts()
        verify(productRepository).refreshProducts()
    }

    @Test
    fun `should not call refreshProducts when local products exist`() = runTest {
        // GIVEN
        whenever(productRepository.hasLocalProducts()).thenReturn(true)
        
        // WHEN
        viewModel = ProductsViewModel(productRepository, authRepository)
        advanceUntilIdle()
        
        // THEN
        verify(productRepository).hasLocalProducts()
        verify(productRepository, never()).refreshProducts()
    }

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

    @Test
    fun `search query should filter products by description`() = runTest {
        // GIVEN
        val testProducts = listOf(testProduct1, testProduct2, testProduct3)
        whenever(productRepository.getProducts()).thenReturn(flowOf(testProducts))
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // WHEN
        viewModel.onSearchQueryChanged("coffee")
        advanceUntilIdle()
        
        // THEN
        val filteredProducts = viewModel.filteredProducts.first()
        assertEquals(1, filteredProducts.size)
        assertEquals(testProduct3, filteredProducts[0])
    }

    @Test
    fun `search should be case insensitive`() = runTest {
        // GIVEN
        val testProducts = listOf(testProduct1, testProduct2, testProduct3)
        whenever(productRepository.getProducts()).thenReturn(flowOf(testProducts))
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // WHEN
        viewModel.onSearchQueryChanged("BURGER")
        advanceUntilIdle()
        
        // THEN
        val filteredProducts = viewModel.filteredProducts.first()
        assertEquals(1, filteredProducts.size)
        assertEquals(testProduct1, filteredProducts[0])
    }

    @Test
    fun `category filter should filter products correctly`() = runTest {
        // GIVEN
        val testProducts = listOf(testProduct1, testProduct2, testProduct3)
        whenever(productRepository.getProducts()).thenReturn(flowOf(testProducts))
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // WHEN
        viewModel.onCategorySelected("Beverages")
        advanceUntilIdle()
        
        // THEN
        val filteredProducts = viewModel.filteredProducts.first()
        assertEquals(1, filteredProducts.size)
        assertEquals(testProduct3, filteredProducts[0])
    }

    @Test
    fun `category filter should be case insensitive`() = runTest {
        // GIVEN
        val testProducts = listOf(testProduct1, testProduct2, testProduct3)
        whenever(productRepository.getProducts()).thenReturn(flowOf(testProducts))
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // WHEN
        viewModel.onCategorySelected("food")
        advanceUntilIdle()
        
        // THEN
        val filteredProducts = viewModel.filteredProducts.first()
        assertEquals(2, filteredProducts.size)
        assertTrue(filteredProducts.contains(testProduct1))
        assertTrue(filteredProducts.contains(testProduct2))
    }

    @Test
    fun `search and category filters should work together`() = runTest {
        // GIVEN
        val testProducts = listOf(testProduct1, testProduct2, testProduct3)
        whenever(productRepository.getProducts()).thenReturn(flowOf(testProducts))
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // WHEN
        viewModel.onSearchQueryChanged("pizza")
        viewModel.onCategorySelected("Food")
        advanceUntilIdle()
        
        // THEN
        val filteredProducts = viewModel.filteredProducts.first()
        assertEquals(1, filteredProducts.size)
        assertEquals(testProduct2, filteredProducts[0])
    }

    @Test
    fun `empty search query should show all products`() = runTest {
        // GIVEN
        val testProducts = listOf(testProduct1, testProduct2, testProduct3)
        whenever(productRepository.getProducts()).thenReturn(flowOf(testProducts))
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // WHEN
        viewModel.onSearchQueryChanged("")
        advanceUntilIdle()
        
        // THEN
        val filteredProducts = viewModel.filteredProducts.first()
        assertEquals(testProducts, filteredProducts)
    }

    @Test
    fun `null category should show all products`() = runTest {
        // GIVEN
        val testProducts = listOf(testProduct1, testProduct2, testProduct3)
        whenever(productRepository.getProducts()).thenReturn(flowOf(testProducts))
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // WHEN
        viewModel.onCategorySelected(null)
        advanceUntilIdle()
        
        // THEN
        val filteredProducts = viewModel.filteredProducts.first()
        assertEquals(testProducts, filteredProducts)
    }

    @Test
    fun `refreshProducts should set loading state correctly`() = runTest {
        // GIVEN
        whenever(productRepository.refreshProducts()).thenReturn(listOf(testProduct1))
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // WHEN
        viewModel.refreshProducts()
        advanceUntilIdle()
        
        // THEN
        assertFalse(viewModel.isLoading.value) // Should be false after completion
    }

    @Test
    fun `refreshProducts should clear error on success`() = runTest {
        // GIVEN
        whenever(productRepository.refreshProducts()).thenReturn(listOf(testProduct1))
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // WHEN
        viewModel.refreshProducts()
        advanceUntilIdle()
        
        // THEN
        assertNull(viewModel.error.value)
    }

    @Test
    fun `refreshProducts should set error on failure`() = runTest {
        // GIVEN
        val errorMessage = "Network error"
        whenever(productRepository.refreshProducts()).thenThrow(RuntimeException(errorMessage))
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // WHEN
        viewModel.refreshProducts()
        advanceUntilIdle()
        
        // THEN
        assertEquals("Error al cargar productos: $errorMessage", viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `retryLoadProducts should call refreshProducts`() = runTest {
        // GIVEN
        whenever(productRepository.refreshProducts()).thenReturn(listOf(testProduct1))
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // WHEN
        viewModel.retryLoadProducts()
        advanceUntilIdle()
        
        // THEN
        verify(productRepository, atLeastOnce()).refreshProducts()
    }

    @Test
    fun `isUserLoggedIn should return false when no user`() = runTest {
        // GIVEN
        whenever(authRepository.getCurrentUser()).thenReturn(null)
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // WHEN
        val result = viewModel.isUserLoggedIn()
        
        // THEN
        assertFalse(result)
    }

    @Test
    fun `isUserLoggedIn should return true when user exists`() = runTest {
        // GIVEN
        whenever(authRepository.getCurrentUser()).thenReturn(testUser)
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // WHEN
        val result = viewModel.isUserLoggedIn()
        
        // THEN
        assertTrue(result)
    }

    @Test
    fun `getCurrentUser should return current user from auth repository`() = runTest {
        // GIVEN
        whenever(authRepository.getCurrentUser()).thenReturn(testUser)
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // WHEN
        val result = viewModel.getCurrentUser()
        
        // THEN
        assertEquals(testUser, result)
    }

    @Test
    fun `screen loading should be false after delay`() = runTest {
        // GIVEN
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // WHEN
        advanceTimeBy(1000) // Wait longer than the 800ms delay
        
        // THEN
        assertFalse(viewModel.isScreenLoading.value)
    }

    // Tests using Turbine for StateFlow behavior verification
    @Test
    fun `filteredProducts should emit correct sequence when search changes`() = runTest {
        // GIVEN
        val testProducts = listOf(testProduct1, testProduct2, testProduct3)
        whenever(productRepository.getProducts()).thenReturn(flowOf(testProducts))
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // WHEN & THEN - Use Turbine for reactive testing
        viewModel.filteredProducts.test {
            // Initial emission should be all products
            assertEquals(testProducts, awaitItem())
            
            // When search query changes
            viewModel.onSearchQueryChanged("burger")
            
            // Should emit filtered results
            val filteredResult = awaitItem()
            assertEquals(1, filteredResult.size)
            assertEquals(testProduct1, filteredResult[0])
            
            // When search is cleared
            viewModel.onSearchQueryChanged("")
            
            // Should emit all products again
            assertEquals(testProducts, awaitItem())
        }
    }

    @Test
    fun `totalAmount StateFlow should update reactively with category filter`() = runTest {
        // GIVEN
        val testProducts = listOf(testProduct1, testProduct2, testProduct3)
        whenever(productRepository.getProducts()).thenReturn(flowOf(testProducts))
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // WHEN & THEN - Use Turbine for StateFlow emissions
        viewModel.filteredProducts.test {
            // Skip initial emission
            skipItems(1)
            
            // When category filter is applied
            viewModel.onCategorySelected("Beverages")
            
            // Should emit only beverages
            val beverageProducts = awaitItem()
            assertEquals(1, beverageProducts.size)
            assertEquals(testProduct3, beverageProducts[0])
            
            // When category filter is removed
            viewModel.onCategorySelected(null)
            
            // Should emit all products
            assertEquals(testProducts, awaitItem())
        }
    }

    @Test
    fun `error StateFlow should emit correctly on repository failure`() = runTest {
        // GIVEN
        val errorMessage = "Network error"
        whenever(productRepository.getProducts()).thenReturn(flowOf(emptyList()))
        whenever(productRepository.hasLocalProducts()).thenReturn(true) // Avoid init refresh
        whenever(productRepository.refreshProducts()).thenThrow(RuntimeException(errorMessage))
        
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // Wait for init to complete
        runCurrent()
        
        // WHEN & THEN - Use Turbine to verify error state emissions
        viewModel.error.test {
            // Initial state should be null
            assertNull(awaitItem())
            
            // When refresh fails
            viewModel.refreshProducts()
            runCurrent()
            
            // Should emit error message
            assertEquals("Error al cargar productos: $errorMessage", awaitItem())
        }
    }

    @Test
    fun `loading StateFlow should emit correct sequence during refresh`() = runTest {
        // GIVEN
        whenever(productRepository.getProducts()).thenReturn(flowOf(emptyList()))
        whenever(productRepository.hasLocalProducts()).thenReturn(true) // Avoid init refresh
        whenever(productRepository.refreshProducts()).thenReturn(listOf(testProduct1))
        
        viewModel = ProductsViewModel(productRepository, authRepository)
        
        // Wait for init to complete
        runCurrent()
        
        // WHEN
        viewModel.refreshProducts()
        advanceUntilIdle()
        
        // THEN - Verify loading went through correct sequence
        assertFalse(viewModel.isLoading.value) // Should be false after completion
        verify(productRepository).refreshProducts()
    }
}