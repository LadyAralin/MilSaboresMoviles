package com.example.milsabores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.repository.UsuarioRepository // Importación corregida
import com.example.milsabores.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    private val _registroState = MutableStateFlow<RegistroState>(RegistroState.Idle)
    val registroState = _registroState.asStateFlow()

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

    fun actualizarFotoPerfil(usuario: Usuario, fotoUri: String) {
        viewModelScope.launch {

            usuarioRepository.actualizarFotoPerfil(usuario, fotoUri)
        }
    }

    suspend fun obtenerUsuarioPorId(id: Int): Usuario? {

        return usuarioRepository.obtenerUsuarioPorId(id)
    }
}

sealed class RegistroState {
    object Idle : RegistroState()
    object Success : RegistroState()
    data class Error(val message: String) : RegistroState()
}