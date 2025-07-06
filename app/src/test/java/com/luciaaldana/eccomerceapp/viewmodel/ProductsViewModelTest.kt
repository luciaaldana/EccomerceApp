package com.luciaaldana.eccomerceapp.viewmodel

import app.cash.turbine.test
import com.luciaaldana.eccomerceapp.model.data.Product
import com.luciaaldana.eccomerceapp.model.repository.ProductRepository
import com.luciaaldana.eccomerceapp.testutils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ProductsViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    // Repo mockeado con Mockito-Kotlin
    private val repository: ProductRepository = mock()

    @Test
    fun `al inicializar carga productos y los filtra por query y categoría`() = runTest {
        /* ---------- Arrange ---------- */
        val sampleProducts = listOf(
            Product(
                id = "1",
                name = "Pizza Margherita",
                description = "Cheesy",
                category = "Food",
                price = 10.0,
                imageUrl = "",
                includesDrink = false
            ),
            Product(
                id = "2",
                name = "Burger",
                description = "Beef patty",
                category = "Food",
                price = 8.0,
                imageUrl = "",
                includesDrink = true
            ),
            Product(
                id = "3",
                name = "Laptop",
                description = "Gaming",
                category = "Electronics",
                price = 999.0,
                imageUrl = "",
                includesDrink = false
            )
        )

        // Cuando el VM invoque al repo queremos devolver la lista anterior
        whenever(repository.getProducts()).thenReturn(sampleProducts)

        /* ---------- Act ---------- */
        val viewModel = ProductsViewModel(repository)

        // Esperamos a que termine la corrutina del init { … }
        advanceUntilIdle()

        // Verifica que realmente se llamó al repositorio UNA vez
        verify(repository, times(1)).getProducts()

        // Aplicamos filtros
        viewModel.onSearchQueryChanged("lap")          // debería quedar solo "Laptop"
        viewModel.onCategorySelected("electronics")    // insensible a mayúsculas

        /* ---------- Assert ---------- */
        viewModel.filteredProducts.test {
            val filtered = awaitItem()                 // primer emisión tras filtros
            assertEquals(1, filtered.size)
            assertEquals("Laptop", filtered.first().name)
            cancelAndConsumeRemainingEvents()
        }
    }
}
