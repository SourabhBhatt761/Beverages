package com.srb.beverages.adapters.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.srb.beverages.data.database.entities.RecipesEntity
import com.srb.beverages.data.network.models.FoodRecipesResponse
import com.srb.beverages.utils.NetworkResult
import timber.log.Timber

class RecipesBinding {

    companion object {

        @BindingAdapter("readApiResponse","readDatabase",requireAll = true)
        @JvmStatic
        fun handleReadDataErrors(
            view: View,
            apiResponse: NetworkResult<FoodRecipesResponse>?,
            database: List<RecipesEntity>?
        ){
            when (view){
                is ImageView ->{
                    view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
                    Timber.i("image view")
                }
                is TextView ->{
                    view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
                    view.text = apiResponse?.message

                    Timber.i("text view")
                }
            }
        }
    }

}