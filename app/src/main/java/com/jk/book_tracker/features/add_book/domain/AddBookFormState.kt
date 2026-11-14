package com.jk.book_tracker.features.add_book.domain

import com.jk.book_tracker.data.local.database.entity.TagEntity
import com.jk.book_tracker.domain.ReadingStatus

data class AddBookFormState(
    val id: String? = null,

    val cover: ImageState = ImageState.None,
    val title: String = "",
    val titleError: String? = null,
    val subtitle: String? = "",
    val author: String? = "",
    val description: String? = null,

    val readingStatus: ReadingStatus = ReadingStatus.WANT_TO_READ,
    val rating: Int? = null,
    val tags: List<TagEntity> = emptyList(),
    val notes: String? = null,


    val addedAt: Long? = null,

    val pageCount: Int? = null,
    val publisher: String? = null,
    val isbn: String? = null,


    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val isFormValid: Boolean = true
)