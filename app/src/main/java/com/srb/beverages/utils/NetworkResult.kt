package com.srb.beverages.utils

sealed class NetworkResult<T>(
    val data : T? = null,
    val message: String? = null
){

    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String?,data: T? = null, val status: Int? = null) : NetworkResult<T>(data,message)
    class Loading<T> : NetworkResult<T>()

}