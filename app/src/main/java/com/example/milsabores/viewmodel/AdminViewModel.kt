package com.example.milsabores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.repository.ProductoRepository
import com.example.milsabores.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminViewModel(private val repository: ProductoRepository) : ViewModel() {

    // 1. Estado del texto de búsqueda
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    // 2. Estado de la lista (Filtrada y Ordenada)
    val productos = combine(
        repository.productosStream,
        _searchText
    ) { lista, texto ->
        if (texto.isBlank()) {
            // Si no busca nada, mostramos todo ordenado por nombre
            lista.sortedBy { it.nombre }
        } else {
            // Si escribe algo, filtramos por nombre o categoría
            lista.filter {
                it.nombre.contains(texto, ignoreCase = true) ||
                        it.categoria.contains(texto, ignoreCase = true)
            }.sortedBy { it.nombre }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // 3. Función para actualizar el texto desde la vista
    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    // --- TUS FUNCIONES DE SIEMPRE ---
    fun guardarProducto(producto: Producto) {
        viewModelScope.launch {
            if (producto.id == 0) repository.insertarProducto(producto)
            else repository.actualizarProducto(producto)
        }
    }

    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch { repository.eliminarProducto(producto) }
    }
}