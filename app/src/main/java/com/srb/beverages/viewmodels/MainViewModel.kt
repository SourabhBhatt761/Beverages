package com.srb.beverages.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.srb.beverages.data.database.RecipesEntity
import com.srb.beverages.data.Repository
import com.srb.beverages.data.network.models.FoodRecipesResponse
import com.srb.beverages.utils.Constants.API_KEY
import com.srb.beverages.utils.Constants.DEFAULT_RECIPES_NUMBER
import com.srb.beverages.utils.Constants.QUERY_ADD_RECIPE_INFORMATION
import com.srb.beverages.utils.Constants.QUERY_API_KEY
import com.srb.beverages.utils.Constants.QUERY_DIET
import com.srb.beverages.utils.Constants.QUERY_FILL_INGREDIENTS
import com.srb.beverages.utils.Constants.QUERY_NUMBER
import com.srb.beverages.utils.Constants.QUERY_TYPE
import com.srb.beverages.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository : Repository,
    application: Application
) : AndroidViewModel(application) {

    /** ROOM DATABASE **/

    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }



    /** RETROFIT */

    private val _recipes = MutableLiveData<NetworkResult<FoodRecipesResponse>>()
    val recipes : LiveData<NetworkResult<FoodRecipesResponse>> = _recipes

    fun getRecipes(queries : Map<String,String>) = viewModelScope.launch(Dispatchers.IO) {
        if (hasInternetConnection()){
            try {
                val response = repository.remote.getRecipes(queries)
                _recipes.postValue(handleRecipesResponse(response))

            }catch (e : Exception){
                Timber.e(e)
            }
        }else{
            _recipes.postValue(NetworkResult.Error("No internet Connection"))
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipesResponse) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = "snack"
        queries[QUERY_DIET] = "vegan"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }



    private fun handleRecipesResponse(response: Response<FoodRecipesResponse>): NetworkResult<FoodRecipesResponse>{
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()!!
                offlineCacheRecipes(foodRecipes)
                return NetworkResult.Success(foodRecipes)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }





    private fun hasInternetConnection(): Boolean {

        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}