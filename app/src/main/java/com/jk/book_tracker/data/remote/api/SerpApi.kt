package com.jk.book_tracker.data.remote.api

import com.example.model.SerpDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SerpApi {
    @GET("search")
    suspend fun searchCovers(
        @Query("engine") engine: String,
        @Query("q") query: String,
        @Query("api_key") apiKey: String,
    ): Response<SerpDto>
}