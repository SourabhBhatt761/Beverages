package com.srb.gourmet.data.network.models


data class FoodRecipesResponse(
    val results: List<Result>,
    val totalResults: Int?
)