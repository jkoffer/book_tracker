package com.jk.book_tracker.di

import com.jk.book_tracker.data.remote.api.OpenLibraryApi
import com.jk.book_tracker.data.remote.api.SerpApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OpenLibraryRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SerpRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val OPEN_LIB_BASE_URL = "https://openlibrary.org/"
    private const val SERP_BASE_URL = "https://serpapi.com/"

    // --- OPEN LIBRARY ---
    @Provides
    @Singleton
    @OpenLibraryRetrofit
    fun provideOpenLibRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(OPEN_LIB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenLibraryApi(
        @OpenLibraryRetrofit retrofit: Retrofit
    ): OpenLibraryApi {
        return retrofit.create(OpenLibraryApi::class.java)
    }


    // --- SERP ---
    @Provides
    @Singleton
    @SerpRetrofit
    fun provideSerpRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SERP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSerpApi(
        @SerpRetrofit retrofit: Retrofit
    ): SerpApi {
        return retrofit.create(SerpApi::class.java)
    }
}