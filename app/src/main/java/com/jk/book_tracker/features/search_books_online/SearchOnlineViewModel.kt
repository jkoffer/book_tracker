package com.jk.book_tracker.features.search_books_online

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jk.book_tracker.data.local.database.entity.BookEntity
import com.jk.book_tracker.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchOnlineViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    private val _searchResults = MutableStateFlow<List<BookEntity>>(emptyList())
    val searchResults: StateFlow<List<BookEntity>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _hasSearched = MutableStateFlow(false)
    val hasSearched: StateFlow<Boolean> = _hasSearched.asStateFlow()

    fun search(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val results = bookRepository.findBooksByTitle(query)

                _searchResults.value = results.orEmpty()

            } finally {
                _isLoading.value = false
                _hasSearched.value = true
            }
        }
    }

    fun searchCovers(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true

            try {
                val results = bookRepository.findBookCoversByTitle(query)

                _searchResults.value = results.orEmpty()

            } finally {
                _isLoading.value = false
                _hasSearched.value = true
            }
        }
    }
}
