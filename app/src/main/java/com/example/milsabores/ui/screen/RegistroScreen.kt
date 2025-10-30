package com.example.milsabores.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.trimmedLength
import androidx.navigation.NavController
import com.example.milsabores.R
import com.example.milsabores.viewmodel.RegistroState
import com.example.milsabores.viewmodel.UsuarioViewModel

@Composable
fun RegistroScreen(navController: NavController, usuarioViewModel: UsuarioViewModel) {

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    val registroState by usuarioViewModel.registroState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.portada2),
            contentDescription = "Fondo del catálogo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.7f)
        )
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {

        Surface(
            color = Color(0xFFF4E6D4),
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Registro",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF7b3f00),
                    fontSize = 20.sp
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (nombre.isNotBlank() && correo.isNotBlank() && correo.trimmedLength() > 3 && contrasena.isNotBlank() && contrasena.trimmedLength() > 3) {
                            usuarioViewModel.registrarUsuario(nombre, correo, contrasena)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, Color(0xFF7b3f00), CircleShape),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF4E6D4),
                        contentColor = Color(0xFF7b3f00)
                    )
                ) {
                    Text("Crear cuenta")
                }

                Spacer(modifier = Modifier.height(12.dp))

                when (registroState) {
                    is RegistroState.Success -> {
                        LaunchedEffect(registroState) {
                            navController.navigate("login") {
                                popUpTo("inicio") { inclusive = false }
                            }
                        }
                    }

                    is RegistroState.Error -> Text(
                        (registroState as RegistroState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )

                    else -> {}
                }
            }
        }

    }
}
