package com.jk.book_tracker.features.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jk.book_tracker.data.local.database.entity.BookEntity
import com.jk.book_tracker.data.local.database.relation.BookWithTags
import com.jk.book_tracker.features.home.book_list.BookCoverTileWithTransition
import com.jk.book_tracker.features.home.BookViewModel
import com.jk.book_tracker.utils.popBackStackIfCurrent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.BookDetailsScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    bookId: String?,
    navController: NavController,
    bookViewModel: BookViewModel = hiltViewModel()
) {
    var book by remember { mutableStateOf<BookWithTags?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(bookId) {
        if (bookId != null) {
            isLoading = true
            book = bookViewModel.getBookById(bookId)
            isLoading = false
        }
    }

    if (isLoading) {
        CircularProgressIndicator()
        return
    }

    if (book == null) {
        Text("No book")
        return
    }

    BookDetailsContent(
        animatedVisibilityScope = animatedVisibilityScope,
        book = book!!,
        navController = navController,
        onDelete = {
            bookViewModel.deleteBook(book!!.book.id)

            navController.popBackStackIfCurrent("book-details")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SharedTransitionScope.BookDetailsContent(
    animatedVisibilityScope: AnimatedVisibilityScope,
    book: BookWithTags,
    navController: NavController,
    onDelete: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStackIfCurrent("book-details")
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                    }
                },
                title = { Text("Book details") },
                actions = {
                    BookDetailsMenuButton(
                        onDelete = { onDelete() },
                        navController = navController,
                        bookId = book.book.id,
                        bookTitle = book.book.title,
                    )
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                BookCoverTileWithTransition(
                    cover = book.book.cover,
                    animatedVisibilityScope = animatedVisibilityScope,
                    key = "cover-${book.book.id}",
                    height = 180,
                    width = 130
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = book.book.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                if (!book.book.subtitle.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = book.book.subtitle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }

                if (!book.book.author.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = book.book.author,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Basic Info Section
                if (!book.book.description.isNullOrBlank()) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        SectionHeader("Description")
                        Spacer(modifier = Modifier.height(4.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = book.book.description,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                // Reading Info Section
                Column(modifier = Modifier.fillMaxWidth()) {
                    SectionHeader("Reading info")
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Status",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                ) {
                                    Text(
                                        text = book.book.readingStatus.displayName,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(
                                            horizontal = 10.dp,
                                            vertical = 4.dp
                                        )
                                    )
                                }
                            }

                            if (book.book.rating != null) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "Rating",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Row {
                                        for (rating in 1..5) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = if (rating <= book.book.rating) Color(
                                                    0xFFFCC94F
                                                ) else Color.Gray,
                                                modifier = Modifier.padding(horizontal = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            if (book.tags.isNotEmpty()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "Tags",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = book.tags.joinToString(", ") { it.title },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            if (!book.book.notes.isNullOrBlank()) {
                                Column {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        "Notes",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = book.book.notes,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Edition Info Section
                if (!book.book.publisher.isNullOrBlank() || !book.book.isbn.isNullOrBlank() || book.book.pageCount != null) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        SectionHeader("Edition info")
                        Spacer(modifier = Modifier.height(4.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                if (!book.book.publisher.isNullOrBlank()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "Publisher",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            book.book.publisher,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                if (!book.book.isbn.isNullOrBlank()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "ISBN",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            book.book.isbn,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                if (book.book.pageCount != null) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "Pages",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            book.book.pageCount.toString(),
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    )
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
    )
}

@Composable
@Preview
private fun BookDetailsPreview() {
    SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
            BookDetailsContent(
                onDelete = {},
                animatedVisibilityScope = this,
                book = BookWithTags(
                    book = BookEntity(
                        title = "Cesarz wszech chorób",
                        subtitle = "Opowieść o raku",
                        author = "Siddhartha Mukherjee",
                        description = "Biografia raka, od jego pierwszych opisów w starożytnym Egipcie po współczesne próby jego pokonania.",
                        rating = 4,
                        publisher = "Wydawnictwo Czarne",
                        isbn = "9788375365184",
                        pageCount = 600,
                        notes = "Very insightful read."
                    ),
                    tags = emptyList()
                ),
                navController = rememberNavController()
            )
        }
    }
}
