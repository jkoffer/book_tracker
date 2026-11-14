package com.jk.book_tracker.di

import android.content.Context
import androidx.room3.Room
import com.jk.book_tracker.data.local.database.AppDatabase
import com.jk.book_tracker.data.local.database.dao.BookDao
import com.jk.book_tracker.data.local.database.dao.BookTagDao
import com.jk.book_tracker.data.local.database.dao.TagDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "book_tracker_db"
        ).build()
    }

    @Provides
    fun provideBookDao(db: AppDatabase): BookDao = db.bookDao()

    @Provides
    fun provideTagDao(db: AppDatabase): TagDao = db.tagDao()

    @Provides
    fun provideBookTagDao(db: AppDatabase): BookTagDao = db.bookTagDao()
}
