package com.luciaaldana.eccomerceapp.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luciaaldana.eccomerceapp.core.model.Product
import com.luciaaldana.eccomerceapp.domain.product.ProductRepository
import com.luciaaldana.eccomerceapp.domain.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    val allProducts: StateFlow<List<Product>> = productRepository.getProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _isScreenLoading = MutableStateFlow(true)
    val isScreenLoading: StateFlow<Boolean> = _isScreenLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    var searchQuery = MutableStateFlow(value = "")
        private set

    var selectedCategory = MutableStateFlow<String?>(value = null)
        private set

    val filteredProducts: StateFlow<List<Product>> = combine(
        allProducts, searchQuery, selectedCategory
    ) { products, query, category ->
        products.filter { product ->
            val matchesQuery = query.isBlank() ||
                    product.name.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true)
            val matchesCategory = category == null ||
                    product.category.equals(category, ignoreCase = true)
            matchesQuery && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        simulateScreenLoading()
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            if (!productRepository.hasLocalProducts()) {
                refreshProducts()
            }
        }
    }
    
    private fun simulateScreenLoading() {
        viewModelScope.launch {
            delay(800)
            _isScreenLoading.value = false
        }
    }

    fun refreshProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                productRepository.refreshProducts()
            } catch (e: Exception) {
                _error.value = "Error al cargar productos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun retryLoadProducts() {
        refreshProducts()
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
    }

    fun onCategorySelected(category: String?) {
        selectedCategory.value = category
    }
    
    fun isUserLoggedIn(): Boolean {
        return authRepository.getCurrentUser() != null
    }
    
    fun getCurrentUser() = authRepository.getCurrentUser()
}