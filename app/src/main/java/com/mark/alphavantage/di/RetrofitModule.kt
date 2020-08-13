package com.mark.alphavantage.di

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.mark.alphavantage.core.Constants
import com.mark.alphavantage.data.StockRepository
import com.mark.alphavantage.data.remote.StockRemoteDataSource
import com.mark.alphavantage.network.endpoint.StockEndpoint
import com.mark.alphavantage.network.endpoint.StockService
import okhttp3.Cache
import okhttp3.Call
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

val retrofitModule = module {

    single { retrofit(get(), Constants.BASE_URL) }

    single { get<Retrofit>().create(StockService::class.java) }

    single { StockEndpoint(get()) }

    single { StockRemoteDataSource(get()) }

    single { StockRepository(get()) }

    single<Call.Factory> {
        val cacheFile = cacheFile(androidContext())
        val cache = cache(cacheFile)
        provideOkhttp(cache)
    }
}

private fun cacheFile(context: Context) = File(context.filesDir, "my_own_created_cache").apply {
    if (!this.exists())
        mkdirs()
}

private fun cache(cacheFile: File) = Cache(cacheFile, Constants.CACHE_FILE_SIZE)

fun retrofit(callFactory: Call.Factory, baseUrl: String): Retrofit = Retrofit.Builder()
    .callFactory(callFactory)
    .baseUrl(baseUrl)
    .addConverterFactory(
        GsonConverterFactory.create(
            GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setPrettyPrinting().create()
        )
    )
    .build()

fun provideOkhttp(cache: Cache) = OkHttpClient.Builder()
    .connectTimeout(Constants.TIMEOUT_LENGTH.toLong(), TimeUnit.SECONDS)
    .readTimeout(Constants.TIMEOUT_LENGTH.toLong(), TimeUnit.SECONDS)
    .writeTimeout(Constants.TIMEOUT_LENGTH.toLong(), TimeUnit.SECONDS)
    .addInterceptor { chain ->
        val request = chain.request().newBuilder()
        val originalHttpUrl = chain.request().url
        val url =
            originalHttpUrl.newBuilder().addQueryParameter("apikey", Constants.API_KEY).build()
        request.url(url)
        return@addInterceptor chain.proceed(request.build())
    }
    .addInterceptor(
        LoggingInterceptor.Builder()
            .setLevel(Level.BASIC)
            .log(Log.INFO)
            .request("Request")
            .response("Response")
            .build()
    )
    .cache(cache)
    .build()