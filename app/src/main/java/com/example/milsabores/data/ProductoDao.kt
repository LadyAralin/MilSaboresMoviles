package com.example.milsabores.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.milsabores.model.Producto

@Dao
interface ProductoDao {
    @Insert
    suspend fun insertar(producto: Producto)

    @Query("SELECT * FROM Producto")
    suspend fun obtenerProductos(): List<Producto>
}