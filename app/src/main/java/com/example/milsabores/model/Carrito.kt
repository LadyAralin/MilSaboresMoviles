package com.example.milsabores.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Carrito")
data class Carrito(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Guardamos los datos del producto directamente aquí
    val nombre: String,
    val precio: Int,
    val imagen: String,
    val cantidad: Int = 1
)