package com.mykim.marvelheroes.search

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
import com.mykim.marvelheroes.BuildConfig
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

    companion object {
        private const val PUBLIC_KEY = BuildConfig.PUBLIC_KEY
        private const val PRIVATE_KEY = BuildConfig.PRIVATE_KEY
    }

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
        if (heroList.value.isNotEmpty()) {
            compareFavoriteSet(heroList.value)
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // 첫 호출 / 재호출(paging) 여부에 따른 검색 리스트 처리
    fun setHeroList(list: List<HeroData>) {
        if (isFirstSearch) {
            compareFavoriteSet(list)
        } else {
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
                modList[index] = heroData.copy(isFavorite = favoriteIdSet.contains(heroData.heroId))
            }
            _heroList.value = modList
            offset = modList.size
        }
    }

    private var searchJob: Job? = null
    fun searchHero() {

        // 두글자 미만일 때, api 호출 취소
        if (heroName.length < 2) {
            searchJob?.takeIf { it.isActive }?.cancel()
            return
        }

        val timeStamp = System.currentTimeMillis().toString()
        val hash = md5("$timeStamp$PRIVATE_KEY$PUBLIC_KEY")

        searchJob?.takeIf { it.isActive }?.cancel()
        searchJob = getHeroDataUseCase.invoke(
            nameStartsWith = heroName,
            ts = timeStamp,
            apikey = PUBLIC_KEY,
            hash = hash,
            offset = offset
        ).onStart {
            _heroDataState.value = ResultUiState.Loading
        }.onEach {
            total = it.data.total
            _heroDataState.value = ResultUiState.Success(it)
        }.onCompletion {
            _heroDataState.value = ResultUiState.Finish
        }.catch {
            _heroDataState.value = ResultUiState.Error(it)
        }.launchIn(viewModelScope)

    }

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    fun addFavoriteHero(heroData: HeroData) {
        viewModelScope.launch(ioDispatcher) {
            if (favoriteIdSet.size >= 5) {
                val firstItemId = getFirstItemIdUseCase.invoke()
                deleteFavoriteUseCase.invoke(firstItemId)
            }
            addFavoriteUseCase.invoke(
                FavoriteTable(
                    heroId = heroData.heroId,
                    thumbnail = heroData.thumbnail.toImageUrl(),
                    name = heroData.name,
                    desc = heroData.description
                )
            )
        }
    }

    fun removeFavoriteHero(heroData: HeroData) {
        viewModelScope.launch(ioDispatcher) {
            deleteFavoriteUseCase.invoke(heroData.heroId)
        }
    }

}