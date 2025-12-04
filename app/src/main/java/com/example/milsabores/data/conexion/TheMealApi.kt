package com.example.milsabores.data.conexion

import com.example.milsabores.model.MealResponse
import retrofit2.http.GET

interface TheMealApi {
    @GET("filter.php?c=Dessert")
    suspend fun obtenerPostres(): MealResponse
}