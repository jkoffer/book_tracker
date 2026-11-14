package com.jk.book_tracker.data.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class OpenLibrarySearchBookDtoTest {

    @Test
    fun `toDomain maps docs to BookEntity list correctly`() {
        val dto = OpenLibrarySearchBookDto(
            docs = listOf(
                Doc(
                    key = "/works/OL12345W",
                    title = "The Hobbit",
                    subtitle = "There and Back Again",
                    authorName = listOf("J. R. R. Tolkien"),
                    coverI = 123456L
                ),
                Doc(
                    key = "/works/OL67890W",
                    title = "The Fellowship of the Ring",
                    authorName = listOf("J. R. R. Tolkien"),
                    coverEditionKey = "OL9876M"
                )
            )
        )

        val result = dto.toDomain()

        assertEquals(2, result.size)

        val book1 = result[0]
        assertEquals("/works/OL12345W", book1.id)
        assertEquals("The Hobbit", book1.title)
        assertEquals("There and Back Again", book1.subtitle)
        assertEquals("J. R. R. Tolkien", book1.author)
        assertEquals("https://covers.openlibrary.org/b/id/123456-L.jpg", book1.cover)

        val book2 = result[1]
        assertEquals("/works/OL67890W", book2.id)
        assertEquals("The Fellowship of the Ring", book2.title)
        assertNull(book2.subtitle)
        assertEquals("J. R. R. Tolkien", book2.author)
        assertEquals("https://covers.openlibrary.org/b/olid/OL9876M-L.jpg", book2.cover)
    }

    @Test
    fun `toDomain handles null docs gracefully`() {
        val dto = OpenLibrarySearchBookDto(docs = null)

        val result = dto.toDomain()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `toDomain handles doc with null fields gracefully`() {
        val dto = OpenLibrarySearchBookDto(
            docs = listOf(
                Doc(
                    key = null,
                    title = null,
                    subtitle = null,
                    authorName = null,
                    coverI = null,
                    coverEditionKey = null
                )
            )
        )

        val result = dto.toDomain()

        assertEquals(1, result.size)

        val book = result[0]
        assertNotNull(book.id)
        assertEquals("", book.title)
        assertNull(book.subtitle)
        assertNull(book.author)
        assertNull(book.cover)
    }
}
