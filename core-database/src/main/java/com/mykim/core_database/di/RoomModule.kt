package com.mykim.core_database.di

import android.content.Context
import androidx.room.Room
import com.mykim.core_database.dao.FavoriteDao
import com.mykim.core_database.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context : Context) : AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "Favorite.db")
            .setQueryCallback(
                { sqlQuery, _ -> /*Log.i("123123, "sqlQuery : $sqlQuery")*/ },
                Executors.newSingleThreadExecutor()
            )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideFavoriteDao(appDatabase: AppDatabase) : FavoriteDao = appDatabase.getFavoriteDao()

}