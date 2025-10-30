package com.example.milsabores.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.milsabores.viewmodel.LoginViewModel
import com.example.milsabores.R

@Composable
fun InicioScreen(navController: NavController, loginViewModel: LoginViewModel) {
    val usuarioSesion by loginViewModel.usuarioSesion.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.portada2),
            contentDescription = "Fondo de la pantalla de inicio",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.7f)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, Color(0xFF7b3f00), CircleShape)
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo de PMS",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                )
            }

            Surface(
                color = Color(0xFFF4E6D4),
                shape = RoundedCornerShape(size = 12.dp),
                modifier = Modifier
                    .wrapContentSize(Alignment.Center)
                    .padding(top = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Text(
                        text = "Bienvenid@ a Pasteleria Mil Sabores",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF7b3f00),
                        fontSize = 20.sp
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            if (usuarioSesion == null) {
                Button(
                    onClick = { navController.navigate("login") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF4E6D4),
                        contentColor = Color(0xFF7b3f00)
                    ),
                    modifier = Modifier
                        .border(2.dp, Color(0xFF7b3f00), CircleShape),
                ) {
                    Text("Iniciar sesión")
                }

            } else {
                Button(
                    onClick = { navController.navigate("perfil/${usuarioSesion!!.id}") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF4E6D4),
                        contentColor = Color(0xFF7b3f00)
                    ),
                    modifier = Modifier
                        .border(2.dp, Color(0xFF7b3f00), CircleShape),
                ) {
                    Text("Ver perfil")
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { loginViewModel.cerrarSesion() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF4E6D4),
                        contentColor = Color(0xFF7b3f00)
                    ),
                    modifier = Modifier
                        .border(2.dp, Color(0xFF7b3f00), CircleShape),
                ) {
                    Text("Cerrar sesión")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("catalogo") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF4E6D4),
                    contentColor = Color(0xFF7b3f00)
                ),
                modifier = Modifier
                    .border(2.dp, Color(0xFF7b3f00), CircleShape),
            ) {
                Text("Ir al catálogo")
            }
        }
    }


}
