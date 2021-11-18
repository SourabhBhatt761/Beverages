package com.srb.beverages.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.srb.beverages.data.network.models.FoodJokeResponse
import com.srb.beverages.utils.Constants.FOOD_JOKE_TABLE

@Entity(tableName = FOOD_JOKE_TABLE)
data class FoodJokeEntity(
    @Embedded
    var foodJoke: FoodJokeResponse
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}