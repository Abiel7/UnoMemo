package com.example.unomemo.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    abstract val data: Any

    data class Success<out T : Any>(override val data: T) : Result<T>()
    data class Error(val exception: Exception, override val data: Any) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}