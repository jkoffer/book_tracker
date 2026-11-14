package com.jk.book_tracker.features.home.book_list

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.jk.book_tracker.data.local.database.relation.BookWithTags

@Composable
fun SharedTransitionScope.BookGridTile(
    animatedVisibilityScope: AnimatedVisibilityScope,
    navController: NavController,
    bookWithTags: BookWithTags,
    modifier: Modifier = Modifier,
) {
    val book = bookWithTags.book

    Box(
        modifier = modifier
            .clickable(
                onClick = {
                    navController.navigate("book-details/${book.id}")
                }
            )
    ) {
        BookCoverTileWithTransition(
            cover = book.cover,
            title = book.title,
            animatedVisibilityScope = animatedVisibilityScope,
            key = "cover-${book.id}",
            height = 140,
            width = 110
        )
    }
}
