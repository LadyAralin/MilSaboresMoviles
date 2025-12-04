package com.example.milsabores.ui.screen

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.milsabores.model.Producto
import com.example.milsabores.viewmodel.AdminViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    navController: NavController,
    viewModel: AdminViewModel
) {
    val productos by viewModel.productos.collectAsState()
    var mostrarDialogo by remember { mutableStateOf(false) }
    var productoAEditar by remember { mutableStateOf<Producto?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    productoAEditar = null // Null = Modo Crear
                    mostrarDialogo = true
                },
                containerColor = Color(0xFF7b3f00),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Gestión de Productos",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF7b3f00),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (productos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay productos. Agrega uno.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(productos) { producto ->
                        ProductoItemRow(
                            producto = producto,
                            onEdit = {
                                productoAEditar = producto
                                mostrarDialogo = true
                            },
                            onDelete = { viewModel.eliminarProducto(producto) }
                        )
                    }
                }
            }
        }
    }

    if (mostrarDialogo) {
        ProductoFormDialog(
            producto = productoAEditar,
            onDismiss = { mostrarDialogo = false },
            onConfirm = { productoNuevo ->
                viewModel.guardarProducto(productoNuevo)
                mostrarDialogo = false
            }
        )
    }
}

@Composable
fun ProductoItemRow(
    producto: Producto,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4E6D4))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Opcional: Mostrar imagen pequeña en la lista si existe
            if (producto.imagen.isNotEmpty()) {
                AsyncImage(
                    model = producto.imagen,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(end = 8.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = producto.nombre, fontWeight = FontWeight.Bold, color = Color(0xFF7b3f00))
                Text(text = "$${producto.precio}", style = MaterialTheme.typography.bodyMedium)
                Text(text = producto.categoria, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF7b3f00))
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Red)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoFormDialog(
    producto: Producto?,
    onDismiss: () -> Unit,
    onConfirm: (Producto) -> Unit
) {
    var nombre by remember { mutableStateOf(producto?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(producto?.descripcion ?: "") }
    var categoria by remember { mutableStateOf(producto?.categoria ?: "") }
    var precioStr by remember { mutableStateOf(producto?.precio?.toString() ?: "") }
    var imagenUrl by remember { mutableStateOf(producto?.imagen ?: "") }

    val context = LocalContext.current

    val launcherGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val rutaInterna = guardarImagenProductoEnApp(context, it)
            if (rutaInterna != null) {
                imagenUrl = rutaInterna
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (producto == null) "Nuevo Producto" else "Editar Producto",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF7b3f00)
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    maxLines = 3
                )
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = precioStr,
                    onValueChange = { if (it.all { char -> char.isDigit() }) precioStr = it },
                    label = { Text("Precio") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                Text("Imagen", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { launcherGaleria.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7b3f00)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Subir Foto")
                    }
                    if (imagenUrl.isNotEmpty()) {
                        AsyncImage(
                            model = imagenUrl,
                            contentDescription = "Preview",
                            modifier = Modifier
                                .size(50.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (nombre.isNotBlank() && precioStr.isNotBlank()) {
                                onConfirm(
                                    Producto(
                                        id = producto?.id ?: 0,
                                        nombre = nombre,
                                        descripcion = descripcion,
                                        categoria = categoria,
                                        precio = precioStr.toIntOrNull() ?: 0,
                                        imagen = imagenUrl
                                    )
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7b3f00))
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}

fun guardarImagenProductoEnApp(context: Context, uri: Uri): String? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val nombreArchivo = "producto_${System.currentTimeMillis()}.jpg"
        val archivoDestino = File(context.filesDir, nombreArchivo)

        val outputStream = FileOutputStream(archivoDestino)
        inputStream?.copyTo(outputStream)

        inputStream?.close()
        outputStream.close()

        archivoDestino.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}