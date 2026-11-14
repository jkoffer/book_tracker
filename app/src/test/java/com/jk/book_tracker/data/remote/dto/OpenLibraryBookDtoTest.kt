package com.jk.book_tracker.data.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OpenLibraryBookDtoTest {

    @Test
    fun `toDomain handles null lists gracefully without NullPointerException`() {
        val dto = OpenLibraryBookDto(
            title = "Test Book",
            subtitle = null,
            byStatement = null,
            authors = null,
            numberOfPages = 300,
            publishers = null,
            covers = null
        )

        val domainModel = dto.toDomain("9788381912174")

        assertEquals("Test Book", domainModel.book.title)
        assertNull(domainModel.book.cover)
        assertNull(domainModel.book.author)
        assertNull(domainModel.book.publisher)
        assertEquals(300, domainModel.book.pageCount)
        assertEquals("9788381912174", domainModel.book.isbn)
    }
}
