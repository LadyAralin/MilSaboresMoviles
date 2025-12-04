package com.example.milsabores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.repository.ProductoRepository
import com.example.milsabores.model.Producto
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminViewModel(private val repository: ProductoRepository) : ViewModel() {

    val productos: StateFlow<List<Producto>> = repository.productosStream
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun guardarProducto(producto: Producto) {
        viewModelScope.launch {
            if (producto.id == 0) {
                repository.insertarProducto(producto)
            } else {
                repository.actualizarProducto(producto)
            }
        }
    }

    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            repository.eliminarProducto(producto)
        }
    }
}