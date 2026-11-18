package com.jk.book_tracker.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jk.book_tracker.data.local.database.relation.BookWithTags
import com.jk.book_tracker.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.emptyList

@HiltViewModel
class BookViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {
    val booksWithTags = repository.booksWithTags.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    suspend fun getBookById(id: String): BookWithTags? {
        return repository.getBookWithTags(id)
    }

    fun deleteBook(bookId: String) {
        viewModelScope.launch {
            repository.deleteBookWithTags(bookId)
        }
    }
}