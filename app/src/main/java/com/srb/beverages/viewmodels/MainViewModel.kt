package com.srb.beverages.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.srb.beverages.data.database.RecipesEntity
import com.srb.beverages.data.Repository
import com.srb.beverages.data.database.FavoritesEntity
import com.srb.beverages.data.database.FoodJokeEntity
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

    //asLiveData is part of Flow, when we want to collect the data from db which returns the value as liveData
    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
//    val readFavoriteRecipes: LiveData<List<FavoritesEntity>> = repository.local.readFavoriteRecipes().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

//    fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity) =
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.local.insertFavoriteRecipes(favoritesEntity)
//        }
//
//    private fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) =
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.local.insertFoodJoke(foodJokeEntity)
//        }
//
//    fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity) =
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.local.deleteFavoriteRecipe(favoritesEntity)
//        }
//
//    fun deleteAllFavoriteRecipes() =
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.local.deleteAllFavoriteRecipes()
//        }



    /** RETROFIT */

    private val _recipes = MutableLiveData<NetworkResult<FoodRecipesResponse>>()
    val apiRecipes : LiveData<NetworkResult<FoodRecipesResponse>> = _recipes
    private val _searchRecipes = MutableLiveData<NetworkResult<FoodRecipesResponse>>()
    val searchRecipes : LiveData<NetworkResult<FoodRecipesResponse>> = _searchRecipes


    fun getRecipes(queries : Map<String,String>) = viewModelScope.launch(Dispatchers.IO) {
        _recipes.postValue(NetworkResult.Loading())
        if (hasInternetConnection()){
            try {
                val response = repository.remote.getRecipes(queries)
                _recipes.postValue(handleRecipesResponse(response))

                val foodRecipes = _recipes.value!!.data
                if(foodRecipes!=null){
                    offlineCacheRecipes(foodRecipes)
                }
            }catch (e : Exception){
                Timber.e(e)
                _recipes.postValue(NetworkResult.Error("Recipes not found"))
            }
        }else{
            _recipes.postValue(NetworkResult.Error("No internet Connection"))
        }
    }

    fun searchRecipes(searchQuery : Map<String,String>) = viewModelScope.launch(Dispatchers.IO) {
        _searchRecipes.postValue(NetworkResult.Loading())
        if (hasInternetConnection()){
            try {
                val response = repository.remote.searchRecipes(searchQuery)
                _searchRecipes.postValue(handleRecipesResponse(response))

            }catch (e : Exception){
                Timber.e(e)
                _searchRecipes.postValue(NetworkResult.Error("Recipes not found"))
            }
        }else{
            _searchRecipes.postValue(NetworkResult.Error("No internet Connection"))
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipesResponse) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
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