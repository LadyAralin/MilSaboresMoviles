package com.example.milsabores.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.milsabores.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch
import com.example.milsabores.R


@Composable
fun PerfilScreen(
    navController: NavController,
    usuarioViewModel: UsuarioViewModel,
    usuarioId: Int
) {
    val scope = rememberCoroutineScope()
    var usuario by remember { mutableStateOf<com.example.milsabores.model.Usuario?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Lanzador para seleccionar imagen de galería
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        usuario?.let { u ->
            uri?.let {
                usuarioViewModel.actualizarFotoPerfil(u, it.toString())
                usuario = u.copy(fotoUri = it.toString())
            }
        }
    }

    LaunchedEffect(usuarioId) {
        usuario = usuarioViewModel.obtenerUsuarioPorId(usuarioId)
    }

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

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = rememberAsyncImagePainter(
                model = selectedImageUri ?: usuario?.fotoUri
            ),
            contentDescription = "Foto de perfil",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable { launcher.launch("image/*") },
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Nombre: ${usuario?.nombre ?: ""}", style = MaterialTheme.typography.titleMedium)
        Text(text = "Correo: ${usuario?.correo ?: ""}", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF4E6D4),
                contentColor = Color(0xFF7b3f00))
        ) {
            Text("Volver")
        }
    }
}
