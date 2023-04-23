package com.mykim.marvelheroes.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mykim.common_util.di.DefaultDispatcher
import com.mykim.common_util.di.IoDispatcher
import com.mykim.common_util.resultState
import com.mykim.commonbase.BaseViewModel
import com.mykim.core_data.usecase.GetHeroDataUseCase
import com.mykim.core_model.HeroData
import com.mykim.core_model.HeroResponseData
import com.mykim.core_model.state.ResultUiState
import com.mykim.core_model.state.mutableResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    private val getHeroDataUseCase: GetHeroDataUseCase
) : BaseViewModel() {

    var isFirstSearch = true
    var heroName = ""
    var total = 0
    var offset = 0

    // 검색 단어
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // 검색 상태
    private val _heroDataState = mutableResultState<HeroResponseData>(ResultUiState.UnInitialize)
    val heroDataState = _heroDataState.asStateFlow()

    // 결과 리스트
    private val _heroList = MutableStateFlow(listOf<HeroData>())
    val heroList = _heroList.asStateFlow()

    fun setSearchQuery(query : String) {
        _searchQuery.value = query
    }

    fun setHeroList(list: List<HeroData>) {
        if(isFirstSearch) {
            _heroList.value = list
        }else {
            val modList = _heroList.value.toMutableList()
            modList.addAll(list)
            _heroList.value = modList
        }
        offset = heroList.value.size
    }

    var searchJob: Job? = null
    fun searchHero() {


        Log.d("123123123", "cancel 전 : ${searchJob?.isActive}")
        searchJob?.takeIf { it.isActive }?.cancel()
        Log.d("123123123", "cancel 후 : ${searchJob?.isActive}")
        searchJob = getHeroDataUseCase.invoke(
            nameStartsWith = heroName,
            ts = timeStamp,
            apikey = apikey,
            hash = hash,
            offset = offset
        ).onStart {
            _heroDataState.value = ResultUiState.Loading
        }.onEach {
            Log.d("123123123", "success : $it")
            total = it.data.total
            _heroDataState.value = ResultUiState.Success(it)
        }.onCompletion {
            _heroDataState.value = ResultUiState.Finish
        }.catch {
            _heroDataState.value = ResultUiState.Error(it)
        }.launchIn(viewModelScope)

        //TODO 두 글자 미만일 떄 cancel???
        if(heroName.length < 2) searchJob?.takeIf { it.isActive }?.cancel()

        // TODO defaultDispatcher 에서 계산 작업

    }

    private fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

}