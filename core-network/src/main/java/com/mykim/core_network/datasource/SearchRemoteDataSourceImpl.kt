package com.mykim.core_network.datasource

import com.mykim.core_model.HeroResponseData
import com.mykim.core_network.service.SearchService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRemoteDataSourceImpl @Inject constructor(
    private val searchService: SearchService
): SearchRemoteDataSource {

    override fun getHeroInfoList(
        nameStartsWith: String,
        ts: String,
        apikey: String,
        hash: String,
        offset: Int
    ): Flow<HeroResponseData> = searchService.getHeroInfoList(nameStartsWith, ts, apikey, hash, offset)

}