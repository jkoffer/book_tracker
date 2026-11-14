package com.jk.book_tracker.data.local.database.dao

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Upsert
import com.jk.book_tracker.data.local.database.entity.BookEntity
import com.jk.book_tracker.data.local.database.relation.BookWithTags
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Transaction
    @Query("SELECT * FROM books")
    fun getBooksWithTags(): Flow<List<BookWithTags>>

    @Transaction
    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getBookWithTag(id: String): BookWithTags?

    @Upsert
    suspend fun insertBook(book: BookEntity)

    @Transaction
    suspend fun insertBooks(books: List<BookEntity>) {
        books.forEach {
            insertBook(it)
        }
    }

    @Query("DELETE FROM books WHERE id = :id")
    suspend fun deleteBook(id: String)
}