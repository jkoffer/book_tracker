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
        val crossRefs = bookWithTags.tags.map { tag ->
            BookTagCrossRefEntity(
                bookId = bookWithTags.book.id,
                tagId = tag.id
            )
        }

        db.withWriteTransaction {
            bookDao.insertBook(bookWithTags.book)

            // Clear all tags of this book
            bookTagDao.deleteAllTagsFromBook(bookWithTags.book.id)

            // Insert new tags (if any)
            if (crossRefs.isNotEmpty()) {
                bookTagDao.addTagsToBooks(crossRefs)
            }
        }
    }

    suspend fun addBooksWithTags(bookWithTags: List<BookWithTags>) {
        val books = bookWithTags.map { it.book }
        val bookIds = bookWithTags.map { it.book.id }
        val crossRefs = bookWithTags.flatMap { item ->
            item.tags.map { tag ->
                BookTagCrossRefEntity(
                    bookId = item.book.id,
                    tagId = tag.id
                )
            }
        }

        db.withWriteTransaction {
            bookDao.insertBooks(books)

            bookTagDao.deleteAllTagsFromBooks(bookIds)

            if (crossRefs.isNotEmpty()) {
                bookTagDao.addTagsToBooks(crossRefs)
            }
        }
    }

    suspend fun deleteBookWithTags(bookId: String) {
        bookDao.deleteBook(bookId)
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