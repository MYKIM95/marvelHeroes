package com.mykim.common_util

import androidx.lifecycle.LifecycleCoroutineScope
import com.mykim.core_model.state.ResultUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

fun <T> Flow<T>.onResult(scope: LifecycleCoroutineScope, action: (T) -> Unit) {
    scope.launch {
        collect(action)
    }
}

inline fun <T> Flow<T>.collect(
    externalScope: CoroutineScope,
    crossinline collect: (T) -> Unit
) = onEach { collect.invoke(it) }.launchIn(externalScope)

fun <T> Flow<T>.resultState(
    scope: CoroutineScope,
    delay: Long? = null,
    collect: (ResultUiState<T>) -> Unit
) {
    onLoading(delay) { collect(ResultUiState.Loading) }
        .mapLatest { collect(ResultUiState.Success(it)) }
        .catch { collect(ResultUiState.Error(it)) }
        .onCompletion { collect(ResultUiState.Finish) }
        .launchIn(scope)
}

fun <T> Flow<T>.onLoading(
    delay: Long? = null,
    action: suspend () -> Unit
): Flow<T> = onStart {
    action.invoke()
    if(delay != null) delay(delay)
}

fun <T> Flow<ResultUiState<T>>.onUiState(
    scope : CoroutineScope,
    loading: () -> Unit = {},
    success: (T) -> Unit = {},
    error: (Throwable) -> Unit = {},
    finish: () -> Unit = {}
) {
    collect(scope) { state ->
        when (state) {
            ResultUiState.Loading -> loading()
            is ResultUiState.Success -> success(state.data)
            is ResultUiState.Error -> error(state.error)
            ResultUiState.Finish -> finish()
            else -> Unit
        }
    }
}