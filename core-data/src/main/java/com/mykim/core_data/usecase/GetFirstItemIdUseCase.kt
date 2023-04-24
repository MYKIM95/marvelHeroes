package com.mykim.core_data.usecase

import com.mykim.core_data.repository.FavoriteDatabaseRepository
import javax.inject.Inject

class GetFirstItemIdUseCase @Inject constructor(
    private val favoriteDatabaseRepository: FavoriteDatabaseRepository
){
    operator fun invoke() = favoriteDatabaseRepository.selectFirstFavoriteItemId()
}