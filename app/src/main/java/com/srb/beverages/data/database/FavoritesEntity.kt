package com.srb.beverages.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.srb.beverages.data.network.models.Result
import com.srb.beverages.utils.Constants.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)