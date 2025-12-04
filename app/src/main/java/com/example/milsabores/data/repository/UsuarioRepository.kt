package com.example.milsabores.data.repository

import com.example.milsabores.data.UsuarioDao
import com.example.milsabores.model.Usuario

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    suspend fun inicializarAdmin() {
        val adminExistente = usuarioDao.obtenerUsuarioPorCorreo("admin@mail.com")

        if (adminExistente == null) {
            val adminUsuario = Usuario(
                nombre = "Admin",
                correo = "admin@mail.com",
                contrasena = "admin123",
                fotoUri = null,
                tipoUsuario = "Admin"
            )
            usuarioDao.insertarUsuario(adminUsuario)
        }
    }

    suspend fun registrarUsuario(nombre: String, correo: String, contrasena: String) {
        val nuevoUsuario = Usuario(
            nombre = nombre,
            correo = correo,
            contrasena = contrasena,
            fotoUri = null,
            tipoUsuario = "Cliente"
        )
        usuarioDao.insertarUsuario(nuevoUsuario)
    }

    suspend fun login(correo: String, contrasena: String): Usuario? {
        return usuarioDao.login(correo, contrasena)
    }

    suspend fun actualizarFotoPerfil(usuario: Usuario, fotoUri: String) {
        val actualizado = usuario.copy(fotoUri = fotoUri)
        usuarioDao.actualizarUsuario(actualizado)
    }

    suspend fun obtenerUsuarioPorId(id: Int): Usuario? {
        return usuarioDao.obtenerUsuarioPorId(id)
    }
}