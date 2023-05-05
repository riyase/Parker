package com.keepqueue.sparepark.data.response

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    object Loading: Result<Nothing>()
    data class Error(val exception: Exception, val message: String? = null) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> {
                "Success[data=$data]"
            }
            is Loading -> {
                "Loading"
            }
            is Error -> {
                "Error[exception=$exception]"
            }
        }
    }
}