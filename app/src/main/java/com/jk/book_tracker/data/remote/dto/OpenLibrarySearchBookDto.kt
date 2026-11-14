package com.jk.book_tracker.data.remote.dto

import com.jk.book_tracker.data.local.database.entity.BookEntity
import com.google.gson.annotations.SerializedName
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class OpenLibrarySearchBookDto(
    val numFound: Long? = null,
    val start: Long? = null,
    val numFoundExact: Boolean? = null,
    @SerializedName("num_found")
    val numFound2: Long? = null,
    @SerializedName("documentation_url")
    val documentationUrl: String? = null,
    val q: String? = null,
    val offset: Any? = null,
    val docs: List<Doc>? = null,
)

data class Doc(
    @SerializedName("author_key")
    val authorKey: List<String>? = null,
    @SerializedName("author_name")
    val authorName: List<String>? = null,
    @SerializedName("cover_edition_key")
    val coverEditionKey: String? = null,
    @SerializedName("cover_height")
    val coverHeight: Long? = null,
    @SerializedName("cover_i")
    val coverI: Long? = null,
    @SerializedName("cover_width")
    val coverWidth: Long? = null,
    @SerializedName("edition_count")
    val editionCount: Long? = null,
    @SerializedName("first_publish_year")
    val firstPublishYear: Long? = null,
    @SerializedName("has_fulltext")
    val hasFulltext: Boolean? = null,
    val ia: List<String>? = null,
    @SerializedName("ia_collection")
    val iaCollection: List<String>? = null,
    val key: String? = null,
    val language: List<String>? = null,
    @SerializedName("lending_edition_s")
    val lendingEditionS: String? = null,
    @SerializedName("lending_identifier_s")
    val lendingIdentifierS: String? = null,
    @SerializedName("public_scan_b")
    val publicScanB: Boolean? = null,
    @SerializedName("series_key")
    val seriesKey: List<String>? = null,
    @SerializedName("series_name")
    val seriesName: List<String>? = null,
    @SerializedName("series_position")
    val seriesPosition: List<String>? = null,
    val title: String? = null,
    val subtitle: String? = null,
)

@OptIn(ExperimentalUuidApi::class)
fun OpenLibrarySearchBookDto.toDomain(): List<BookEntity> {
    return docs.orEmpty().map { doc ->
        val coverUrl = when {
            doc.coverI != null -> "https://covers.openlibrary.org/b/id/${doc.coverI}-L.jpg"
            !doc.coverEditionKey.isNullOrBlank() -> "https://covers.openlibrary.org/b/olid/${doc.coverEditionKey}-L.jpg"
            else -> null
        }

        val authorName = doc.authorName
            ?.filter { it.isNotBlank() }
            ?.joinToString(", ")
            ?.takeIf { it.isNotBlank() }

        BookEntity(
            id = doc.key?.takeIf { it.isNotBlank() } ?: Uuid.random().toString(),
            openLibKey = doc.coverEditionKey,
            isbn = null,
            cover = coverUrl,
            title = doc.title ?: "",
            subtitle = doc.subtitle,
            author = authorName,
        )
    }
}
