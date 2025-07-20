package com.luciaaldana.eccomerceapp.data.product

import com.luciaaldana.eccomerceapp.core.model.Product
import com.luciaaldana.eccomerceapp.data.database.dao.ProductDao
import com.luciaaldana.eccomerceapp.data.database.entity.ProductEntity
import com.luciaaldana.eccomerceapp.data.product.dto.ProductDto
import com.luciaaldana.eccomerceapp.data.product.network.ProductApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import java.io.IOException
import app.cash.turbine.test

/**
 * Unit tests for ProductRepositoryImpl following TDD methodology
 * RED -> GREEN -> REFACTOR
 */
class ProductRepositoryImplTest {

    @Mock
    private lateinit var productApi: ProductApi
    
    @Mock
    private lateinit var productDao: ProductDao
    
    private lateinit var repository: ProductRepositoryImpl

    // Test data following TDD approach
    private val testProductDto1 = ProductDto(
        id = "1",
        name = "Burger Deluxe",
        description = "Delicious burger with cheese",
        price = 12.99,
        imageUrl = "url1",
        category = "Food",
        hasDrink = true,
        createdAt = "2023-01-01T00:00:00.000Z",
        updatedAt = "2023-01-01T00:00:00.000Z"
    )

    private val testProductDto2 = ProductDto(
        id = "2",
        name = "Pizza Margherita", 
        description = "Classic pizza with tomato and mozzarella",
        price = 15.50,
        imageUrl = "url2",
        category = "Food",
        hasDrink = false,
        createdAt = "2023-01-01T00:00:00.000Z",
        updatedAt = "2023-01-01T00:00:00.000Z"
    )

    private val testProductEntity1 = ProductEntity(
        id = "1",
        name = "Burger Deluxe",
        description = "Delicious burger with cheese",
        price = 12.99,
        imageUrl = "url1",
        category = "Food",
        includesDrink = true,
        lastSyncTimestamp = System.currentTimeMillis()
    )

