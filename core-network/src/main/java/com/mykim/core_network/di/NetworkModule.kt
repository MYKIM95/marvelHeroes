package com.mykim.core_network.di

import com.google.gson.Gson
import com.mykim.core_network.adapter.FlowCallAdapterFactory
import com.mykim.core_network.service.SearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun providesMarvelUrl() = "https://gateway.marvel.com"

    @Singleton
    @Provides
    fun getHeaderInterceptor() = Interceptor { chain ->
        val builder = chain.request().newBuilder().apply {
            addHeader("Content-Type", "application/json;charset=UTF-8")
        }
        chain.proceed(builder.build())
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        val timeout = 2L
        val timeUnit = TimeUnit.MINUTES

        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(getHeaderInterceptor())
            .connectTimeout(timeout, timeUnit)
            .writeTimeout(timeout, timeUnit)
            .readTimeout(timeout, timeUnit)
            .retryOnConnectionFailure(true)

        return okHttpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun providesRetrofitBuilder(
        okHttpClient: OkHttpClient
    ): Retrofit.Builder =
        Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(FlowCallAdapterFactory())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(Gson()))


    @Singleton
    @Provides
    fun providesApiService(
        retrofit: Retrofit.Builder
    ): SearchService = retrofit.baseUrl(providesMarvelUrl()).build().create(SearchService::class.java)

}