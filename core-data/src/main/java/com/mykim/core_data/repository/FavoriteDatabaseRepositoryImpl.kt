package com.mykim.core_data.repository

import com.mykim.core_database.dao.FavoriteDao
import com.mykim.core_database.entity.FavoriteTable
import javax.inject.Inject

class FavoriteDatabaseRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
): FavoriteDatabaseRepository {

    override fun getFavoriteList() = favoriteDao.selectAllFavoriteTable()

    override suspend fun addFavorite(favoriteTable: FavoriteTable) = favoriteDao.addFavoriteHero(favoriteTable)

    override suspend fun deleteFavorite(heroId: Int): Int = favoriteDao.deleteFavoriteHero(heroId)

    override suspend fun selectFirstFavoriteItemId(): Int = favoriteDao.selectFirstFavoriteItemId()

}