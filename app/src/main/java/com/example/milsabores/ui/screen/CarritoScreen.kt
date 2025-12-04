package com.example.milsabores.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.milsabores.R
import com.example.milsabores.model.Producto
import com.example.milsabores.viewmodel.CarritoViewModel
import com.example.milsabores.viewmodel.LoginViewModel
import com.example.milsabores.viewmodel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    viewModel: ProductoViewModel,
    carritoViewModel: CarritoViewModel,
    navController: NavController? = null,
    loginViewModel: LoginViewModel
) {
    val productos by viewModel.productos.collectAsState()
    val usuario by loginViewModel.usuarioSesion.collectAsState()

    Scaffold(
        floatingActionButton = {
            if (usuario != null) {
                FloatingActionButton(onClick = { navController?.navigate("carrito") }) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito de compras")
                }
            }
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(it)
        ) {
            Image(
                painter = painterResource(id = R.drawable.portada2),
                contentDescription = "Fondo del catálogo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.3f)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.safeDrawing.asPaddingValues())
                    .padding(8.dp), // margen interno adicional
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(productos) { producto ->
                        ProductCard(
                            producto = producto,
                            carritoViewModel = carritoViewModel,
                            isLoggedIn = usuario != null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(producto: Producto, carritoViewModel: CarritoViewModel, isLoggedIn: Boolean) {
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
                val context = LocalContext.current

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
                    if (resourceId == 0) R.drawable.ic_launcher_background else resourceId
                }

                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "$${producto.precio}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Button(
                onClick = { carritoViewModel.agregarProducto(producto) },
                enabled = isLoggedIn
            ) {
                Text("Agregar al carrito")
            }
        }
    }
}