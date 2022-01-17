package com.srb.gourmet.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.srb.gourmet.data.network.models.FoodRecipesResponse
import com.srb.gourmet.data.network.models.Result

class RecipesTypeConverter {

    private var gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipesResponse): String {
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String): FoodRecipesResponse {
        val listType = object : TypeToken<FoodRecipesResponse>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun resultToString(result: Result): String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data: String): Result {
        val listType = object : TypeToken<Result>() {}.type
        return gson.fromJson(data, listType)
    }

}