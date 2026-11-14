package com.jk.book_tracker.data.remote.api

import com.jk.book_tracker.data.remote.dto.OpenLibraryBookDto
import com.jk.book_tracker.data.remote.dto.OpenLibrarySearchBookDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenLibraryApi {
    @GET("isbn/{isbn}.json")
    suspend fun findBookByIsbn(
        @Path("isbn") isbn: String
    ): Response<OpenLibraryBookDto>

    @GET("books/{key}.json")
    suspend fun findBookByOpenLibKey(
        @Path("key") key: String
    ) : Response<OpenLibraryBookDto>

    @GET("search.json")
    suspend fun findBooksByTitle(
        @Query("q") title: String,
        @Query("limit") limit: Int = 10
    ): Response<OpenLibrarySearchBookDto>
}
