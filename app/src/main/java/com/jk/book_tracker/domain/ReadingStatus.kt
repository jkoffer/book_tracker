package com.jk.book_tracker.domain

enum class ReadingStatus(val displayName: String) {
    WISHLIST("Wishlist"),
    WANT_TO_READ("To read"),
    READING("Reading"),
    FINISHED("Finished"),
    DROPPED("Dropped")
}
