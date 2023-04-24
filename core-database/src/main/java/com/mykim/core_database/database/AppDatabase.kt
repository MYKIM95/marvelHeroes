package com.mykim.core_database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mykim.core_database.dao.FavoriteDao
import com.mykim.core_database.entity.FavoriteTable

@Database(
    entities = [FavoriteTable::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun getFavoriteDao(): FavoriteDao
}