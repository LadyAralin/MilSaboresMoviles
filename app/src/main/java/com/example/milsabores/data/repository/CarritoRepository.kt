package com.example.milsabores.data.repository

import com.example.milsabores.data.Carrito
import com.example.milsabores.data.CarritoDao
import kotlinx.coroutines.flow.Flow

class CarritoRepository(private val carritoDao: CarritoDao) {
    val carritoStream: Flow<List<Carrito>> = carritoDao.obtenerCarrito()

    suspend fun agregarProducto(item: Carrito) {
        carritoDao.insertar(item)
    }

    suspend fun eliminarProducto(item: Carrito) {
        carritoDao.eliminar(item)
    }

    suspend fun vaciarCarrito() {
        carritoDao.vaciarCarrito()
    }

    suspend fun obtenerItemPorNombre(nombre: String): Carrito? =
        carritoDao.obtenerItemPorNombre(nombre)

    suspend fun actualizarCantidad(id: Int, cantidad: Int) =
        carritoDao.actualizarCantidad(id, cantidad)
}
