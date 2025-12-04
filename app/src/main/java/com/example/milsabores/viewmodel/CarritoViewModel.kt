package com.example.milsabores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.Carrito
import com.example.milsabores.data.repository.CarritoRepository
import com.example.milsabores.model.Producto
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CarritoViewModel(private val repository: CarritoRepository) : ViewModel() {

    val itemsCarrito: StateFlow<List<Carrito>> = repository.carritoStream
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalCarrito: StateFlow<Int> = itemsCarrito.map { lista ->
        lista.sumOf { it.precio * it.cantidad }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    fun agregarProducto(producto: Producto) {
        viewModelScope.launch {
            val existente = repository.obtenerItemPorNombre(producto.nombre)

            if (existente != null) {
                val nuevaCantidad = existente.cantidad + 1
                repository.actualizarCantidad(existente.id, nuevaCantidad)
            } else {
                val nuevoItem = Carrito(
                    nombre = producto.nombre,
                    precio = producto.precio,
                    imagen = producto.imagen,
                    cantidad = 1
                )
                repository.agregarProducto(nuevoItem)
            }
        }
    }

    fun aumentarCantidad(item: Carrito) {
        viewModelScope.launch {
            val nuevaCantidad = item.cantidad + 1
            repository.actualizarCantidad(item.id, nuevaCantidad)
        }
    }

    fun disminuirCantidad(item: Carrito) {
        viewModelScope.launch {
            val nuevaCantidad = item.cantidad - 1
            if (nuevaCantidad > 0) {
                repository.actualizarCantidad(item.id, nuevaCantidad)
            } else {
                repository.eliminarProducto(item)
            }
        }
    }

    fun eliminarProducto(item: Carrito) {
        viewModelScope.launch {
            repository.eliminarProducto(item)
        }
    }

    fun vaciarCarrito() {
        viewModelScope.launch {
            repository.vaciarCarrito()
        }
    }

}