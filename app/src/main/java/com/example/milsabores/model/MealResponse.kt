package com.example.milsabores.model

import com.google.gson.annotations.SerializedName

data class MealResponse(
    @SerializedName("meals") val listaPostres: List<MealDto>
)

data class MealDto(
    @SerializedName("idMeal") val id: String,
    @SerializedName("strMeal") val nombre: String,
    @SerializedName("strMealThumb") val imagenUrl: String
)