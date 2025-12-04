package com.example.milsabores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.repository.UsuarioRepository
import com.example.milsabores.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            usuarioRepository.inicializarAdmin()
        }
    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    private val _usuarioSesion = MutableStateFlow<Usuario?>(null)
    val usuarioSesion = _usuarioSesion.asStateFlow()

    fun login(correo: String, contrasena: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val usuario = usuarioRepository.login(correo, contrasena)
            if (usuario != null) {
                _loginState.value = LoginState.Success(usuario)
                _usuarioSesion.value = usuario
            } else {
                _loginState.value = LoginState.Error("Credenciales inválidas")
            }
        }
    }

    fun cerrarSesion() {
        _usuarioSesion.value = null
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val usuario: Usuario) : LoginState()
    data class Error(val message: String) : LoginState()
}