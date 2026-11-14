package com.jk.book_tracker.features.add_book.domain

sealed interface AddBookSource {
    data class Local(val bookId: String) : AddBookSource
    data class RemoteIsbn(val isbn: String) : AddBookSource
    data class RemoteOpenLibId(val openLibId: String) : AddBookSource
}