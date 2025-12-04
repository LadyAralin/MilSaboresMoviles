package com.example.milsabores

import com.example.milsabores.data.Carrito
import com.example.milsabores.data.repository.CarritoRepository
import com.example.milsabores.model.Producto
import com.example.milsabores.viewmodel.CarritoViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CarritoViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    // MockK: repo falso
    private val repo: CarritoRepository = mockk(relaxed = true)

    // ViewModel se declara pero NO se crea aún
    private lateinit var viewModel: CarritoViewModel

    @Before
    fun setUp() {
        // Aquí ya corrió el Rule → Dispatchers.Main está configurado
        viewModel = CarritoViewModel(repo)
    }

    @Test
    fun agregarProducto_aumentaCantidad_cuandoYaExisteEnCarrito() = runTest {
        val producto = Producto(
            id = 1,
            nombre = "Alfajores",
            descripcion = "desc",
            categoria = "dulce",
            precio = 8081,
            imagen = "alfajor.png"
        )

        val itemExistente = Carrito(
            id = 10,
            nombre = "Alfajores",
            precio = 8081,
            imagen = "alfajor.png",
            cantidad = 1
        )

        coEvery { repo.obtenerItemPorNombre("Alfajores") } returns itemExistente

        viewModel.agregarProducto(producto)

        coVerify { repo.actualizarCantidad(itemExistente.id, 2) }
        coVerify(exactly = 0) { repo.agregarProducto(any()) }
    }


@Test
    fun agregarProducto_creaItemNuevo_cuandoNoExisteEnCarrito() = runTest {
        val producto = Producto(
            id = 2,
            nombre = "Brownie",
            descripcion = "desc",
            categoria = "dulce",
            precio = 5000,
            imagen = "brownie.png"
        )

        // Mock: el repo dice que no existe en el carrito
        coEvery { repo.obtenerItemPorNombre("Brownie") } returns null

        viewModel.agregarProducto(producto)

        // Debe llamar a agregarProducto con cantidad = 1
        coVerify {
            repo.agregarProducto(
                match { it.nombre == "Brownie" && it.cantidad == 1 }
            )
        }
    }

    @Test
    fun disminuirCantidad_actualizaCantidad_cuandoQuedaMayorQueCero() = runTest {
        val item = Carrito(
            id = 20,
            nombre = "Cheesecake",
            precio = 6000,
            imagen = "cheesecake.png",
            cantidad = 3
        )

        // Acción
        viewModel.disminuirCantidad(item)

        // Como cantidad era 3, ahora debe ser 2 → actualizarCantidad
        coVerify { repo.actualizarCantidad(item.id, 2) }

        // No debe eliminar el producto
        coVerify(exactly = 0) { repo.eliminarProducto(any()) }
    }

    @Test
    fun disminuirCantidad_eliminaItem_cuandoLlegaACero() = runTest {
        val item = Carrito(
            id = 21,
            nombre = "Tarta limón",
            precio = 4500,
            imagen = "tarta.png",
            cantidad = 1
        )

        // Acción
        viewModel.disminuirCantidad(item)

        // Como cantidad era 1, al disminuir debe eliminar el ítem
        coVerify { repo.eliminarProducto(item) }

        // Y no debe actualizar cantidad a un número negativo ni nada raro
        coVerify(exactly = 0) { repo.actualizarCantidad(any(), any()) }
    }


}
