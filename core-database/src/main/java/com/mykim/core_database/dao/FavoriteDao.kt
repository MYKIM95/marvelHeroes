package com.mykim.core_database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mykim.core_database.entity.FavoriteTable
import kotlinx.coroutines.flow.Flow
import java.sql.SQLException

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM FavoriteTable ORDER BY id ASC")
    fun selectAllFavoriteTable(): Flow<List<FavoriteTable>>

    @Throws(SQLException::class)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteHero(favoriteTable: FavoriteTable)

    @Query("DELETE FROM FavoriteTable WHERE heroId = :heroId")
    suspend fun deleteFavoriteHero(heroId: Int): Int

    @Query("SELECT heroId FROM FavoriteTable LIMIT 1")
    suspend fun selectFirstFavoriteItemId(): Int
}