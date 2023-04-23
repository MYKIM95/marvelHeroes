package com.mykim.core_data.repository

import com.mykim.core_model.HeroResponseData
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getHeroInfoList(nameStartsWith: String, ts: String, apikey: String, hash: String, offset: Int): Flow<HeroResponseData>
}