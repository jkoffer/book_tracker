package com.jk.book_tracker.features.home.book_list

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.FilterAltOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jk.book_tracker.features.filters.FilterViewModel
import com.jk.book_tracker.features.home.BookViewModel

@Composable
fun SharedTransitionScope.BookList(
    animatedVisibilityScope: AnimatedVisibilityScope,
    bookViewModel: BookViewModel = hiltViewModel(),
    filterViewModel: FilterViewModel = hiltViewModel(),
    navController: NavController,
    isGridView: Boolean,
    header: @Composable () -> Unit = {},
) {
    val booksWithTags by bookViewModel.booksWithTags.collectAsStateWithLifecycle()
    val activeFilters by filterViewModel.filterState.collectAsStateWithLifecycle()

    val filteredBooks = remember(booksWithTags, activeFilters) {
        booksWithTags
            .filter { item ->
                val matchesStatuses = activeFilters.selectedStatuses.isEmpty() ||
                        activeFilters.selectedStatuses.contains(item.book.readingStatus)

                val matchesTags = activeFilters.selectedTagIds.isEmpty() ||
                        item.tags.map { it.id }.containsAll(activeFilters.selectedTagIds)

                matchesStatuses && matchesTags
            }
            .sortedWith(activeFilters.sortBy.comparator)
    }

    if (!isGridView) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    header()
                }
            }

            if (booksWithTags.isEmpty()) {
                item {
                    EmptyState(
                        icon = Icons.AutoMirrored.Outlined.MenuBook,
                        title = "No books yet",
                        subtitle = "Tap the + button to add your first book."
                    )
                }
            } else if (filteredBooks.isEmpty()) {
                item {
                    EmptyState(
                        icon = Icons.Outlined.FilterAltOff,
                        title = "No matching books",
                        subtitle = "Try adjusting or clearing your filters."
                    )
                }
            }

            items(filteredBooks, key = { it.book.id }) { bookWithTags ->
                BookTile(
                    animatedVisibilityScope = animatedVisibilityScope,
                    navController = navController,
                    bookWithTags = bookWithTags,
                )
            }
        }
    } else {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize(),
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    header()
                }
            }

            if (booksWithTags.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    EmptyState(
                        icon = Icons.AutoMirrored.Outlined.MenuBook,
                        title = "No books yet",
                        subtitle = "Tap the + button to add your first book."
                    )
                }
            } else if (filteredBooks.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    EmptyState(
                        icon = Icons.Outlined.FilterAltOff,
                        title = "No matching books",
                        subtitle = "Try adjusting or clearing your filters."
                    )
                }
            }

            items(filteredBooks, key = { it.book.id }) { bookWithTags ->
                BookGridTile(
                    animatedVisibilityScope = animatedVisibilityScope,
                    navController = navController,
                    bookWithTags = bookWithTags,
                )
            }
        }
    }
}

@Composable
private fun EmptyState(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}


