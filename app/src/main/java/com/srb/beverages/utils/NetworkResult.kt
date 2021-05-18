package com.srb.beverages.utils

sealed class NetworkResult<T>{

    class Success<T>(val data: T) : NetworkResult<T>()
    class Error<T>(val message: String?,val data: T? = null,val status: Int? = null) : NetworkResult<T>()
    class Loading<T> : NetworkResult<T>()

}