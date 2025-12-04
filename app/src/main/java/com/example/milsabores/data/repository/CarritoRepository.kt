package com.example.milsabores.data.repository

import com.example.milsabores.data.Carrito
import com.example.milsabores.data.CarritoDao
import kotlinx.coroutines.flow.Flow

class CarritoRepository(private val carritoDao: CarritoDao) {

    // 1. Exponemos el flujo de la base de datos
    val carritoStream: Flow<List<Carrito>> = carritoDao.obtenerCarrito()

    // 2. Funciones suspendidas para insertar y borrar
    suspend fun agregarProducto(item: Carrito) {
        carritoDao.insertar(item)
    }

    suspend fun eliminarProducto(item: Carrito) {
        carritoDao.eliminar(item)
    }

    suspend fun vaciarCarrito() {
        carritoDao.vaciarCarrito()
    }
}