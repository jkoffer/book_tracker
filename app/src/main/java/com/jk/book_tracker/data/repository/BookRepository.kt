package com.jk.book_tracker.data.repository

import android.util.Log
import androidx.room3.withWriteTransaction
import com.jk.book_tracker.BuildConfig
import com.jk.book_tracker.data.local.database.AppDatabase
import com.jk.book_tracker.data.local.database.dao.BookDao
import com.jk.book_tracker.data.local.database.dao.BookTagDao
import com.jk.book_tracker.data.local.database.entity.BookEntity
import com.jk.book_tracker.data.local.database.entity.BookTagCrossRefEntity
import com.jk.book_tracker.data.local.database.relation.BookWithTags
import com.jk.book_tracker.data.remote.api.OpenLibraryApi
import com.jk.book_tracker.data.remote.api.SerpApi
import com.jk.book_tracker.data.remote.dto.toDomain
import com.example.model.toDomain
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val db: AppDatabase,
    private val bookDao: BookDao,
    private val bookTagDao: BookTagDao,
    private val openLibraryApiService: OpenLibraryApi,
    private val serpApiService: SerpApi,
) {
    val booksWithTags = bookDao.getBooksWithTags()

    suspend fun getBookWithTags(id: String): BookWithTags? {
        return bookDao.getBookWithTag(id)
    }

    suspend fun getAllBooks(): List<BookWithTags> {
        return bookDao.getBooksWithTags().first()
    }

    suspend fun addBookWithTags(bookWithTags: BookWithTags) {
        db.withWriteTransaction {
            bookDao.insertBook(bookWithTags.book)

            // Clear & Re-insert tags
            bookTagDao.deleteAllTagsFromBook(bookWithTags.book.id)

            bookWithTags.tags.forEach { tag ->
                bookTagDao.addTagToBook(
                    BookTagCrossRefEntity(
                        bookId = bookWithTags.book.id,
                        tagId = tag.id
                    )
                )
            }
        }
    }

    suspend fun addBooksWithTags(bookWithTags: List<BookWithTags>) {
        db.withWriteTransaction {
            bookWithTags.forEach { bookWithTags ->
                bookDao.insertBook(bookWithTags.book)

                // Clear & Re-insert tags
                bookTagDao.deleteAllTagsFromBook(bookWithTags.book.id)

                bookWithTags.tags.forEach { tag ->
                    bookTagDao.addTagToBook(
                        BookTagCrossRefEntity(
                            bookId = bookWithTags.book.id,
                            tagId = tag.id
                        )
                    )
                }
            }
        }
    }

    suspend fun deleteBookWithTags(
        bookId: String,
        tagIds: List<String>
    ) {
        db.withWriteTransaction {
            bookDao.deleteBook(bookId)

            tagIds.forEach {
                bookTagDao.deleteTagFromBook(
                    BookTagCrossRefEntity(
                        bookId = bookId,
                        tagId = it
                    )
                )
            }
        }
    }

    suspend fun findBookByIsbn(isbn: String): BookWithTags? {
        try {
            val response = openLibraryApiService.findBookByIsbn(isbn)

            if (response.isSuccessful) {
                val dto = response.body()

                if (dto != null) {
                    return dto.toDomain(scannedIsbn = isbn)
                }
            }
        } catch (e: Exception) {
            Log.e("BookRepository", "Error fetching book by ISBN $isbn", e)
        }

        return null
    }

    suspend fun findBookByOpenLibId(openLibId: String): BookWithTags? {
        try {
            val response = openLibraryApiService.findBookByOpenLibKey(openLibId)

            if (response.isSuccessful) {
                val dto = response.body()

                if (dto != null) {
                    return dto.toDomain(scannedIsbn = null)
                }
            }

        } catch (e: Exception) {
            Log.e("BookRepository", "Error fetching book by OpenLibId: $openLibId", e)
        }

        return null
    }

    suspend fun findBooksByTitle(title: String): List<BookEntity>? {
        try {
            val response = openLibraryApiService.findBooksByTitle(
                title = title,
            )

            if (response.isSuccessful) {
                val dto = response.body()

                if (dto != null) {
                    return dto.toDomain()
                } else {
                    Log.d("Error", "my error")
                }
            }
        } catch (e: Exception) {
            Log.e("BookRepository", "Error fetching book by ISBN", e)
        }

        return null
    }

    suspend fun findBookCoversByTitle(title: String): List<BookEntity>? {
        val apiKey = BuildConfig.SERP_API_KEY

        try {
            val response = serpApiService.searchCovers(
                engine = "google_images_light",
                query = "$title+cover",
                apiKey = apiKey,
            )

            if (response.isSuccessful) {
                val dto = response.body()

                if (dto != null) {
                    return dto.toDomain()
                }
            }
        } catch (e: Exception) {
            Log.e("BookRepository", "", e)
        }

        return null

    }
}