package com.jk.book_tracker.features.filters.domain

import com.jk.book_tracker.domain.ReadingStatus

data class BookFilterState(
    val sortBy: SortByOptions = SortByOptions.DATE_ADDED_NEWEST,
    val selectedStatuses: Set<ReadingStatus> = emptySet(),
    val selectedTagIds: Set<String> = emptySet()
)