package com.mykim.core_data.repository

import com.mykim.core_database.entity.FavoriteTable
import kotlinx.coroutines.flow.Flow

interface FavoriteDatabaseRepository {

    fun getFavoriteList(): Flow<List<FavoriteTable>>

    suspend fun addFavorite(favoriteTable: FavoriteTable)

    suspend fun deleteFavorite(heroId: Int): Int

    suspend fun selectFirstFavoriteItemId(): Int
}