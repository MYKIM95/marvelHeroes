package com.mykim.core_data.di

import com.mykim.core_data.repository.FavoriteDatabaseRepository
import com.mykim.core_data.repository.FavoriteDatabaseRepositoryImpl
import com.mykim.core_data.repository.SearchRepository
import com.mykim.core_data.repository.SearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsSearchRepository(searchRepository: SearchRepositoryImpl) : SearchRepository

    @Binds
    abstract fun bindsFavoriteDatabaseRepository(favoriteDatabaseRepository: FavoriteDatabaseRepositoryImpl) : FavoriteDatabaseRepository
}