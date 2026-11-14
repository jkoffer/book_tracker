package com.jk.book_tracker.data.local

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.jk.book_tracker.data.local.database.entity.TagEntity
import com.jk.book_tracker.data.local.database.relation.BookWithTags
import com.jk.book_tracker.data.repository.BookRepository
import com.jk.book_tracker.data.repository.TagRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import javax.inject.Inject

@Serializable
data class BackupData(
    val version: Int,
    val books: List<BookWithTags>,
    val tags: List<TagEntity>
)

class BackupManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bookRepository: BookRepository,
    private val tagsRepository: TagRepository,
) {
    suspend fun import(uri: Uri) = withContext(Dispatchers.IO) {
        try {
            var json: String? = null

            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                ZipInputStream(inputStream).use { zip ->
                    var entry = zip.nextEntry

                    while (entry != null) {
                        when {
                            entry.name == "data.json" -> {
                                json = zip.readBytes().decodeToString()
                            }

                            entry.name.startsWith("covers/") && !entry.isDirectory -> {
                                val fileName = File(entry.name).name
                                val outputFile = File(context.filesDir, fileName)

                                outputFile.outputStream().use { output ->
                                    zip.copyTo(output)
                                }
                            }

                            else -> {}

                        }
                        zip.closeEntry()
                        entry = zip.nextEntry
                    }
                }
            } ?: throw IOException("Unable to open backup file")

            val decodedJson = json?.let {
                Json.decodeFromString<BackupData>(it)
            } ?: throw IllegalStateException("Missing data.json")

            val books = decodedJson.books.map {
                if (it.book.cover.isNullOrEmpty()) {
                    it
                } else {
                    val savedImage = File(context.filesDir, it.book.cover)

                    val imagePath = if (savedImage.exists()) {
                        savedImage.absoluteFile.toString()
                    } else {
                        null
                    }

                    it.copy(
                        book = it.book.copy(
                            cover = imagePath,
                        )
                    )
                }
            }

            tagsRepository.addTags(decodedJson.tags)
            bookRepository.addBooksWithTags(books)

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Import successful", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Import failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun export(uri: Uri) = withContext(Dispatchers.IO) {
        try {
            val originalBooks = bookRepository.getAllBooks()
            val originalTags = tagsRepository.getAllTags()

            val booksForJson = originalBooks.map {
                if (it.book.cover.isNullOrEmpty()) {
                    it
                } else {
                    val file = File(it.book.cover)

                    it.copy(
                        book = it.book.copy(
                            cover = file.name
                        )
                    )
                }
            }

            val jsonBackup = Json.encodeToString(
                BackupData(
                    version = 1,
                    books = booksForJson,
                    tags = originalTags,
                )
            )

            val outputStream = context.contentResolver.openOutputStream(uri)
                ?: throw IOException("Unable to open backup destination")

            outputStream.use { stream ->
                ZipOutputStream(stream).use { zip ->

                    zip.putNextEntry(ZipEntry("data.json"))
                    zip.write(jsonBackup.toByteArray(Charsets.UTF_8))
                    zip.closeEntry()

                    zip.putNextEntry(ZipEntry("covers/"))
                    zip.closeEntry()

                    originalBooks.forEach { book ->
                        val path = book.book.cover

                        if (!path.isNullOrEmpty()) {
                            val file = File(path)

                            if (file.isFile) {
                                zip.putNextEntry(
                                    ZipEntry("covers/${file.name}")
                                )

                                file.inputStream().use { input ->
                                    input.copyTo(zip)
                                }

                                zip.closeEntry()
                            }
                        }
                    }
                }
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Export successful", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Export failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}