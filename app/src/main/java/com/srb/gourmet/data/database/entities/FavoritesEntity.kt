package com.srb.gourmet.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.srb.gourmet.data.network.models.Result
import com.srb.gourmet.utils.Constants.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
data class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)