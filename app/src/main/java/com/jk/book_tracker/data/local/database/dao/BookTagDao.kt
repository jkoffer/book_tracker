package com.jk.book_tracker.data.local.database.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.jk.book_tracker.data.local.database.entity.BookTagCrossRefEntity

@Dao
interface BookTagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTagsToBooks(refs: List<BookTagCrossRefEntity>)

    @Query("DELETE FROM book_tags WHERE bookId = :bookId")
    suspend fun deleteAllTagsFromBook(bookId: String)

    @Query("DELETE FROM book_tags WHERE bookId IN (:bookIds)")
    suspend fun deleteAllTagsFromBooks(bookIds: List<String>)
}