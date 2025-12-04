package com.example.milsabores.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.milsabores.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    // Tus métodos existentes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(producto: Producto)

    @Query("SELECT * FROM Producto ORDER BY id DESC")
    fun obtenerProductosFlow(): Flow<List<Producto>>

    @Update
    suspend fun actualizar(producto: Producto)

    @Delete
    suspend fun eliminar(producto: Producto)

    @Query("SELECT * FROM Producto")
    suspend fun obtenerProductos(): List<Producto>
}