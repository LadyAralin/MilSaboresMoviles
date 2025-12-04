package com.example.milsabores.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
            .padding(WindowInsets.safeDrawing.asPaddingValues())
            .padding(8.dp), // margen interno adicional
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        // --- Catálogo (arriba) ---
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(productos) { producto ->
                ProductoCard(producto = producto)
            }
        }

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
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            if (producto.imagen.startsWith("http")) {
                coil.compose.AsyncImage(
                    model = producto.imagen,
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            } else {
                val context = androidx.compose.ui.platform.LocalContext.current

                val imageName = if (producto.imagen.isNotEmpty()) {
                    producto.imagen.substringAfterLast('/').substringBeforeLast('.')
                } else {
                    "placeholder"
                }

                val imageRes = remember(producto.imagen) {
                    val resourceId = context.resources.getIdentifier(
                        imageName,
                        "drawable",
                        context.packageName
                    )
                    if (resourceId == 0) com.example.milsabores.R.drawable.ic_launcher_background else resourceId
                }

                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = imageRes),
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }

            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleMedium, // Ajusta el estilo si prefieres
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "$${producto.precio}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}