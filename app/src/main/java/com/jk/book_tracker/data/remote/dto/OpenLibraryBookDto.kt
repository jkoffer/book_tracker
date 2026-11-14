package com.jk.book_tracker.data.remote.dto

import com.jk.book_tracker.data.local.database.entity.BookEntity
import com.jk.book_tracker.data.local.database.relation.BookWithTags
import com.jk.book_tracker.domain.ReadingStatus
import com.google.gson.annotations.SerializedName

data class OpenLibraryBookDto(
    val isbn: String? = null,
    val title: String? = null,
    val subtitle: String? = null,
    @SerializedName("by_statement")
    val byStatement: String? = null,
    val authors: List<AuthorDto>? = null,
    @SerializedName("number_of_pages")
    val numberOfPages: Int? = null,
    val publishers: List<String>? = null,
    val covers: List<Int>? = null,
)

data class AuthorDto(
    val key: String? = null,
    val name: String? = null,
)

fun OpenLibraryBookDto.toDomain(scannedIsbn: String?): BookWithTags {
    val coverUrl = covers?.firstOrNull()?.let { id ->
        "https://covers.openlibrary.org/b/id/$id-L.jpg"
    }

    val authorName = byStatement?.takeIf { it.isNotBlank() }
        ?: authors.orEmpty().mapNotNull { it.name }.joinToString(", ").takeIf { it.isNotBlank() }

    val publisherName = publishers?.firstOrNull()

    return BookWithTags(
        book = BookEntity(
            id = "",
            cover = coverUrl,
            title = title ?: "",
            subtitle = subtitle,
            author = authorName,
            publisher = publisherName,
            pageCount = numberOfPages,
            isbn = scannedIsbn ?: isbn,
            readingStatus = ReadingStatus.READING,
            rating = null,
        ),
        tags = emptyList(),
    )
}
