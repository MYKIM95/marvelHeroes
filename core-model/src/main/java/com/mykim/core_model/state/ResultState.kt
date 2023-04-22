package com.mykim.core_model.state

import kotlinx.coroutines.flow.MutableStateFlow

fun <T> mutableResultState(
    state: ResultState<T> = ResultState.Loading
): MutableStateFlow<ResultState<T>> = MutableStateFlow(state)

sealed class ResultState<out T> {

    object UnInitialize : ResultState<Nothing>()

    object Loading : ResultState<Nothing>()

    data class Success<T>(val data: T) : ResultState<T>()

    data class Error(val error: Throwable) : ResultState<Nothing>()
}