package com.jk.book_tracker.features.home.book_list

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jk.book_tracker.data.local.database.relation.BookWithTags

@Composable
fun BookTile(
    navController: NavController,
    bookWithTags: BookWithTags,
    modifier: Modifier = Modifier,
) {
    BookTileContent(
        navController = navController,
        bookWithTags = bookWithTags,
        modifier = modifier,
        coverContent = {
            BookCoverTile(cover = bookWithTags.book.cover)
        }
    )
}

@Composable
fun SharedTransitionScope.BookTile(
    animatedVisibilityScope: AnimatedVisibilityScope,
    navController: NavController,
    bookWithTags: BookWithTags,
    modifier: Modifier = Modifier,
) {
    val book = bookWithTags.book

    BookTileContent(
        navController = navController,
        bookWithTags = bookWithTags,
        modifier = modifier,
        coverContent = {
            BookCoverTileWithTransition(
                cover = book.cover,
                animatedVisibilityScope = animatedVisibilityScope,
                key = "cover-${book.id}"
            )
        }
    )
}

@Composable
private fun BookTileContent(
    navController: NavController,
    bookWithTags: BookWithTags,
    modifier: Modifier = Modifier,
    coverContent: @Composable () -> Unit,
) {
    val book = bookWithTags.book
    val tags = bookWithTags.tags

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .clickable {
                navController.navigate("book-details/${book.id}")
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        coverContent()

        Spacer(Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .height(120.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart,
            ) {
                Column {
                    Text(
                        book.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )

                    if (book.author != null) {
                        Text(
                            text = book.author,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        book.rating?.let {
                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "$it/5",
                                        style = MaterialTheme.typography.bodySmall,
                                    )

                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = "star",
                                        modifier = Modifier.size(10.dp),
                                    )

                                    Spacer(modifier = Modifier.width(2.dp))
                                }
                            }
                        }

                        item {
                            Surface(
                                shape = MaterialTheme.shapes.small,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                            ) {
                                Text(
                                    text = book.readingStatus.displayName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        items(tags, key = { it.id }) { tag ->
                            Surface(
                                shape = MaterialTheme.shapes.small,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                            ) {
                                Text(
                                    text = tag.title,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                }
            }

            HorizontalDivider()
        }
    }
}
