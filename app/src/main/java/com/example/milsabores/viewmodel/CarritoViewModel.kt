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

    // 1. LISTA REACTIVA: Se actualiza sola cuando la BD cambia
    val itemsCarrito: StateFlow<List<Carrito>> = repository.carritoStream
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 2. TOTAL REACTIVO: Se recalcula solo cada vez que cambia la lista
    val totalCarrito: StateFlow<Int> = itemsCarrito.map { lista ->
        lista.sumOf { it.precio * it.cantidad }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    // 3. AGREGAR: Convierte de 'Producto' (Catálogo) a 'Carrito' (BD Local)
    fun agregarProducto(producto: Producto) {
        viewModelScope.launch {
            val nuevoItem = Carrito(
                nombre = producto.nombre,
                precio = producto.precio,
                imagen = producto.imagen,
                cantidad = 1 // Por defecto 1
            )
            repository.agregarProducto(nuevoItem)
        }
    }

    // 4. ELIMINAR
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