package com.example.milsabores.data

import androidx.room.*
import com.example.milsabores.model.Usuario

@Dao
interface UsuarioDao {
    @Insert
    suspend fun insertarUsuario(usuario: Usuario)

    @Query("SELECT * FROM Usuario")
    suspend fun obtenerUsuarios(): List<Usuario>

    @Query("SELECT * FROM Usuario WHERE id = :id")
    suspend fun obtenerUsuarioPorId(id: Int): Usuario?

    @Update
    suspend fun actualizarUsuario(usuario: Usuario)

    @Query("SELECT * FROM Usuario WHERE correo = :correo AND contrasena = :contrasena LIMIT 1")
    suspend fun login(correo: String, contrasena: String): Usuario?
}
