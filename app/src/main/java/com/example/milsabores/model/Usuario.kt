package com.example.milsabores.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Usuario")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val fotoUri: String? = null, // <-- URI de la foto opcional
    val tipoUsuario: String
)
