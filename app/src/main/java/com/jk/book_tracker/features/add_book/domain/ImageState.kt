package com.jk.book_tracker.features.add_book.domain

sealed interface ImageState {
    val data: ImageFileSource?

    data object None : ImageState {
        override val data: ImageFileSource? = null
    }

    data class Existing(
        override val data: ImageFileSource?
    ) : ImageState

    data class New(
        override val data: ImageFileSource?
    ) : ImageState
}