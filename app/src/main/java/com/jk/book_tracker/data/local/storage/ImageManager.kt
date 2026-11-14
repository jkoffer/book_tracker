package com.jk.book_tracker.data.local.storage

import android.content.Context
import android.util.Log
import com.jk.book_tracker.features.add_book.domain.ImageFileSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL

import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ImageManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun saveImageToInternalStorage(
        imageSource: ImageFileSource?
    ): String? = withContext(
        Dispatchers.IO
    ) {
        try {
            val imageName = "${System.currentTimeMillis()}.jpg"

            when (imageSource) {
                // https://stackoverflow.com/a/76587708
                is ImageFileSource.Local -> {
                    Log.d("uri", imageSource.uri.toString())
                    val inputStream = context.contentResolver.openInputStream(imageSource.uri)
                    val outputStream = context.openFileOutput(imageName, Context.MODE_PRIVATE)
                    inputStream?.use { input ->
                        outputStream.use { output ->
                            input.copyTo(output)
                        }
                    } ?: return@withContext null
                }

                is ImageFileSource.Remote -> {
                    val outputStream = context.openFileOutput(imageName, Context.MODE_PRIVATE)

                    URL(imageSource.url).openStream().use { input ->
                        outputStream.use { output ->
                            input.copyTo(output)
                        }
                    }
                }


                else -> {
                    return@withContext null
                }
            }

            File(context.filesDir, imageName).absoluteFile.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
