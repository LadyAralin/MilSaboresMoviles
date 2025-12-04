package com.example.milsabores.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.milsabores.data.Carrito // Asegúrate de que esta clase Carrito exista
import com.example.milsabores.viewmodel.CarritoViewModel
import com.example.milsabores.viewmodel.LoginViewModel
import com.example.milsabores.viewmodel.ProductoViewModel
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    // Mantenemos la firma de NavHost, aunque solo usaremos carritoViewModel y navController
    viewModel: ProductoViewModel,
    carritoViewModel: CarritoViewModel,
    navController: NavController? = null,
    loginViewModel: LoginViewModel
) {
    // 1. OBTENER DATOS CORRECTOS: Usamos el CarritoViewModel para la lista y el total
    val itemsCarrito by carritoViewModel.itemsCarrito.collectAsState()
    val totalCarrito by carritoViewModel.totalCarrito.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver al catálogo")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (itemsCarrito.isEmpty()) {
                Text(
                    text = "El carrito está vacío. ¡Añade algo del catálogo!",
                    modifier = Modifier.padding(24.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(itemsCarrito, key = { index, item -> "${item.nombre}-$index" }) { index, item ->
                        ItemCarritoCard(item, carritoViewModel)
                    }
                }

                // Resumen del total y botón de pago
                Divider(Modifier.fillMaxWidth())
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total:",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "$${totalCarrito}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Button(
                    onClick = { /* Lógica de pago */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    enabled = totalCarrito > 0
                ) {
                    Text("Finalizar Compra")
                }
            }
        }
    }
}

// Componente para dibujar la tarjeta individual del ítem del carrito
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemCarritoCard(item: Carrito, carritoViewModel: CarritoViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Info del producto
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.nombre, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))

                // 🔹 Fila con botones - cantidad +
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { carritoViewModel.disminuirCantidad(item) }) {
                        Icon(Icons.Default.Remove, contentDescription = "Disminuir cantidad")
                    }

                    Text(
                        text = "Cantidad: ${item.cantidad}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    IconButton(onClick = { carritoViewModel.aumentarCantidad(item) }) {
                        Icon(Icons.Default.Add, contentDescription = "Aumentar cantidad")
                    }
                }

                Text(
                    text = "$${item.precio * item.cantidad}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            // Botón para eliminar todo el producto del carrito
            IconButton(
                onClick = { carritoViewModel.eliminarProducto(item) },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
