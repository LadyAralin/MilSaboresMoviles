package com.example.milsabores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.milsabores.data.UsuarioDao
import com.example.milsabores.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FormularioViewModel(private val usuarioDao: UsuarioDao) : ViewModel() {
    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuario = _usuarios.asStateFlow()

    // FormularioViewModel.kt (Opción A: Asignar fijo)
    fun agregarUsuarios(nombre: String, correo: String, contrasena: String) {
        val nuevoUsuario = Usuario(
            nombre = nombre,
            correo = correo,
            contrasena = contrasena,
            tipoUsuario = "Cliente"
        )
        viewModelScope.launch {
            usuarioDao.insertarUsuario(nuevoUsuario)
            // ...
        }
    }


    fun cargarUsuarios() {
        viewModelScope.launch {
            _usuarios.value = usuarioDao.obtenerUsuarios()
        }
    }
}