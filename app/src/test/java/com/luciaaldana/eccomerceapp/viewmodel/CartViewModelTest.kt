package com.luciaaldana.eccomerceapp.viewmodel

import app.cash.turbine.test
import com.luciaaldana.eccomerceapp.model.data.CartItem
import com.luciaaldana.eccomerceapp.model.data.Product
import com.luciaaldana.eccomerceapp.model.repository.CartItemRepository
import com.luciaaldana.eccomerceapp.testutils.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var cartItemRepository: CartItemRepository
    private lateinit var cartViewModel: CartViewModel

    private val sampleProduct1 = Product(
        id = "1",
        name = "Hamburguesa",
        description = "Deliciosa hamburguesa",
        price = 15.99,
        imageUrl = "https://example.com/burger.jpg",
        category = "Comida",
        includesDrink = false
    )

    private val sampleProduct2 = Product(
        id = "2",
        name = "Pizza",
        description = "Pizza grande",
        price = 25.50,
        imageUrl = "https://example.com/pizza.jpg",
        category = "Comida",
        includesDrink = true
    )

    private val sampleCartItem1 = CartItem(product = sampleProduct1, quantity = 2)
    private val sampleCartItem2 = CartItem(product = sampleProduct2, quantity = 1)

    @Before
    fun setup() {
        cartItemRepository = mockk(relaxed = true)
        every { cartItemRepository.getCartItems() } returns emptyList()
        cartViewModel = CartViewModel(cartItemRepository)
    }

    @Test
    fun `init should load cart items from repository`() = runTest {
        // Given
        val expectedItems = listOf(sampleCartItem1, sampleCartItem2)
        every { cartItemRepository.getCartItems() } returns expectedItems

        // When
        val viewModel = CartViewModel(cartItemRepository)

        // Then
        viewModel.cartItems.test {
            assertEquals(expectedItems, awaitItem())
        }
    }

    @Test
    fun `cartItems should be empty initially when repository is empty`() = runTest {
        cartViewModel.cartItems.test {
            assertEquals(emptyList<CartItem>(), awaitItem())
        }
    }

    @Test
    fun `totalAmount should be calculated correctly with multiple items`() = runTest {
        // Given
        val cartItems = listOf(sampleCartItem1, sampleCartItem2)
        every { cartItemRepository.getCartItems() } returns cartItems
        val expectedTotal = (15.99 * 2) + (25.50 * 1) // 57.48

        // When
        val viewModel = CartViewModel(cartItemRepository)

        // Then
        viewModel.totalAmount.test {
            assertEquals(expectedTotal, awaitItem(), 0.01)
        }
    }

    @Test
    fun `totalAmount should be zero when cart is empty`() = runTest {
        cartViewModel.totalAmount.test {
            assertEquals(0.0, awaitItem(), 0.01)
        }
    }

    @Test
    fun `add should call repository addProduct and reload cart`() = runTest {
        // Given
        val updatedItems = listOf(sampleCartItem1)
        
        // Reset mock to return initial empty, then updated items after add
        every { cartItemRepository.getCartItems() } returnsMany listOf(emptyList(), updatedItems)

        // Create fresh ViewModel for this test
        val testViewModel = CartViewModel(cartItemRepository)
        advanceUntilIdle()

        // When
        testViewModel.add(sampleProduct1)
        advanceUntilIdle()

        // Then
        verify { cartItemRepository.addProduct(sampleProduct1) }
        assertEquals(updatedItems, testViewModel.cartItems.value)
    }

    @Test
    fun `remove should call repository removeProduct and reload cart`() = runTest {
        // Given
        // Setup mock to return initial cart with item, then empty after remove
        every { cartItemRepository.getCartItems() } returnsMany listOf(listOf(sampleCartItem1), emptyList())

        // Create fresh ViewModel for this test
        val testViewModel = CartViewModel(cartItemRepository)
        advanceUntilIdle()

        // When
        testViewModel.remove(sampleProduct1)
        advanceUntilIdle()

        // Then
        verify { cartItemRepository.removeProduct(sampleProduct1) }
        assertEquals(emptyList<CartItem>(), testViewModel.cartItems.value)
    }

    @Test
    fun `updateQuantity should call repository updateQuantity and reload cart`() = runTest {
        // Given
        val newQuantity = 5
        val updatedCartItem = CartItem(product = sampleProduct1, quantity = newQuantity)
        
        // Setup mock to return initial cart with item, then updated item after updateQuantity
        every { cartItemRepository.getCartItems() } returnsMany listOf(listOf(sampleCartItem1), listOf(updatedCartItem))

        // Create fresh ViewModel for this test
        val testViewModel = CartViewModel(cartItemRepository)
        advanceUntilIdle()

        // When
        testViewModel.updateQuantity(sampleProduct1, newQuantity)
        advanceUntilIdle()

        // Then
        verify { cartItemRepository.updateQuantity(sampleProduct1, newQuantity) }
        assertEquals(listOf(updatedCartItem), testViewModel.cartItems.value)
    }

    @Test
    fun `clearCart should call repository clearCart and reload cart`() = runTest {
        // Given
        // Setup mock to return initial cart with items, then empty after clearCart
        every { cartItemRepository.getCartItems() } returnsMany listOf(listOf(sampleCartItem1, sampleCartItem2), emptyList())

        // Create fresh ViewModel for this test
        val testViewModel = CartViewModel(cartItemRepository)
        advanceUntilIdle()

        // When
        testViewModel.clearCart()
        advanceUntilIdle()

        // Then
        verify { cartItemRepository.clearCart() }
        assertEquals(emptyList<CartItem>(), testViewModel.cartItems.value)
    }

    @Test
    fun `totalAmount should update when cart items change`() = runTest {
        // Given
        // Setup mock to return initial empty cart, then cart with item after add
        every { cartItemRepository.getCartItems() } returnsMany listOf(emptyList(), listOf(sampleCartItem1))

        // Create fresh ViewModel for this test
        val testViewModel = CartViewModel(cartItemRepository)
        advanceUntilIdle()

        // When & Then
        testViewModel.totalAmount.test {
            // First emission should be 0.0 (empty cart)
            assertEquals(0.0, awaitItem(), 0.01)
            
            // Perform action
            testViewModel.add(sampleProduct1)
            
            // Should emit updated total
            val expectedTotal = 15.99 * 2 // price * quantity
            assertEquals(expectedTotal, awaitItem(), 0.01)
        }
    }

    @Test
    fun `multiple operations should maintain state consistency`() = runTest {
        // Given
        // Setup mock to return sequence: empty -> [item1] -> [item1,item2] -> [item2]
        every { cartItemRepository.getCartItems() } returnsMany listOf(
            emptyList(),
            listOf(sampleCartItem1),
            listOf(sampleCartItem1, sampleCartItem2),
            listOf(sampleCartItem2)
        )

        // Create fresh ViewModel for this test
        val testViewModel = CartViewModel(cartItemRepository)
        advanceUntilIdle()

        // When
        testViewModel.add(sampleProduct1)
        advanceUntilIdle()
        assertEquals(listOf(sampleCartItem1), testViewModel.cartItems.value)

        testViewModel.add(sampleProduct2)
        advanceUntilIdle()
        assertEquals(listOf(sampleCartItem1, sampleCartItem2), testViewModel.cartItems.value)

        testViewModel.remove(sampleProduct1)
        advanceUntilIdle()
        assertEquals(listOf(sampleCartItem2), testViewModel.cartItems.value)

        // Then
        verify { cartItemRepository.addProduct(sampleProduct1) }
        verify { cartItemRepository.addProduct(sampleProduct2) }
        verify { cartItemRepository.removeProduct(sampleProduct1) }
    }
}