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

@Database(entities = [Usuario::class, Producto::class, Carrito::class], version = 4)
abstract class MilSaboresDataBase : RoomDatabase(){

    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao
    abstract fun carritoDao(): CarritoDao

    companion object {
        @Volatile
        private var INSTANCE: MilSaboresDataBase? = null

        private val applicationScope = CoroutineScope(Dispatchers.IO)

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
            }
        }

        private val MiDatabaseCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    applicationScope.launch {
                        val productoRepository = ProductoRepository(database.productoDao())
                        productoRepository.inicializarProductos()
                    }
                }
            }
        }


        fun getDatabase(context: Context): MilSaboresDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MilSaboresDataBase::class.java,
                    "mil_sabores_db_v2"
                )
                    .addMigrations(MIGRATION_3_4)
                    .addCallback(MiDatabaseCallback)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}