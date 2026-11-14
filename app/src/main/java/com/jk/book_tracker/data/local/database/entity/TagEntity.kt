package com.jk.book_tracker.data.local.database.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.UUID

// TODO P1: Create also pure domain model
@Serializable
@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
)
