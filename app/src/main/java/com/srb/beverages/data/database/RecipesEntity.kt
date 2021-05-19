package com.srb.beverages.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.srb.beverages.data.network.models.FoodRecipesResponse
import com.srb.beverages.utils.Constants.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipesResponse
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}