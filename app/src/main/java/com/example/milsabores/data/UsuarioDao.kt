package com.example.milsabores.data

import androidx.room.*
import com.example.milsabores.model.Usuario

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: Usuario)

    @Update
    suspend fun actualizarUsuario(usuario: Usuario)

    @Delete
    suspend fun eliminarUsuario(usuario: Usuario)

    @Query("SELECT * FROM Usuario WHERE correo = :correo AND contrasena = :contrasena")
    suspend fun login(correo: String, contrasena: String): Usuario?

    @Query("SELECT * FROM Usuario WHERE id = :id")
    suspend fun obtenerUsuarioPorId(id: Int): Usuario?

    @Query("SELECT * FROM Usuario WHERE correo = :correo LIMIT 1")
    suspend fun obtenerUsuarioPorCorreo(correo: String): Usuario?

    @Query("SELECT * FROM Usuario")
    suspend fun obtenerUsuarios(): List<Usuario>
}