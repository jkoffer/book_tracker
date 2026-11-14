package com.example.model

import com.jk.book_tracker.data.local.database.entity.BookEntity
import com.google.gson.annotations.SerializedName

data class SerpDto(
    @SerializedName("search_metadata") val searchMetadata: SearchMetadata,
    @SerializedName("search_parameters") val searchParameters: SearchParameters,
    @SerializedName("search_information") val searchInformation: SearchInformation,
    @SerializedName("images_results") val imagesResults: List<ImagesResults>,
    @SerializedName("serpapi_pagination") val serpapiPagination: SerpapiPagination
)

data class SearchMetadata(
    val id: String,
    val status: String,
    @SerializedName("json_endpoint") val jsonEndpoint: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("processed_at") val processedAt: String,
    @SerializedName("google_images_light_url") val googleImagesLightUrl: String,
    @SerializedName("raw_html_file") val rawHtmlFile: String,
    @SerializedName("total_time_taken") val totalTimeTaken: Double
)

data class SearchParameters(
    val engine: String,
    val q: String,
    @SerializedName("google_domain") val googleDomain: String,
    val hl: String,
    val gl: String,
    val device: String
)

data class SearchInformation(
    @SerializedName("image_results_state") val imageResultsState: String
)

data class ImagesResults(
    val position: Int,
    val title: String,
    val source: String,
    val link: String,
    @SerializedName("raw_link") val rawLink: String,
    val original: String,
    @SerializedName("original_width") val originalWidth: Int,
    @SerializedName("original_height") val originalHeight: Int,
    val thumbnail: String,
    @SerializedName("serpapi_thumbnail") val serpapiThumbnail: String,
    @SerializedName("related_content_id") val relatedContentId: String,
    @SerializedName("serpapi_related_content_link") val serpapiRelatedContentLink: String
)

data class SerpapiPagination(
    val current: Int,
    val next: String
)

fun SerpDto.toDomain(): List<BookEntity> {
    return imagesResults.map { item ->
        BookEntity(
            title = "",
            cover = item.serpapiThumbnail,
        )
    }
}