package com.mykim.core_network.service

import com.mykim.core_model.HeroResponseData
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("/v1/public/characters")
    fun getHeroInfoList(
        @Query("nameStartsWith") nameStartsWith: String,
        @Query("ts") ts: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 20
    ): Flow<HeroResponseData>

}