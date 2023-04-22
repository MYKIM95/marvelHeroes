package com.mykim.common_util

import androidx.lifecycle.LifecycleCoroutineScope
import com.mykim.core_model.state.ResultState
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
    collect: (ResultState<T>) -> Unit
) {
    onLoading(delay) { collect(ResultState.Loading) }
        .mapLatest { collect(ResultState.Success(it)) }
        .catch { collect(ResultState.Error(it)) }
        .launchIn(scope)
}

fun <T> Flow<T>.onLoading(
    delay: Long? = null,
    action: suspend () -> Unit
): Flow<T> = onStart {
    action.invoke()
    if(delay != null) delay(delay)
}