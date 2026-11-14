package com.jk.book_tracker.features.add_book

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jk.book_tracker.data.local.database.entity.BookEntity
import com.jk.book_tracker.data.local.database.relation.BookWithTags
import com.jk.book_tracker.data.local.storage.ImageManager
import com.jk.book_tracker.data.repository.BookRepository
import com.jk.book_tracker.features.add_book.domain.AddBookFormEvent
import com.jk.book_tracker.features.add_book.domain.AddBookFormState
import com.jk.book_tracker.features.add_book.domain.AddBookSource
import com.jk.book_tracker.features.add_book.domain.ImageFileSource
import com.jk.book_tracker.features.add_book.domain.ImageState
import com.jk.book_tracker.features.add_book.domain.NoApiBookFound
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBookFormViewModel @Inject constructor(
    private val imageManager: ImageManager,
    private val bookRepository: BookRepository
) : ViewModel() {
    private var isInitialized = false
    var state by mutableStateOf(AddBookFormState())
        private set

    suspend fun init(source: AddBookSource?) {
        if (isInitialized) return
        isInitialized = true

        state = state.copy(isLoading = true)

        Log.d("source", source.toString())

        try {
            val book = when (source) {
                is AddBookSource.Local ->
                    bookRepository.getBookWithTags(source.bookId)

                is AddBookSource.RemoteIsbn ->
                    bookRepository.findBookByIsbn(source.isbn)

                is AddBookSource.RemoteOpenLibId ->
                    bookRepository.findBookByOpenLibId(source.openLibId)

                null -> null
            }

            Log.d("INIT BOOK", book.toString())

            if ((source is AddBookSource.RemoteIsbn || source is AddBookSource.RemoteOpenLibId) && book == null) {
                throw NoApiBookFound("Book not found.")
            }

            book?.let {
                val img = it.book.cover
                val cover = when (source) {
                    is AddBookSource.Local ->
                        img?.let { ImageState.Existing(ImageFileSource.Local(img.toUri())) }
                            ?: ImageState.None

                    is AddBookSource.RemoteIsbn ->
                        img?.let { ImageState.New(ImageFileSource.Remote(img)) }
                            ?: ImageState.None

                    is AddBookSource.RemoteOpenLibId ->
                        img?.let { ImageState.New(ImageFileSource.Remote(img)) }
                            ?: ImageState.None

                    else -> ImageState.None
                }

                state = AddBookFormState(
                    id = it.book.id,
                    cover = cover,
                    title = it.book.title,
                    subtitle = it.book.subtitle,
                    author = it.book.author,
                    readingStatus = it.book.readingStatus,
                    tags = it.tags,
                    addedAt = it.book.addedAt,
                    description = it.book.description,
                    rating = it.book.rating,
                    pageCount = it.book.pageCount,
                    publisher = it.book.publisher,
                    isbn = it.book.isbn,
                    notes = it.book.notes,
                    isFormValid = true,
                )
            }
        } finally {
            state = state.copy(isLoading = false)
        }
    }

    fun submit(onSubmit: (String) -> Unit) {
        viewModelScope.launch {
            val cover: String? = when (val cover = state.cover) {
                is ImageState.Existing -> {
                    (cover.data as ImageFileSource.Local).uri.path.toString()
                }

                // TODO: Delete existing image
                is ImageState.New ->
                    imageManager.saveImageToInternalStorage(cover.data)

                is ImageState.None -> null
            }

            val book = if (state.id.isNullOrBlank()) {
                BookEntity(
                    cover = cover,
                    title = state.title,
                    subtitle = state.subtitle,
                    author = state.author,
                    readingStatus = state.readingStatus,
                    description = state.description,
                    rating = state.rating,
                    pageCount = state.pageCount,
                    publisher = state.publisher,
                    isbn = state.isbn,
                    notes = state.notes,
                )
            } else {
                BookEntity(
                    id = state.id!!,
                    cover = cover,
                    title = state.title,
                    subtitle = state.subtitle,
                    author = state.author,
                    readingStatus = state.readingStatus,
                    description = state.description,
                    rating = state.rating,
                    pageCount = state.pageCount,
                    publisher = state.publisher,
                    isbn = state.isbn,
                    notes = state.notes,
                    addedAt = state.addedAt ?: System.currentTimeMillis()
                )
            }

            val bookWithTags = BookWithTags(
                book = book,
                tags = state.tags,
            )

            bookRepository.addBookWithTags(bookWithTags = bookWithTags)

            onSubmit(book.id)
        }
    }

    fun onEvent(event: AddBookFormEvent) {
        state = when (event) {
            is AddBookFormEvent.OnCoverChanged -> state.copy(cover = event.value)
            is AddBookFormEvent.OnTitleChanged -> state.copy(title = event.value)
            is AddBookFormEvent.OnSubtitleChanged -> state.copy(subtitle = event.value)
            is AddBookFormEvent.OnAuthorChanged -> state.copy(author = event.value)
            is AddBookFormEvent.OnStatusChanged -> state.copy(readingStatus = event.value)
            is AddBookFormEvent.OnTagChanged -> state.copy(tags = event.value)
            is AddBookFormEvent.OnDescriptionChanged -> state.copy(description = event.value)
            is AddBookFormEvent.OnNotesChanged -> state.copy(notes = event.value)
            is AddBookFormEvent.OnPublisherChanged -> state.copy(publisher = event.value)
            is AddBookFormEvent.OnRatingChanged -> state.copy(rating = event.value)
        }
    }
}