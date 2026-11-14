package com.jk.book_tracker.features.filters

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jk.book_tracker.features.filters.domain.SortByOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortByModal(
    onDismissRequest: () -> Unit,
    filterViewModel: FilterViewModel = hiltViewModel()
) {
    val filterState by filterViewModel.filterState.collectAsStateWithLifecycle()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
    ) {
        SortByOptions.entries.forEach {
            ListItem(
                headlineContent = {
                    Text(it.value)
                },
                trailingContent = {
                    if (filterState.sortBy == it) {
                        Icon(Icons.Default.Done, contentDescription = null)
                    }
                },
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .clickable(
                        onClick = {
                            filterViewModel.selectSortBy(it)
                        }
                    )
            )
        }
    }
}

@Preview
@Composable
private fun SortByModalPreview() {
    SortByModal(
        onDismissRequest = {}
    )
}