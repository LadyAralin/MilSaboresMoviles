package com.example.milsabores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.repository.UsuarioRepository // Importación corregida
import com.example.milsabores.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// CAMBIO CRÍTICO: Ahora recibe el Repositorio
class UsuarioViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    // Estado del registro
    private val _registroState = MutableStateFlow<RegistroState>(RegistroState.Idle)
    val registroState = _registroState.asStateFlow()

    // Registrar un nuevo usuario
    fun registrarUsuario(nombre: String, correo: String, contrasena: String) {
        viewModelScope.launch {
            try {

                usuarioRepository.registrarUsuario(nombre, correo, contrasena)
                _registroState.value = RegistroState.Success
            } catch (e: Exception) {
                _registroState.value = RegistroState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    // Actualizar foto de perfil
    fun actualizarFotoPerfil(usuario: Usuario, fotoUri: String) {
        viewModelScope.launch {

            usuarioRepository.actualizarFotoPerfil(usuario, fotoUri)
        }
    }

    // Obtener usuario por ID
    suspend fun obtenerUsuarioPorId(id: Int): Usuario? {

        return usuarioRepository.obtenerUsuarioPorId(id)
    }
}

// Estados para el registro
sealed class RegistroState {
    object Idle : RegistroState()
    object Success : RegistroState()
    data class Error(val message: String) : RegistroState()
}