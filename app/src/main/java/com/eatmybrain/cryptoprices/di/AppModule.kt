package com.eatmybrain.cryptoprices.di

import com.eatmybrain.cryptoprices.BuildConfig
import com.eatmybrain.cryptoprices.data.Repository
import com.eatmybrain.cryptoprices.data.api.CoinmarketApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoggingClient() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    @Provides
    @Singleton
    fun provideCoinmarketApi(loggingClient: OkHttpClient):CoinmarketApi = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(loggingClient)
        .build()
        .create(CoinmarketApi::class.java)

    @Provides
    @Singleton
    fun provideRepository(api:CoinmarketApi):Repository = Repository(api)
}