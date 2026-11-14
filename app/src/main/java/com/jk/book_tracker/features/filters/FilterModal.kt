package com.jk.book_tracker.features.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jk.book_tracker.data.local.database.entity.TagEntity
import com.jk.book_tracker.domain.ReadingStatus
import com.jk.book_tracker.features.add_book.tags.TagViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterModal(
    onDismissRequest: () -> Unit,
    filterViewModel: FilterViewModel = hiltViewModel(),
    tagViewModel: TagViewModel = hiltViewModel()
) {
    val filters by filterViewModel.filterState.collectAsStateWithLifecycle()
    val tags by tagViewModel.tags.collectAsStateWithLifecycle()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = Modifier.fillMaxHeight(),
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
    ) {
        FilterModalContent(
            tags = tags,
            selectedStatuses = filters.selectedStatuses,
            onStatusToggle = { status -> filterViewModel.toggleReadingStatus(status) },
            selectedTagIds = filters.selectedTagIds,
            onTagToggle = { tagId -> filterViewModel.toggleTag(tagId) },
            onClearClick = { filterViewModel.clearFilters() },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterModalContent(
    tags: List<TagEntity>,
    selectedStatuses: Set<ReadingStatus>,
    onStatusToggle: (ReadingStatus) -> Unit,
    selectedTagIds: Set<String>,
    onTagToggle: (String) -> Unit,
    onClearClick: () -> Unit,
) {

    CompositionLocalProvider(
        LocalMinimumInteractiveComponentSize provides 0.dp
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .padding(horizontal = 15.dp),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Reading status",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                )
            }

            items(ReadingStatus.entries.filter { it != ReadingStatus.WISHLIST }) { status ->
                FilterTagChip(
                    title = status.displayName,
                    icon = getReadingStatusIcon(status),
                    isSelected = selectedStatuses.contains(status),
                    onClick = { onStatusToggle(status) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "Tags",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                )
            }

            if (tags.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = "No tags to display.",
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            } else {
                items(tags, key = { it.id }) { tag ->
                    FilterTagChip(
                        title = tag.title,
                        icon = Icons.Default.Tag,
                        isSelected = selectedTagIds.contains(tag.id),
                        onClick = { onTagToggle(tag.id) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                TextButton(
                    onClick = onClearClick
                ) {
                    Text("Clear filters", textAlign = TextAlign.Center)
                }
            }
        }
    }
}

private fun getReadingStatusIcon(status: ReadingStatus): ImageVector {
    return when (status) {
        ReadingStatus.WANT_TO_READ -> Icons.Default.Bookmark
        ReadingStatus.READING -> Icons.AutoMirrored.Filled.MenuBook
        ReadingStatus.FINISHED -> Icons.Default.CheckCircle
        ReadingStatus.DROPPED -> Icons.Default.Block
        ReadingStatus.WISHLIST -> Icons.Default.Favorite
    }
}

@Composable
private fun FilterTagChip(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(title)
            }
        },
        selected = isSelected,
        onClick = { onClick() },
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterModalPreview() {
    FilterModalContent(
        tags = emptyList(),
        selectedStatuses = setOf(ReadingStatus.READING),
        onStatusToggle = {},
        selectedTagIds = emptySet(),
        onTagToggle = {},
        onClearClick = {},
    )
}
