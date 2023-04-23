package com.mykim.core_data.usecase

import com.mykim.core_data.repository.SearchRepository
import javax.inject.Inject

class GetHeroDataUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(nameStartsWith: String, ts: String, apikey: String, hash: String, offset: Int) =
        searchRepository.getHeroInfoList(nameStartsWith, ts, apikey, hash, offset)
}