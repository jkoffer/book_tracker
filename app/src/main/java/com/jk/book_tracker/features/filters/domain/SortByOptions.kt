package com.jk.book_tracker.features.filters.domain

import com.jk.book_tracker.data.local.database.relation.BookWithTags

enum class SortByOptions(val value: String, val comparator: Comparator<in BookWithTags>) {
    DATE_ADDED_NEWEST("Newest date", compareBy { it.book.addedAt }),
    DATE_ADDED_OLDEST("Oldest date", compareByDescending { it.book.addedAt }),
    TITLE_AZ("Title A-Z", compareBy(String.CASE_INSENSITIVE_ORDER) { it.book.title }),
    TITLE_ZA("Title Z-A", compareByDescending(String.CASE_INSENSITIVE_ORDER) { it.book.title }),
    AUTHOR_AZ(
        "Author A-Z",
        compareBy(nullsFirst(String.CASE_INSENSITIVE_ORDER)) { it.book.author }),
    AUTHOR_ZA(
        "Author Z-A",
        compareByDescending(nullsFirst(String.CASE_INSENSITIVE_ORDER)) { it.book.author }),
    HIGHEST_RATE("Highest rated", compareByDescending { it.book.rating }),
    LOWEST_RATE("Lowest rated", compareBy { it.book.rating })
}