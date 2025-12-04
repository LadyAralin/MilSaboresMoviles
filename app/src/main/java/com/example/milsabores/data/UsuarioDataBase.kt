package com.example.milsabores.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.milsabores.model.Producto
import com.example.milsabores.model.Usuario

@Database(entities = [Usuario::class, Producto::class], version = 3)
abstract class MilSaboresDataBase : RoomDatabase(){
    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao

    companion object {
        @Volatile
        private var INSTANCE: MilSaboresDataBase? = null

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE Usuario ADD COLUMN tipoUsuario TEXT NOT NULL DEFAULT 'Cliente'")
            }
        }

        // El DATABASE_CALLBACK para insertar el Admin se ELIMINA de aquí.

        fun getDatabase(context: Context): MilSaboresDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MilSaboresDataBase::class.java,
                    "mil_sabores_db"
                )
                    .addMigrations(MIGRATION_2_3)
                    // Se elimina .addCallback(DATABASE_CALLBACK)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}