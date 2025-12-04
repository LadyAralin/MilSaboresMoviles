package com.example.milsabores.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CarritoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(item: Carrito)

    @Delete
    suspend fun eliminar(item: Carrito)

    @Query("SELECT * FROM Carrito")
    fun obtenerCarrito(): Flow<List<Carrito>>

    @Query("DELETE FROM Carrito")
    suspend fun vaciarCarrito()

    @Query("SELECT * FROM Carrito WHERE nombre = :nombre LIMIT 1")
    suspend fun obtenerItemPorNombre(nombre: String): Carrito?

    @Query("UPDATE Carrito SET cantidad = :cantidad WHERE id = :id")
    suspend fun actualizarCantidad(id: Int, cantidad: Int)
}
