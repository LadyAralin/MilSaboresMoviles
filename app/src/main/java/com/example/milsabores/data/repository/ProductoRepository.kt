package com.example.milsabores.data.repository

import com.example.milsabores.data.ProductoDao
import com.example.milsabores.data.conexion.RetrofitClient
import com.example.milsabores.model.Producto
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val productoDao: ProductoDao) {
    val productosStream: Flow<List<Producto>> = productoDao.obtenerProductosFlow()

    suspend fun insertarProducto(producto: Producto) = productoDao.insertar(producto)

    suspend fun actualizarProducto(producto: Producto) = productoDao.actualizar(producto)

    suspend fun eliminarProducto(producto: Producto) = productoDao.eliminar(producto)

    suspend fun obtenerProductos(): List<Producto> {
        return productoDao.obtenerProductos()
    }

    private suspend fun obtenerProductosDeApi(): List<Producto> {
        return try {
            val respuesta = RetrofitClient.api.obtenerPostres()
            respuesta.listaPostres.map { meal ->
                Producto(
                    id = 0,
                    nombre = meal.nombre,
                    descripcion = "Delicia internacional importada de nuestro catálogo online.",
                    categoria = "Internacional",
                    precio = (5000..12000).random(),
                    imagen = meal.imagenUrl
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    suspend fun inicializarProductos() {
        if (productoDao.obtenerProductos().isEmpty()) {
            productoDao.insertar(Producto(nombre = "Torta Cuadrada de Chocolate", descripcion = "Deliciosa torta de chocolate con capas de ganache y un toque de avellanas. Personalizable con mensajes especiales.", categoria = "Tortas Cuadradas", precio = 45000, imagen = "cuadrada_chocolate.jpg"))
            productoDao.insertar(Producto(nombre = "Torta Cuadrada de Frutas", descripcion = "Una mezcla de frutas frescas y crema chantilly sobre un suave bizcocho de vainilla, ideal para celebraciones.", categoria = "Tortas Cuadradas", precio = 50000, imagen = "cuadrada_frutas.jpg"))
            productoDao.insertar(Producto(nombre = "Torta Circular de Vainilla", descripcion = "Bizcocho de vainilla clásico relleno con crema pastelera y cubierto con un glaseado dulce, perfecto para cualquier ocasión.", categoria = "Tortas Circulares", precio = 40000, imagen = "circular_vainilla.png"))
            productoDao.insertar(Producto(nombre = "Torta Circular de Manjar", descripcion = "Torta tradicional chilena con manjar y nueces, un deleite para los amantes de los sabores dulces y clásicos.", categoria = "Tortas Circulares", precio = 42000, imagen = "circular_manjar.jpg"))
            productoDao.insertar(Producto(nombre = "Torta Especial de Cumpleaños", descripcion = "Diseñada especialmente para celebraciones, personalizable con decoraciones y mensajes únicos.", categoria = "Tortas Especiales", precio = 55000, imagen = "especial_cumpleanos.png"))
            productoDao.insertar(Producto(nombre = "Torta Especial de Boda", descripcion = "Elegante y deliciosa, esta torta está diseñada para ser el centro de atención en cualquier boda.", categoria = "Tortas Especiales", precio = 60000, imagen = "especial_boda.png"))
            productoDao.insertar(Producto(nombre = "Mousse de Chocolate", descripcion = "Postre individual cremoso y suave, hecho con chocolate de alta calidad, ideal para los amantes del chocolate.", categoria = "Postres Individuales", precio = 5000, imagen = "mousse_chocolate.jpg"))
            productoDao.insertar(Producto(nombre = "Tiramisú Clásico", descripcion = "Un postre italiano individual con capas de café, mascarpone y cacao, perfecto para finalizar cualquier comida.", categoria = "Postres Individuales", precio = 5500, imagen = "tiramisu_clasico.jpg"))
            productoDao.insertar(Producto(nombre = "Empanada de Manzana", descripcion = "Pastelería tradicional rellena de manzanas especiadas, perfecta para un dulce desayuno o merienda.", categoria = "Pastelería Tradicional", precio = 3000, imagen = "empanada_manzana.webp"))
            productoDao.insertar(Producto(nombre = "Tarta de Santiago", descripcion = "Tradicional tarta española hecha con almendras, azúcar y huevos, una delicia para los amantes de los postres clásicos.", categoria = "Pastelería Tradicional", precio = 6000, imagen = "tarta_de_santiago.jpg"))
            productoDao.insertar(Producto(nombre = "Torta Sin Azúcar de Naranja", descripcion = "Torta ligera y deliciosa, endulzada naturalmente, ideal para quienes buscan opciones más saludables.", categoria = "Productos sin Azúcar", precio = 48000, imagen = "naranja_sin_azucar.jpg"))
            productoDao.insertar(Producto(nombre = "Cheesecake Sin Azúcar", descripcion = "Suave y cremoso, este cheesecake es una opción perfecta para disfrutar sin culpa.", categoria = "Productos sin Azúcar", precio = 47000, imagen = "cheesecake.jpg"))
            productoDao.insertar(Producto(nombre = "Brownie Sin Gluten", descripcion = "Rico y denso, este brownie es perfecto para quienes necesitan evitar el gluten sin sacrificar el sabor.", categoria = "Productos Sin Gluten", precio = 4000, imagen = "brownie_sin_gluten.jpg"))
            productoDao.insertar(Producto(nombre = "Pan Sin Gluten", descripcion = "Suave y esponjoso, ideal para sándwiches o para acompañar cualquier comida.", categoria = "Productos Sin Gluten", precio = 3500, imagen = "pan_sin_gluten.jpg"))
            productoDao.insertar(Producto(nombre = "Torta Vegana de Chocolate", descripcion = "Torta de chocolate húmeda y deliciosa, hecha sin productos de origen animal, perfecta para veganos.", categoria = "Productos Veganos", precio = 50000, imagen = "vegana_chocolate.jpg"))
            productoDao.insertar(Producto(nombre = "Galletas Veganas de Avena", descripcion = "Crujientes y sabrosas, estas galletas son una excelente opción para un snack saludable y vegano.", categoria = "Productos Veganos", precio = 4500, imagen = "galleta_vegana_avena.jpg"))

            val productosApi = obtenerProductosDeApi()
            productosApi.forEach { productoApi ->
                productoDao.insertar(productoApi)
            }
        }
    }
}