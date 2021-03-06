package com.eatmybrain.cryptoprices.di

import com.eatmybrain.cryptoprices.BuildConfig
import com.eatmybrain.cryptoprices.data.Repository
import com.eatmybrain.cryptoprices.data.api.CoinmarketApi
import com.eatmybrain.cryptoprices.data.api.FinnhubApi
import com.eatmybrain.cryptoprices.util.ChartData
import com.eatmybrain.cryptoprices.util.Time
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
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        .build()

    @Provides
    @Singleton
    fun provideCoinmarketApi(loggingClient: OkHttpClient):CoinmarketApi = Retrofit.Builder()
        .baseUrl(BuildConfig.COINMARKETCAP_API)
        .addConverterFactory(GsonConverterFactory.create())
        .client(loggingClient)
        .build()
        .create(CoinmarketApi::class.java)


    @Provides
    @Singleton
    fun provideFinnhubApi(loggingClient: OkHttpClient): FinnhubApi = Retrofit.Builder()
        .baseUrl(BuildConfig.FINNHUB_API)
        .addConverterFactory(GsonConverterFactory.create())
        .client(loggingClient)
        .build()
        .create(FinnhubApi::class.java)

    @Provides
    @Singleton
    fun provideRepository(
        coinmarketApi: CoinmarketApi,
        finnhubApi: FinnhubApi
    ):Repository = Repository(coinmarketApi, finnhubApi)


    @Provides
    @Singleton
    fun provideTime() = Time()

    @Provides
    @Singleton
    fun provideChartData() = ChartData()

}