package com.example.milsabores.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.milsabores.data.repository.ProductoRepository
import com.example.milsabores.model.Producto
import com.example.milsabores.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// 1. VERSIÓN MANTENIDA: version = 4
@Database(entities = [Usuario::class, Producto::class, Carrito::class], version = 4)
abstract class MilSaboresDataBase : RoomDatabase(){

    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao
    abstract fun carritoDao(): CarritoDao

    companion object {
        @Volatile
        private var INSTANCE: MilSaboresDataBase? = null

        // 2. Definimos el ámbito para ejecutar Coroutines
        private val applicationScope = CoroutineScope(Dispatchers.IO)

        // Migración 3 a 4 (vacía, confiando en fallback si hay problemas)
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Aquí irían los ALTER TABLE si hubieran cambios en el esquema
            }
        }

        // 3. Callback para inicializar datos cuando la base de datos es creada (onCreate)
        private val MiDatabaseCallback = object : RoomDatabase.Callback() {
            // Se llama la primera vez que se crea el archivo de la base de datos
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    applicationScope.launch {
                        // 🔑 CORRECCIÓN CLAVE: Creamos la instancia y llamamos al método de inicialización.
                        val productoRepository = ProductoRepository(database.productoDao())
                        productoRepository.inicializarProductos() // <--- ¡Esto faltaba!
                    }
                }
            }
        }


        fun getDatabase(context: Context): MilSaboresDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MilSaboresDataBase::class.java,
                    "mil_sabores_db_v2" // Nombre cambiado para forzar la creación
                )
                    .addMigrations(MIGRATION_3_4)
                    // Añadimos el Callback aquí para que ejecute onCreate()
                    .addCallback(MiDatabaseCallback)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}