package com.jk.book_tracker.features.details

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.jk.book_tracker.features.common.DeleteDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsMenuButton(
    onDelete: () -> Unit,
    navController: NavController,
    bookId: String,
    bookTitle: String
) {
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        DeleteDialog(
            itemName = bookTitle,
            onConfirmDelete = {
                onDelete()
            },
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }

    IconButton(
        onClick = { expanded = !expanded }
    ) {
        Icon(Icons.Default.MoreHoriz, contentDescription = "More")
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {
            expanded = false
        }
    ) {
        DropdownMenuItem(
            text = { Text("Edit") },
            onClick = {
                expanded = false
                navController.navigate("add-book?bookId=${bookId}")
            }
        )
        DropdownMenuItem(
            text = { Text("Delete") },
            onClick = {
                expanded = false
                showDeleteDialog = true
            }
        )
    }
}