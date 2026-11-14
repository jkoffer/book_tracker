package com.jk.book_tracker.features.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookMethodModal(
    navController: NavController,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
    ) {
        AddBookMethodModalContent(
            onAddBookManually = {
                onDismissRequest()

                navController.navigate("add-book")
            },
            onSearchOnline = {
                onDismissRequest()

                navController.navigate("search-online")
            },
            onScanBarcode = {
                onDismissRequest()

                navController.navigate("scanner")
            },
        )
    }
}

@Composable
fun AddBookMethodModalContent(
    onAddBookManually: () -> Unit,
    onSearchOnline: () -> Unit,
    onScanBarcode: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
            .padding(horizontal = 15.dp)
    ) {
        AddBookMethodItem(
            title = "Scan Barcode",
            description = "Scan ISBN barcode using camera",
            icon = Icons.Default.DocumentScanner,
            onClick = onScanBarcode,
        )

        HorizontalDivider()

        AddBookMethodItem(
            title = "Search Online",
            description = "Find book by title, author, or ISBN",
            icon = Icons.Default.Search,
            onClick = onSearchOnline,
        )

        HorizontalDivider()

        AddBookMethodItem(
            title = "Add Manually",
            description = "Enter book details manually",
            icon = Icons.Default.Keyboard,
            onClick = onAddBookManually,
        )
    }
}

@Composable
private fun AddBookMethodItem(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineContent = {
            Text(title)
        },
        supportingContent = {
            Text(description)
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent,
        ),
        modifier = modifier.clickable(onClick = onClick),
    )
}

@Preview(showBackground = true)
@Composable
private fun AddBookMethodModalPreview() {
    AddBookMethodModalContent(
        onAddBookManually = {},
        onSearchOnline = {},
        onScanBarcode = {},
    )
}

