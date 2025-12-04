package com.example.milsabores.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.milsabores.R
import com.example.milsabores.model.Producto
import com.example.milsabores.viewmodel.CarritoViewModel
import com.example.milsabores.viewmodel.LoginViewModel
import com.example.milsabores.viewmodel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    viewModel: ProductoViewModel,
    carritoViewModel: CarritoViewModel, // Agregado del compañero
    loginViewModel: LoginViewModel,     // Agregado del compañero
    navController: NavController? = null
) {
    val productos by viewModel.productos.collectAsState()
    val searchText by viewModel.searchText.collectAsState() // Tuyo
    val usuario by loginViewModel.usuarioSesion.collectAsState()

    Scaffold(
        floatingActionButton = {
            if (usuario != null) {
                FloatingActionButton(
                    onClick = { navController?.navigate("carrito") },
                    containerColor = Color(0xFF7b3f00),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito de compras")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { viewModel.onSearchTextChange(it) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    placeholder = { Text("Buscar pastel...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.8f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.8f)
                    )
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(productos) { producto ->
                        ProductoCard(
                            producto = producto,
                            carritoViewModel = carritoViewModel,
                            isLoggedIn = usuario != null
                        )
                    }
                }

                Button(
                    onClick = { navController?.popBackStack() },
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF4E6D4),
                        contentColor = Color(0xFF7b3f00)
                    )
                ) {
                    Text("Volver")
                }
            }
        }
    }
}

@Composable
fun ProductoCard(
    producto: Producto,
    carritoViewModel: CarritoViewModel,
    isLoggedIn: Boolean
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            if (producto.imagen.startsWith("http") || producto.imagen.startsWith("/")) {
                AsyncImage(
                    model = producto.imagen,
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.ic_launcher_background)
                )
            } else {
                val context = LocalContext.current
                val imageName = if (producto.imagen.isNotEmpty()) {
                    producto.imagen.substringAfterLast('/').substringBeforeLast('.')
                } else {
                    "placeholder"
                }
                val imageRes = remember(producto.imagen) {
                    val resourceId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
                    if (resourceId == 0) R.drawable.ic_launcher_background else resourceId
                }
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = producto.nombre,
                    modifier = Modifier.height(150.dp).fillMaxWidth(),
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
                enabled = isLoggedIn, // Se bloquea si no hay sesión
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7b3f00)
                )
            ) {
                Text(if (isLoggedIn) "Agregar al carrito" else "Inicia sesión")
            }
        }
    }
}