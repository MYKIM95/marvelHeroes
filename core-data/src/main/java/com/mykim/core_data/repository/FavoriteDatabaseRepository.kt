package com.mykim.core_data.repository

import com.mykim.core_database.entity.FavoriteTable
import kotlinx.coroutines.flow.Flow

interface FavoriteDatabaseRepository {

    fun getFavoriteList(): Flow<List<FavoriteTable>>

    fun addFavorite(favoriteTable: FavoriteTable)

    fun deleteFavorite(heroId: Int): Int

}