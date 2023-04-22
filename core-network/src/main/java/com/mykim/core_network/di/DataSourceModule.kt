package com.mykim.core_network.di

import com.mykim.core_network.datasource.SearchRemoteDataSource
import com.mykim.core_network.datasource.SearchRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    abstract fun bindSearchDataSource(searchRemoteDataSource: SearchRemoteDataSourceImpl): SearchRemoteDataSource
}