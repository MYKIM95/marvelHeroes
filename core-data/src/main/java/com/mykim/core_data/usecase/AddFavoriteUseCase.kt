package com.mykim.core_data.usecase

import com.mykim.core_data.repository.FavoriteDatabaseRepository
import com.mykim.core_database.entity.FavoriteTable
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val favoriteDatabaseRepository: FavoriteDatabaseRepository
) {
    operator fun invoke(favoriteTable: FavoriteTable) = favoriteDatabaseRepository.addFavorite(favoriteTable)
}