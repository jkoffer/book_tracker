package com.jk.book_tracker.features.add_book.tags

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jk.book_tracker.data.local.database.entity.TagEntity

@Composable
fun AddTagScreen(
    tag: TagEntity? = null,
    onDismiss: (tag: TagEntity?) -> Unit,
    viewModel: TagViewModel = hiltViewModel()
) {
    val titleController = rememberTextFieldState()

    LaunchedEffect(Unit) {
        tag?.let {
            titleController.edit {
                replace(0, tag.title.length, tag.title)
            }
        }
    }

    fun onSubmit() {
        val newTag = TagEntity(
            title = titleController.text.toString()
        )

        viewModel.addTag(newTag)

        onDismiss(newTag)
    }

    fun onDelete(tagId: String) {
        viewModel.deleteTag(tagId)

        onDismiss(null)
    }

    AddTagScreenContent(
        isEditMode = tag != null,
        titleController = titleController,
        onDismiss = { onDismiss(null) },
        onSubmit = { onSubmit() },
        onDelete = { tag?.let { onDelete(it.id) } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTagScreenContent(
    isEditMode: Boolean = false,
    titleController: TextFieldState,
    onDismiss: (tag: TagEntity?) -> Unit,
    onSubmit: () -> Unit,
    onDelete: () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = { onDismiss(null) },
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (!isEditMode) "Add new tag" else "Edit tag",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )

                Box(modifier = Modifier.height(16.dp))

                TextField(
                    state = titleController,
                    label = { Text("Tag title") }
                )

                Box(modifier = Modifier.height(16.dp))

                Row() {
                    if (isEditMode)
                        FilledTonalButton(
                            modifier = Modifier.height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.errorContainer,
                            ),
                            onClick = {
                                onDelete()
                            }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete tag",
                            )
                        }

                    Spacer(modifier = Modifier.width(6.dp))

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        onClick = { onSubmit() }
                    ) {
                        Text("Submit")
                    }
                }


            }
        }
    }
}

@Composable
@Preview
fun AddTagScreenPreview() {
    AddTagScreenContent(
        titleController = rememberTextFieldState(),
        onDismiss = {},
        onSubmit = {},
        onDelete = {},
    )
}