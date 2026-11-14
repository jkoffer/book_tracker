package com.jk.book_tracker.data.local.database.entity

import androidx.room3.Entity
import androidx.room3.Ignore
import androidx.room3.PrimaryKey
import com.jk.book_tracker.domain.ReadingStatus
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

// TODO P1: Create also pure domain model

@Serializable
@Entity(tableName = "books")
data class BookEntity constructor(
    @PrimaryKey val id: String = Uuid.random().toString(),
    @Ignore val openLibKey: String? = null,
    val cover: String? = null,
    val title: String,
    val subtitle: String? = null,
    val description: String? = null,
    val author: String? = null,
    val readingStatus: ReadingStatus = ReadingStatus.FINISHED,
    val rating: Int? = null,
    val pageCount: Int? = null,
    val publisher: String? = null,
    val isbn: String? = null,
    val notes: String? = null,
    val addedAt: Long = System.currentTimeMillis()
)