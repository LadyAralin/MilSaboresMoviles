package com.example.milsabores.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val nombre : String,
    val descripcion : String,
    val categoria : String,
    val precio : Int,
    val imagen : String
    )