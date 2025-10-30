package com.example.milsabores.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.milsabores.model.Producto
import com.example.milsabores.model.Usuario

@Database(entities = [Usuario::class, Producto::class], version = 2)
abstract class MilSaboresDataBase : RoomDatabase(){
    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao
}