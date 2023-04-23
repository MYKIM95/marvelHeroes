package com.mykim.core_data.repository

import com.mykim.core_network.datasource.SearchRemoteDataSource
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchRemoteDataSource: SearchRemoteDataSource
): SearchRepository {
    override fun getHeroInfoList(
        nameStartsWith: String,
        ts: String,
        apikey: String,
        hash: String,
        offset: Int
    ) =searchRemoteDataSource.getHeroInfoList(nameStartsWith, ts, apikey, hash, offset)
}