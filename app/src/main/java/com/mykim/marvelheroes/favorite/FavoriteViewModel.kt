package com.mykim.marvelheroes.favorite

import androidx.lifecycle.viewModelScope
import com.mykim.common_util.di.IoDispatcher
import com.mykim.commonbase.BaseViewModel
import com.mykim.core_data.usecase.DeleteFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase
) : BaseViewModel() {

    fun removeFavoriteHero(id: Int) {
        viewModelScope.launch(ioDispatcher) {
            deleteFavoriteUseCase.invoke(id)
        }
    }

}