    private val testProductEntity2 = ProductEntity(
        id = "2",
        name = "Pizza Margherita",
        description = "Classic pizza with tomato and mozzarella", 
        price = 15.50,
        imageUrl = "url2",
        category = "Food",
        includesDrink = false,
        lastSyncTimestamp = System.currentTimeMillis()
    )

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

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = ProductRepositoryImpl(productApi, productDao)
    }

    // RED: Test should fail initially (TDD Red phase)
    @Test
    fun `getProducts should return flow of products from dao`() = runTest {
        // GIVEN
        val entities = listOf(testProductEntity1, testProductEntity2)
        whenever(productDao.getAllProducts()).thenReturn(flowOf(entities))

        // WHEN
        val result = repository.getProducts()

        // THEN
        val products = result.first()
        assertEquals(2, products.size)
        assertEquals(testProduct1.copy(), products[0])
        assertEquals(testProduct2.copy(), products[1])
    }

    // GREEN: Make test pass (TDD Green phase)
    @Test
    fun `getProducts should return empty flow when no products in dao`() = runTest {
        // GIVEN
        whenever(productDao.getAllProducts()).thenReturn(flowOf(emptyList()))

        // WHEN
        val result = repository.getProducts()

        // THEN
        assertEquals(emptyList<Product>(), result.first())
    }

    // REFACTOR: Improve and add more comprehensive tests (TDD Refactor phase)
    @Test
    fun `getProductById should return product when exists in dao`() = runTest {
        // GIVEN
        val productId = "1"
        whenever(productDao.getProductById(productId)).thenReturn(testProductEntity1)

        // WHEN
        val result = repository.getProductById(productId)

        // THEN
        assertEquals(testProduct1.copy(), result)
        verify(productDao).getProductById(productId)
    }

    @Test
    fun `getProductById should return null when product not exists in dao`() = runTest {
        // GIVEN
        val productId = "nonexistent"
        whenever(productDao.getProductById(productId)).thenReturn(null)

        // WHEN
        val result = repository.getProductById(productId)

        // THEN
        assertNull(result)
        verify(productDao).getProductById(productId)
    }

    @Test
    fun `syncProductsFromApi should fetch from api and save to dao`() = runTest {
        // GIVEN
        val apiProducts = listOf(testProductDto1, testProductDto2)
        whenever(productApi.getProducts()).thenReturn(apiProducts)

        // WHEN
        val result = repository.syncProductsFromApi()

        // THEN
        assertEquals(2, result.size)
        assertEquals(testProduct1.copy(), result[0])
        assertEquals(testProduct2.copy(), result[1])
        
        verify(productApi).getProducts()
        verify(productDao).clearAllProducts()
        verify(productDao).insertProducts(any())
    }

    @Test
    fun `syncProductsFromApi should clear existing products before inserting new ones`() = runTest {
        // GIVEN
        val apiProducts = listOf(testProductDto1)
        whenever(productApi.getProducts()).thenReturn(apiProducts)

        // WHEN
        repository.syncProductsFromApi()

        // THEN
        val inOrder = inOrder(productApi, productDao)
        inOrder.verify(productApi).getProducts()
        inOrder.verify(productDao).clearAllProducts()
        inOrder.verify(productDao).insertProducts(any())
    }

    @Test
    fun `syncProductsFromApi should handle api failures gracefully`() = runTest {
        // GIVEN - Test that verifies repository behavior when API fails
        val exception = RuntimeException("Network error")
        whenever(productApi.getProducts()).thenThrow(exception)

        // WHEN & THEN
        var exceptionThrown = false
        try {
            repository.syncProductsFromApi()
        } catch (e: Exception) {
            exceptionThrown = true
        }
        
        // Verify that an exception was thrown and API was called
        assertTrue("Expected exception to be thrown when API fails", exceptionThrown)
        verify(productApi).getProducts()
    }

    @Test
    fun `syncProductsFromApi should handle empty api response correctly`() = runTest {
        // GIVEN
        whenever(productApi.getProducts()).thenReturn(emptyList())

        // WHEN
        val result = repository.syncProductsFromApi()

        // THEN
        assertEquals(emptyList<Product>(), result)
        verify(productApi).getProducts()
        verify(productDao).clearAllProducts()
        verify(productDao).insertProducts(emptyList())
    }

    @Test
    fun `refreshProducts should delegate to syncProductsFromApi`() = runTest {
        // GIVEN
        val apiProducts = listOf(testProductDto1)
        whenever(productApi.getProducts()).thenReturn(apiProducts)

        // WHEN
        val result = repository.refreshProducts()

        // THEN
        assertEquals(1, result.size)
        assertEquals(testProduct1.copy(), result[0])
        
        verify(productApi).getProducts()
        verify(productDao).clearAllProducts()
        verify(productDao).insertProducts(any())
    }

    @Test
    fun `hasLocalProducts should return true when products exist in dao`() = runTest {
        // GIVEN
        whenever(productDao.getProductCount()).thenReturn(5)

        // WHEN
        val result = repository.hasLocalProducts()

        // THEN
        assertTrue(result)
        verify(productDao).getProductCount()
    }

    @Test
    fun `hasLocalProducts should return false when no products exist in dao`() = runTest {
        // GIVEN
        whenever(productDao.getProductCount()).thenReturn(0)

        // WHEN
        val result = repository.hasLocalProducts()

        // THEN
        assertFalse(result)
        verify(productDao).getProductCount()
    }

    @Test
    fun `repository should handle database errors gracefully`() = runTest {
        // GIVEN
        val exception = RuntimeException("Database error")
        whenever(productDao.getProductCount()).thenThrow(exception)

        // WHEN & THEN
        try {
            repository.hasLocalProducts()
            fail("Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals("Database error", e.message)
        }
    }

    @Test
    fun `syncProductsFromApi should handle large product lists efficiently`() = runTest {
        // GIVEN
        val largeProductList = (1..100).map { index ->
            ProductDto(
                id = index.toString(),
                name = "Product $index",
                description = "Description $index",
                price = index.toDouble(),
                imageUrl = "url$index",
                category = "Category",
                hasDrink = false,
                createdAt = "2023-01-01T00:00:00.000Z",
                updatedAt = "2023-01-01T00:00:00.000Z"
            )
        }
        whenever(productApi.getProducts()).thenReturn(largeProductList)

        // WHEN
        val result = repository.syncProductsFromApi()

        // THEN
        assertEquals(100, result.size)
        verify(productDao).insertProducts(argThat { entities -> entities.size == 100 })
    }

    @Test
    fun `mapper should handle edge cases correctly`() = runTest {
        // GIVEN
        val productWithEdgeCases = ProductDto(
            id = "1",
            name = "",
            description = "",
            price = 0.0,
            imageUrl = "",
            category = "",
            hasDrink = false,
            createdAt = "2023-01-01T00:00:00.000Z",
            updatedAt = "2023-01-01T00:00:00.000Z"
        )
        whenever(productApi.getProducts()).thenReturn(listOf(productWithEdgeCases))

        // WHEN
        val result = repository.syncProductsFromApi()

        // THEN
        assertEquals(1, result.size)
        val product = result[0]
        assertEquals("1", product.id)
        assertEquals("", product.name)
        assertEquals("", product.description)
        assertEquals(0.0, product.price, 0.001)
        assertEquals("", product.imageUrl)
        assertEquals("", product.category)
        assertFalse(product.includesDrink)
    }

    // Tests using Turbine for Flow behavior verification
    @Test
    fun `getProducts Flow should emit updated products when dao changes`() = runTest {
        // GIVEN
        val entities = listOf(testProductEntity1, testProductEntity2)
        whenever(productDao.getAllProducts()).thenReturn(flowOf(entities))

        // WHEN & THEN - Use Turbine for Flow testing
        repository.getProducts().test {
            // Should emit products
            val products = awaitItem()
            assertEquals(2, products.size)
            assertEquals(testProduct1.copy(), products[0])
            assertEquals(testProduct2.copy(), products[1])
            
            awaitComplete()
        }
    }

    @Test
    fun `getProducts Flow should handle empty to populated state transition`() = runTest {
        // GIVEN
        whenever(productDao.getAllProducts()).thenReturn(flowOf(emptyList()))

        // WHEN & THEN - Use Turbine for empty state verification
        repository.getProducts().test {
            // Should emit empty list
            assertEquals(emptyList<Product>(), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `syncProductsFromApi should handle API response flow correctly`() = runTest {
        // GIVEN - Simulate different API responses over time
        val firstApiResponse = listOf(testProductDto1)
        val secondApiResponse = listOf(testProductDto1, testProductDto2)
        
        // WHEN & THEN - Test sequential API calls
        whenever(productApi.getProducts()).thenReturn(firstApiResponse)
        val firstResult = repository.syncProductsFromApi()
        assertEquals(1, firstResult.size)
        assertEquals(testProduct1.copy(), firstResult[0])
        
        // Verify dao interactions for first call
        verify(productDao).clearAllProducts()
        verify(productDao).insertProducts(any())
        
        // Second API call with different response
        whenever(productApi.getProducts()).thenReturn(secondApiResponse)
        val secondResult = repository.syncProductsFromApi()
        assertEquals(2, secondResult.size)
        assertEquals(testProduct1.copy(), secondResult[0])
        assertEquals(testProduct2.copy(), secondResult[1])
        
        // Verify dao was called again
        verify(productDao, times(2)).clearAllProducts()
        verify(productDao, times(2)).insertProducts(any())
    }
}