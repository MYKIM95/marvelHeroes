package com.mykim.core_data.usecase

import com.mykim.core_data.repository.FavoriteDatabaseRepository
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(
    private val favoriteDatabaseRepository: FavoriteDatabaseRepository
) {
    suspend operator fun invoke(heroId: Int) = favoriteDatabaseRepository.deleteFavorite(heroId)
}