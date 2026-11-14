package com.jk.book_tracker.features.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun MenuButton(
    isExpanded: Boolean,
    onDismiss: () -> Unit,
    onSortSelect: () -> Unit,
    onFilterSelect: () -> Unit,
    isGridView: Boolean,
    onViewSelect: (Boolean) -> Unit
) {
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { onDismiss() }
    ) {
        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    Icons.Default.SortByAlpha,
                    contentDescription = "Sort"
                )
            },
            text = { Text("Sort") },
            onClick = {
                onSortSelect()
                onDismiss()
            }
        )

        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    Icons.Default.FilterList,
                    contentDescription = "Filter"
                )
            },
            text = { Text("Filter") },
            onClick = {
                onFilterSelect()
                onDismiss()
            }
        )

        HorizontalDivider()

        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    Icons.Default.GridView,
                    tint = if(isGridView) Color.Gray else LocalContentColor.current,
                    contentDescription = "Grid view"
                )
            },
            text = { Text("Grid", color = if (isGridView) Color.Gray else Color.Unspecified) },
            onClick = {
                onViewSelect(true)
                onDismiss()
            }
        )

        DropdownMenuItem(
            leadingIcon = {
                Icon(
                    Icons.AutoMirrored.Filled.ViewList,
                    tint = if(!isGridView) Color.Gray else LocalContentColor.current,
                    contentDescription = "List view"
                )
            },
            text = { Text("List", color = if (!isGridView) Color.Gray else Color.Unspecified) },
            onClick = {
                onViewSelect(false)
                onDismiss()
            }
        )
    }
}