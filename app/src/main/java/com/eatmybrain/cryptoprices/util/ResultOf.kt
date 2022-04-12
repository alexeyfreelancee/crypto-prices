package com.eatmybrain.cryptoprices.util

sealed class ResultOf<out T> {
    data class Success<out R>(val result:R) : ResultOf<R>()
    data class Failure(val message:String) : ResultOf<Nothing>()
}


inline fun <reified T> ResultOf<T>.doIfFailure(callback: (error: String) -> Unit) {
    if (this is ResultOf.Failure) {
        callback(message)
    }
}

inline fun <reified T> ResultOf<T>.doIfSuccess(callback: (value: T) -> Unit) {
    if (this is ResultOf.Success) {
        callback(result)
    }
}