package com.namma.platform.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.namma.platform.BuildConfig
import com.namma.platform.data.remote.api.ERailApiService
import com.namma.platform.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        val rapidApiInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .addHeader("x-rapidapi-host", "irctc1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", BuildConfig.ERAIL_API_KEY)
                .build()
            chain.proceed(requestBuilder)
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(rapidApiInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideERailApiService(
        okHttpClient: OkHttpClient,
        json: Json
    ): ERailApiService {
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl("https://irctc1.p.rapidapi.com/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(ERailApiService::class.java)
    }
}
