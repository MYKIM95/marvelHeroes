package com.mykim.marvelheroes

import androidx.lifecycle.viewModelScope
import com.mykim.common_util.di.IoDispatcher
import com.mykim.common_util.onResult
import com.mykim.common_util.resultState
import com.mykim.commonbase.BaseViewModel
import com.mykim.core_data.usecase.GetFavoriteListUseCase
import com.mykim.core_database.entity.FavoriteTable
import com.mykim.core_model.state.ResultUiState
import com.mykim.core_model.state.mutableResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val getFavoriteListUseCase: GetFavoriteListUseCase
) : BaseViewModel() {

    private val _favoriteListState = mutableResultState<List<FavoriteTable>>(ResultUiState.UnInitialize)
    val favoriteListState = _favoriteListState.asStateFlow()

    private val _favoriteList = MutableStateFlow<List<FavoriteTable>>(listOf())
    val favoriteList = _favoriteList.asStateFlow()

    fun setFavoriteList(list: List<FavoriteTable>) {
        _favoriteList.value = list
    }

    fun getFavoriteList() {
        viewModelScope.launch(ioDispatcher) {
            getFavoriteListUseCase.invoke().resultState(this) { _favoriteListState.value = it }
        }
    }

}