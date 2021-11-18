package com.srb.beverages.adapters.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.google.android.material.card.MaterialCardView
import com.srb.beverages.R
import com.srb.beverages.data.network.models.Result
import com.srb.beverages.ui.fragments.recipes.RecipesFragmentDirections
import org.jsoup.Jsoup
import timber.log.Timber

class RecipesRowBinding {

companion object{

    @BindingAdapter("loadImageFromUrl")
    @JvmStatic
    fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
        imageView.load(imageUrl) {
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
    }

    @BindingAdapter("setNumberOfLikes")
    @JvmStatic
    fun setNumberOfLikes(textView: TextView , likes : Int){
        textView.text = likes.toString()
    }

    @BindingAdapter("setNumberOfMinutes")
    @JvmStatic
    fun setNumberOfMinutes(textView: TextView , min : Int){
        textView.text = min.toString()
    }

    @BindingAdapter("applyVeganColor")
    @JvmStatic
    fun applyVeganColor(view: View, vegan: Boolean) {
        if (vegan) {
            when (view) {
                is TextView -> {
                    view.setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.green
                        )
                    )
                }
                is ImageView -> {
                    view.setColorFilter(
                        ContextCompat.getColor(
                            view.context,
                            R.color.green
                        )
                    )
                }
            }
        }
    }

    @BindingAdapter("parseHtml")
    @JvmStatic
    fun parseHtml(textView: TextView, description: String?){
        if(description != null) {
            val desc = Jsoup.parse(description).text()
            textView.text = desc
        }
    }

    @BindingAdapter("onRecipeClickListener")
    @JvmStatic
    fun onRecipeClickListener(recipeRowLayout: MaterialCardView, result: Result) {
        recipeRowLayout.setOnClickListener {
            try {
                val action =
                    RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
                recipeRowLayout.findNavController().navigate(action)
            } catch (e: Exception) {
                Timber.d(e.toString())
            }
        }
    }

}
}