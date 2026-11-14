package com.jk.book_tracker.features.add_book.domain

import com.jk.book_tracker.data.local.database.entity.TagEntity
import com.jk.book_tracker.domain.ReadingStatus

sealed interface AddBookFormEvent {
    data class OnCoverChanged(val value: ImageState) : AddBookFormEvent
    data class OnTitleChanged(val value: String) : AddBookFormEvent
    data class OnSubtitleChanged(val value: String) : AddBookFormEvent
    data class OnAuthorChanged(val value: String) : AddBookFormEvent
    data class OnDescriptionChanged(val value: String) : AddBookFormEvent

    data class OnStatusChanged(val value: ReadingStatus) : AddBookFormEvent
    data class OnRatingChanged(val value: Int?) : AddBookFormEvent
    data class OnNotesChanged(val value: String) : AddBookFormEvent
    data class OnTagChanged(val value: List<TagEntity>) : AddBookFormEvent
    data class OnPublisherChanged(val value: String) : AddBookFormEvent
}