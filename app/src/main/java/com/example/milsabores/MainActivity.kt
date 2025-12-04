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

        window.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setContent {
            MilSaboresTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    val context = LocalContext.current

                    val dataBase = remember {
                        MilSaboresDataBase.getDatabase(context)
                    }

                    val usuarioRepository = remember { UsuarioRepository(dataBase.usuarioDao()) }
                    val productoRepository = remember { ProductoRepository(dataBase.productoDao()) }

                    val carritoRepository = remember { CarritoRepository(dataBase.carritoDao()) }

                    val usuarioViewModel = remember { UsuarioViewModel(usuarioRepository) }
                    val loginViewModel = remember { LoginViewModel(usuarioRepository) }
                    val productoViewModel = remember { ProductoViewModel(productoRepository) }
                    val adminViewModel = remember { AdminViewModel(productoRepository) }

                    val carritoViewModel = remember { CarritoViewModel(carritoRepository) }


                    val navController = rememberNavController()

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
                                carritoViewModel = carritoViewModel,
                                loginViewModel = loginViewModel,
                                navController = navController
                            )
                        }

                        composable("carrito") {
                            CarritoScreen(
                                productoViewModel,
                                carritoViewModel,
                                navController,
                                loginViewModel
                            )
                        }

                        composable(route = "admin") {
                            AdminScreen(
                                viewModel = adminViewModel,
                                navController = navController
                            )
                        }

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