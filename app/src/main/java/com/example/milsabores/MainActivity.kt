package com.example.milsabores

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.milsabores.data.MilSaboresDataBase
import com.example.milsabores.data.repository.CarritoRepository
import com.example.milsabores.data.repository.ProductoRepository
import com.example.milsabores.data.repository.UsuarioRepository
import com.example.milsabores.ui.screen.*
import com.example.milsabores.ui.theme.MilSaboresTheme
import com.example.milsabores.viewmodel.AdminViewModel
import com.example.milsabores.viewmodel.CarritoViewModel
import com.example.milsabores.viewmodel.LoginViewModel
import com.example.milsabores.viewmodel.ProductoViewModel
import com.example.milsabores.viewmodel.UsuarioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Ajustar teclado al aparecer
        window.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setContent {
            MilSaboresTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    val context = LocalContext.current

                    // 1. Base de Datos
                    val dataBase = remember {
                        MilSaboresDataBase.getDatabase(context)
                    }

                    // 2. Repositorios
                    val usuarioRepository = remember { UsuarioRepository(dataBase.usuarioDao()) }
                    val productoRepository = remember { ProductoRepository(dataBase.productoDao()) }

                    // --- NUEVO: Repositorio del Carrito ---
                    val carritoRepository = remember { CarritoRepository(dataBase.carritoDao()) }

                    // 3. ViewModels
                    val usuarioViewModel = remember { UsuarioViewModel(usuarioRepository) }
                    val loginViewModel = remember { LoginViewModel(usuarioRepository) }
                    val productoViewModel = remember { ProductoViewModel(productoRepository) }
                    val adminViewModel = remember { AdminViewModel(productoRepository) }

                    // --- NUEVO: ViewModel del Carrito ---
                    val carritoViewModel = remember { CarritoViewModel(carritoRepository) }


                    val navController = rememberNavController()

                    // NavHost
                    NavHost(navController = navController, startDestination = "inicio") {
                        composable("inicio") {
                            InicioScreen(
                                navController = navController,
                                loginViewModel = loginViewModel
                            )
                        }
                        composable("login") {
                            LoginScreen(
                                navController = navController,
                                loginViewModel = loginViewModel
                            )
                        }
                        composable("registro") {
                            RegistroScreen(
                                navController = navController,
                                usuarioViewModel = usuarioViewModel
                            )
                        }

                        // --- AQUÍ ESTABA EL ERROR: Faltaban los parámetros nuevos ---
                        composable("catalogo") {
                            CatalogoScreen(
                                viewModel = productoViewModel,
                                carritoViewModel = carritoViewModel, // ¡Agregado!
                                loginViewModel = loginViewModel,     // ¡Agregado!
                                navController = navController
                            )
                        }

                        composable("carrito") {
                             CarritoScreen(carritoViewModel, navController)
                        }

                        // Unifiqué tus rutas de admin a una sola ("admin")
                        composable(route = "admin") {
                            AdminScreen(
                                viewModel = adminViewModel,
                                navController = navController
                            )
                        }

                        // Ruta alternativa por si algún botón viejo llama a "panelAdmin"
                        composable(route = "panelAdmin") {
                            AdminScreen(
                                viewModel = adminViewModel,
                                navController = navController
                            )
                        }

                        composable(
                            route = "perfil/{usuarioId}"
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("usuarioId")?.toInt() ?: 0
                            PerfilScreen(
                                navController = navController,
                                usuarioViewModel = usuarioViewModel,
                                usuarioId = id
                            )
                        }
                    }
                }
            }
        }
    }
}