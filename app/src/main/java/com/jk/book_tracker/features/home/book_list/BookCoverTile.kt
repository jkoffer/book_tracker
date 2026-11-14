package com.jk.book_tracker.features.home.book_list

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.platform.LocalContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HideImage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage


@Composable
fun BookCoverTile(
    modifier: Modifier = Modifier,
    cover: String?,
    title: String? = null,
    height: Int = 120,
    width: Int = 90,
) {
    if (cover == null) {
        Box(
            modifier = modifier
                .height(height.dp)
                .width(width.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.onSurfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    Icons.Outlined.HideImage,
                    contentDescription = "No image",
                    tint = MaterialTheme.colorScheme.outlineVariant
                )

                if (title != null) {
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        title,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            }

        }
    } else {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(cover)
                .crossfade(false)
                .build(),
            contentDescription = "Book cover",
            modifier = modifier
                .height(height.dp)
                .width(width.dp)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.FillBounds,
        )
    }
}

@Composable
fun SharedTransitionScope.BookCoverTileWithTransition(
    cover: String?,
    title: String? = null,
    animatedVisibilityScope: AnimatedVisibilityScope,
    key: String,
    height: Int = 120,
    width: Int = 90,
) {
    BookCoverTile(
        cover = cover,
        title = title,
        modifier = Modifier.sharedElement(
            sharedContentState = rememberSharedContentState(key = key),
            animatedVisibilityScope = animatedVisibilityScope,
            boundsTransform = { _, _ ->
                tween(durationMillis = 400)
            }
        ),
        height = height,
        width = width,
    )
}



