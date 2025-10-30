package com.example.milsabores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.UsuarioDao
import com.example.milsabores.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(private val usuarioDao: UsuarioDao) : ViewModel() {

    // Estado del registro
    private val _registroState = MutableStateFlow<RegistroState>(RegistroState.Idle)
    val registroState = _registroState.asStateFlow()

    // Registrar un nuevo usuario
    fun registrarUsuario(nombre: String, correo: String, contrasena: String) {
        viewModelScope.launch {
            try {
                val nuevoUsuario = Usuario(nombre = nombre, correo = correo, contrasena = contrasena)
                usuarioDao.insertarUsuario(nuevoUsuario)
                _registroState.value = RegistroState.Success
            } catch (e: Exception) {
                _registroState.value = RegistroState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    // Actualizar foto de perfil
    fun actualizarFotoPerfil(usuario: Usuario, fotoUri: String) {
        viewModelScope.launch {
            val actualizado = usuario.copy(fotoUri = fotoUri)
            usuarioDao.actualizarUsuario(actualizado)
        }
    }

    // Obtener usuario por ID
    suspend fun obtenerUsuarioPorId(id: Int): Usuario? {
        return usuarioDao.obtenerUsuarioPorId(id)
    }
}

// Estados para el registro
sealed class RegistroState {
    object Idle : RegistroState()
    object Success : RegistroState()
    data class Error(val message: String) : RegistroState()
}
