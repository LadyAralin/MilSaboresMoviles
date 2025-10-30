package com.example.milsabores.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.milsabores.R
import com.example.milsabores.model.Producto
import com.example.milsabores.viewmodel.ProductoViewModel

@Composable
fun CatalogoScreen(viewModel: ProductoViewModel, navController: NavController? = null) {
    val productos by viewModel.productos.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Image(
            painter = painterResource(id = R.drawable.portada2),
            contentDescription = "Fondo del catálogo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.3f)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues()) // ← evita superposición con la barra
            .padding(8.dp), // margen interno adicional
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        // --- Catálogo (arriba) ---
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier
                .weight(1f) // ocupa todo el espacio disponible arriba
                .fillMaxWidth()
        ) {
            items(productos) { producto ->
                ProductoCard(producto = producto)
            }
        }

        // --- Botón Volver (abajo, centrado) ---
        Button(
            onClick = { navController?.popBackStack() },
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF4E6D4),
                contentColor = Color(0xFF7b3f00))
        ) {
            Text("Volver")
        }
    }
}

@Composable
fun ProductoCard(producto: Producto) {
    val context = LocalContext.current

    // Extrae el nombre del archivo de la ruta
    val imageName = producto.imagen.substringAfterLast('/').substringBeforeLast('.')

    val imageRes = remember(producto.imagen) {
        val resourceId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
        if (resourceId == 0) R.drawable.ic_launcher_background else resourceId
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            )

            Text(
                text = producto.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "$${producto.precio}",
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}
