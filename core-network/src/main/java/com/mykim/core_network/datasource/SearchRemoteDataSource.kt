package com.mykim.core_network.datasource

import com.mykim.core_model.HeroResponseData
import kotlinx.coroutines.flow.Flow

interface SearchRemoteDataSource {
    fun getHeroInfoList(nameStartsWith: String, ts: String, apikey: String, hash: String): Flow<HeroResponseData>
}