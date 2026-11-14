package com.jk.book_tracker.features.add_book.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType.Companion.PrimaryEditable
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jk.book_tracker.data.local.database.entity.TagEntity
import com.jk.book_tracker.features.add_book.tags.AddTagScreen
import com.jk.book_tracker.features.add_book.tags.TagViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagField(
    value: List<TagEntity>,
    onValueChange: (List<TagEntity>) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    fun onTagSelect(tag: TagEntity) {
        if (value.contains(tag)) {
            onValueChange(value - tag)
        } else {
            onValueChange(value + tag)
        }
    }

    if (showDialog) {
        AddTagScreen(
            onDismiss = { newTag ->
                if (newTag != null) {
                    onValueChange(value + newTag)
                }
                showDialog = false
            }
        )
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = value.joinToString(", ") { it.title },
            onValueChange = {},
            readOnly = true,
            label = { Text("Tags") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = colorScheme.surfaceVariant,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .menuAnchor(PrimaryEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            TagsList(
                selectedTags = value,
                onTagSelect = { onTagSelect(it) },
            )

            if (value.isNotEmpty()) HorizontalDivider()

            DropdownMenuItem(
                text = { Text("Add new tag") },
                onClick = {
                    showDialog = true
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun TagsList(
    selectedTags: List<TagEntity>,
    onTagSelect: (TagEntity) -> Unit,
    viewModel: TagViewModel = hiltViewModel()
) {
    var editingTag by remember { mutableStateOf<TagEntity?>(null) }

    val tags by viewModel.tags.collectAsStateWithLifecycle()

    if (editingTag != null) {
        AddTagScreen(
            tag = editingTag,
            onDismiss = {
                editingTag = null
            }
        )
    }

    // TODO P0: System might be in dark mode, but app in light mode.
    val gray = if (isSystemInDarkTheme()) {
        Color(0xFF606060)
    } else {
        Color(0xFFDDDDDD)
    }

    tags.forEach {
        DropdownMenuItem(
            leadingIcon = {
                if (selectedTags.contains(it)) {
                    Icon(Icons.Default.Done, contentDescription = "Selected tag")
                }
            },
            text = { Text(it.title) },
            trailingIcon = {
                IconButton(
                    onClick = { editingTag = it }
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit tag",
                        tint = gray,
                    )
                }
            },
            onClick = {
                onTagSelect(it)
            }
        )
    }
}