package com.example.milsabores.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.milsabores.viewmodel.LoginState
import com.example.milsabores.viewmodel.LoginViewModel
import com.example.milsabores.R


@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    val loginState by loginViewModel.loginState.collectAsState()
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier.fillMaxSize()
    ){
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
            color = Color(0xFFF4E6D4), // color de fondo de la caja
            shape = RoundedCornerShape(12.dp), // esquinas redondeadas
            shadowElevation = 4.dp, // elevación opcional
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp) // padding interno de la caja
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF7b3f00),
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { focusManager.clearFocus(); loginViewModel.login(correo, contrasena) },
                    modifier = Modifier.fillMaxWidth()
                        .border(2.dp, Color(0xFF7b3f00), CircleShape),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF4E6D4),
                        contentColor = Color(0xFF7b3f00)),

                ) {
                    Text("Iniciar sesión")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { navController.navigate("registro") },
                    modifier = Modifier.fillMaxWidth()
                        .border(2.dp, Color(0xFF7b3f00), CircleShape),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF4E6D4),
                        contentColor = Color(0xFF7b3f00))
                ) {
                    Text("Registrarse")
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (loginState) {
                    is LoginState.Loading -> CircularProgressIndicator()
                    is LoginState.Success -> {
                        val usuario = (loginState as LoginState.Success).usuario
                        Text("Bienvenido, ${usuario.nombre}!", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { navController.navigate("perfil/${usuario.id}") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ver perfil")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LaunchedEffect(Unit) {
                            navController.navigate("catalogo") { popUpTo("inicio") { inclusive = false } }
                        }
                    }
                    is LoginState.Error -> Text(
                        (loginState as LoginState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                    else -> {}
                }
            }
        }
    }

}
