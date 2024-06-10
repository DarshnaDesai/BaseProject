package com.takehomeproject.util

sealed interface Results<out T> {
    data class Success<T>(val data: T) : Results<T>
    data class Error(val exception: Throwable) : Results<Nothing>
    object Loading : Results<Nothing>
}