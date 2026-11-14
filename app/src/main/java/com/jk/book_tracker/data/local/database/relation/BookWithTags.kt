package com.jk.book_tracker.data.local.database.relation

import androidx.room3.Embedded
import androidx.room3.Junction
import androidx.room3.Relation
import com.jk.book_tracker.data.local.database.entity.BookEntity
import com.jk.book_tracker.data.local.database.entity.BookTagCrossRefEntity
import com.jk.book_tracker.data.local.database.entity.TagEntity
import kotlinx.serialization.Serializable

@Serializable
data class BookWithTags(
    @Embedded val book: BookEntity,

    @Relation(
        parentColumns = ["id"],
        entityColumns = ["id"],
        associateBy = Junction(
            value = BookTagCrossRefEntity::class,
            parentColumns = ["bookId"],
            entityColumns = ["tagId"]
        )
    )

    val tags: List<TagEntity>
)