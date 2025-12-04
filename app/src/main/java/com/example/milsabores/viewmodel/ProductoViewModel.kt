package com.example.milsabores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ProductoViewModel(private val productoRepository: ProductoRepository) : ViewModel() {

    // 1. Estado para guardar lo que el usuario escribe
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    val productos = combine(
        productoRepository.productosStream,
        _searchText
    ) { listaProductos, texto ->
        if (texto.isBlank()) {
            listaProductos.sortedBy { it.nombre }
        } else {
            listaProductos.filter { producto ->
                producto.nombre.contains(texto, ignoreCase = true) ||
                        producto.categoria.contains(texto, ignoreCase = true)
            }.sortedBy { it.nombre }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }
}