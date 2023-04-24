package com.mykim.marvelheroes.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mykim.common_util.di.DefaultDispatcher
import com.mykim.common_util.di.IoDispatcher
import com.mykim.commonbase.BaseViewModel
import com.mykim.core_data.usecase.AddFavoriteUseCase
import com.mykim.core_data.usecase.DeleteFavoriteUseCase
import com.mykim.core_data.usecase.GetFirstItemIdUseCase
import com.mykim.core_data.usecase.GetHeroDataUseCase
import com.mykim.core_database.entity.FavoriteTable
import com.mykim.core_model.HeroData
import com.mykim.core_model.HeroResponseData
import com.mykim.core_model.state.ResultUiState
import com.mykim.core_model.state.mutableResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val getHeroDataUseCase: GetHeroDataUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    private val getFirstItemIdUseCase: GetFirstItemIdUseCase
) : BaseViewModel() {


    // TODO 웬만하면 다 viewModel 안에서 바꾸도록...
    var isFirstSearch = true
    var heroName = ""
    var total = 0
    var offset = 0
    private var favoriteIdSet = setOf<Int>()

    // 검색 단어
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // 검색 상태
    private val _heroDataState = mutableResultState<HeroResponseData>(ResultUiState.UnInitialize)
    val heroDataState = _heroDataState.asStateFlow()

    // 결과 리스트
    private val _heroList = MutableStateFlow<List<HeroData>>(listOf())
    val heroList = _heroList.asStateFlow()

    fun setFavoriteList(list: List<FavoriteTable>) {
        favoriteIdSet = list.map { it.heroId }.toSet()
        if(heroList.value.isNotEmpty()) { compareFavoriteSet(heroList.value) }
    }

    fun setSearchQuery(query : String) {
        _searchQuery.value = query
    }

    fun setHeroList(list: List<HeroData>) {
        if(isFirstSearch) {
            compareFavoriteSet(list)
        }else {
            val modList = _heroList.value.toMutableList()
            modList.addAll(list)
            compareFavoriteSet(modList)
        }
    }

    // favoriteSet 과 비교하여 즐겨찾기 표시/해제
    private fun compareFavoriteSet(list: List<HeroData>) {
        CoroutineScope(defaultDispatcher).launch {
            val modList = list.toMutableList()
            modList.forEachIndexed { index, heroData ->
                modList[index] = heroData.copy(isFavorite = favoriteIdSet.contains(heroData.id))
            }
            _heroList.value = modList
            offset = modList.size
        }
    }

    private var searchJob: Job? = null
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

        //TODO 두 글자 에서 한글자로 변할 때 cancel
        if(heroName.length < 2) searchJob?.takeIf { it.isActive }?.cancel()

    }

    private fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    fun addFavoriteHero(heroData: HeroData) {

        // TODO 이 부분 순차적으로 실행되는게 보장되는지?
        viewModelScope.launch(ioDispatcher) {
            if(favoriteIdSet.size >= 5) {
                val firstItemId = getFirstItemIdUseCase.invoke()
                deleteFavoriteUseCase.invoke(firstItemId)
            }
            addFavoriteUseCase.invoke(FavoriteTable(
                heroId = heroData.id,
                thumbnail = heroData.thumbnail.toImageUrl(),
                name = heroData.name,
                desc = heroData.description
            ))
        }
    }

    fun removeFavoriteHero(heroData: HeroData) {
        viewModelScope.launch(ioDispatcher) {
            deleteFavoriteUseCase.invoke(heroData.id)
        }
    }

}