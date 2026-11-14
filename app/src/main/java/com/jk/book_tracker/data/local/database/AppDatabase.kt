package com.jk.book_tracker.data.local.database

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.jk.book_tracker.data.local.database.dao.BookDao
import com.jk.book_tracker.data.local.database.dao.BookTagDao
import com.jk.book_tracker.data.local.database.dao.TagDao
import com.jk.book_tracker.data.local.database.entity.BookEntity
import com.jk.book_tracker.data.local.database.entity.BookTagCrossRefEntity
import com.jk.book_tracker.data.local.database.entity.TagEntity

@Database(
    version = 1,
    entities = [BookEntity::class, TagEntity::class, BookTagCrossRefEntity::class],
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    abstract fun tagDao(): TagDao

    abstract fun bookTagDao(): BookTagDao
}