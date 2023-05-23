package com.example.businesscards.core.utils

sealed class Resource<out T> {

    class Success<T>(val data: T) : Resource<T>()

    class Error(val error: Exception) : Resource<Nothing>()

    object Loading : Resource<Nothing>()
}
