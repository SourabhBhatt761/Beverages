package com.srb.gourmet.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.srb.gourmet.data.network.models.FoodRecipesResponse
import com.srb.gourmet.utils.Constants.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
data class RecipesEntity(
    var foodRecipe: FoodRecipesResponse
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}