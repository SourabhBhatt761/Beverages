package com.srb.beverages.data.network.models


data class FoodRecipesResponse(
    val results: List<Result>?,
    val totalResults: Int?
)