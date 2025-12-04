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
import com.example.milsabores.data.repository.ProductoRepository
import com.example.milsabores.data.repository.UsuarioRepository // <-- Importación necesaria
import com.example.milsabores.ui.screen.*
import com.example.milsabores.ui.theme.MilSaboresTheme
import com.example.milsabores.viewmodel.AdminViewModel
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

                    // 1. Manejo de la Base de Datos: Usar el Singleton para aplicar migraciones
                    val dataBase = remember {
                        MilSaboresDataBase.getDatabase(context)
                    }

                    // 2. Instancia de Repositorios (requieren DAOs)
                    // ESTO RESUELVE LOS ERRORES DE DEPENDENCIA
                    val usuarioRepository = remember { UsuarioRepository(dataBase.usuarioDao()) }
                    val productoRepository = remember { ProductoRepository(dataBase.productoDao()) }

                    // 3. Instancia de ViewModels (Ahora reciben Repositorios)
                    val usuarioViewModel = remember { UsuarioViewModel(usuarioRepository) } // CORREGIDO
                    val loginViewModel = remember { LoginViewModel(usuarioRepository) }     // CORREGIDO
                    val productoViewModel = remember { ProductoViewModel(productoRepository) }
                    val adminViewModel = remember { AdminViewModel(productoRepository) }

                    val navController = rememberNavController()

                    // NavHost con todas las rutas
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
                        composable("catalogo") {
                            CatalogoScreen(
                                viewModel = productoViewModel,
                                navController = navController
                            )
                        }

                        // Ruta para el botón de Admin
                        composable(route = "panelAdmin") {
                            AdminScreen(
                                viewModel = adminViewModel,
                                navController = navController
                            )
                        }

                        // Ruta antigua
                        composable(route = "admin") {
                            AdminScreen(
                                navController = navController,
                                viewModel = adminViewModel
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