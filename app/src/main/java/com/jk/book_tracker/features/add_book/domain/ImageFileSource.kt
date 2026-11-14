package com.jk.book_tracker.features.add_book.domain

import android.net.Uri

sealed interface ImageFileSource {
    data class Local(val uri: Uri) : ImageFileSource

    data class Remote(val url: String) : ImageFileSource
}