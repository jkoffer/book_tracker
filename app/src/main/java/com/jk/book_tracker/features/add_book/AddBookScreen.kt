package com.jk.book_tracker.features.add_book

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jk.book_tracker.features.add_book.components.ImageField
import com.jk.book_tracker.features.add_book.components.RatingField
import com.jk.book_tracker.features.add_book.components.StatusField
import com.jk.book_tracker.features.add_book.components.TagField
import com.jk.book_tracker.features.add_book.domain.AddBookFormEvent
import com.jk.book_tracker.features.add_book.domain.AddBookFormState
import com.jk.book_tracker.features.add_book.domain.AddBookSource
import com.jk.book_tracker.features.add_book.domain.NoApiBookFound
import com.jk.book_tracker.features.common.ErrorSnackbarHost
import com.jk.book_tracker.utils.popBackStackIfCurrent

@Composable
fun AddBookScreen(
    source: AddBookSource?,
    navController: NavController,
    formViewModel: AddBookFormViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val state = formViewModel.state

    LaunchedEffect(source) {
        try {
            formViewModel.init(source)
        } catch (e: NoApiBookFound) {
            snackbarHostState.showSnackbar(message = e.message.toString())
        } catch (e: Exception) {
            Log.e("Something went wrong", e.toString())
            snackbarHostState.showSnackbar(message = "Something went wrong")
        }
    }

    fun submitForm() {
        formViewModel.submit(
            onSubmit = {
                navController.navigate("book-details/$it") {
                    popUpTo("add-book") {
                        inclusive = true
                    }
                }
            }
        )
    }

    AddBookContent(
        source = source,
        state = state,
        snackbarHostState = snackbarHostState,
        onPopBackStack = { navController.popBackStackIfCurrent("add-book") },
        onEvent = formViewModel::onEvent,
        onSubmit = { submitForm() },
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddBookContent(
    source: AddBookSource?,
    state: AddBookFormState,
    snackbarHostState: SnackbarHostState,
    onPopBackStack: () -> Unit,
    onEvent: (AddBookFormEvent) -> Unit = {},
    onSubmit: () -> Unit = {},
    navController: NavController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { onPopBackStack() }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { if (source == null) Text("Add book") else Text("Edit book") }
            )
        },
        snackbarHost = {
            ErrorSnackbarHost(snackbarHostState = snackbarHostState)
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 15.dp)
                ) {
                    ImageField(
                        value = state.cover.data,
                        onValueChange = {
                            onEvent(AddBookFormEvent.OnCoverChanged(it))
                        },
                        navController = navController,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SectionHeader("Basic info")
                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedTextField(
                        value = state.title,
                        singleLine = true,
                        onValueChange = { onEvent(AddBookFormEvent.OnTitleChanged(it)) },
                        placeholder = { Text("Title") },
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = colorScheme.surfaceVariant,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = state.subtitle ?: "",
                        singleLine = true,
                        onValueChange = { onEvent(AddBookFormEvent.OnSubtitleChanged(it)) },
                        placeholder = { Text("Subtitle") },
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = colorScheme.surfaceVariant,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = state.author ?: "",
                        singleLine = true,
                        onValueChange = { onEvent(AddBookFormEvent.OnAuthorChanged(it)) },
                        placeholder = { Text("Author(s)") },
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = colorScheme.surfaceVariant,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = state.description ?: "",
                        singleLine = false,
                        onValueChange = { onEvent(AddBookFormEvent.OnDescriptionChanged(it)) },
                        placeholder = { Text("Description") },
                        minLines = 3,
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = colorScheme.surfaceVariant,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    SectionHeader("Reading info")
                    Spacer(modifier = Modifier.height(4.dp))

                    StatusField(
                        value = state.readingStatus,
                        onValueChange = { onEvent(AddBookFormEvent.OnStatusChanged(it)) }
                    )

                    RatingField(
                        value = state.rating,
                        onValueChange = { onEvent(AddBookFormEvent.OnRatingChanged(it)) }
                    )

                    TagField(
                        value = state.tags,
                        onValueChange = { onEvent(AddBookFormEvent.OnTagChanged(it)) }
                    )

                    OutlinedTextField(
                        value = state.notes ?: "",
                        onValueChange = { onEvent(AddBookFormEvent.OnNotesChanged(it)) },
                        placeholder = { Text("Notes") },
                        minLines = 3,
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = colorScheme.surfaceVariant,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    SectionHeader("Edition info")
                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("ISBN") },
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = colorScheme.surfaceVariant,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = state.publisher ?: "",
                        onValueChange = { onEvent(AddBookFormEvent.OnPublisherChanged(it)) },
                        placeholder = { Text("Publisher") },
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = colorScheme.surfaceVariant,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Publication date") },
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = colorScheme.surfaceVariant,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Book format") },
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = colorScheme.surfaceVariant,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    Box(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onSubmit() },
                        enabled = state.isFormValid && !state.isSubmitting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        if (state.isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = colorScheme.onPrimary
                            )
                        } else {
                            Text("Submit")
                        }
                    }

                    Box(modifier = Modifier.height(32.dp))
                }
            }
        }
    )

}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = colorScheme.primary,
        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
    )
}


@Preview
@Composable
fun AddBookScreenPreview() {
    AddBookContent(
        source = null,
        state = AddBookFormState(),
        snackbarHostState = SnackbarHostState(),
        onPopBackStack = {},
        navController = rememberNavController(),
    )
}


// SECTION 1 "Basic info"
// title, subtitle, author(s), description

// SECTION 2 "Reading info"
// Reading status (StatusField.kt), rating (RatingField.kt), tags (TagField.kt), notes

// SECTION 3 "Edition info"
// isbn, publisher, publication date, book format