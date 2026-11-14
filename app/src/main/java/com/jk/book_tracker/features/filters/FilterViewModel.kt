package com.jk.book_tracker.features.filters

import androidx.lifecycle.ViewModel
import com.jk.book_tracker.domain.ReadingStatus
import com.jk.book_tracker.features.filters.domain.BookFilterState
import com.jk.book_tracker.features.filters.domain.SortByOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class FilterViewModel @Inject constructor() : ViewModel() {
    private val _filterState = MutableStateFlow(BookFilterState())
    val filterState: StateFlow<BookFilterState> = _filterState.asStateFlow()

    fun selectSortBy(sortByOptions: SortByOptions) {
        _filterState.update { it.copy(sortBy = sortByOptions) }
    }

    fun toggleReadingStatus(status: ReadingStatus) {
        _filterState.update {
            if (it.selectedStatuses.contains(status)) {
                it.copy(selectedStatuses = it.selectedStatuses - status)
            } else {
                it.copy(selectedStatuses = it.selectedStatuses + status)
            }
        }
    }

    fun toggleTag(tagId: String) {
        _filterState.update {
            if (it.selectedTagIds.contains(tagId)) {
                it.copy(selectedTagIds = it.selectedTagIds - tagId)
            } else {
                it.copy(selectedTagIds = it.selectedTagIds + tagId)
            }
        }
    }

    fun clearFilters() {
        _filterState.value = BookFilterState()
    }
}