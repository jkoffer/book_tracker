package com.jk.book_tracker.features.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(
    placeholder: String = "Search...",
    readOnly: Boolean = false,
    focusRequester: FocusRequester? = FocusRequester(),
    initFocus: Boolean? = true,
    onClick: (() -> Unit)? = null,
    onValueChange: ((text: String) -> Unit)? = null,
    onSearch: ((query: String) -> Unit)? = null,
) {
    val focusRequester = remember { focusRequester }

    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState()

    LaunchedEffect(Unit) {
        if (initFocus == true) {
            focusRequester!!.requestFocus()
        }
    }

    // TODO: Disable if onValueChange is null?
    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text.toString() }
            .collect { text ->
                onValueChange?.invoke(text)
            }
    }

    val inputField = @Composable {
        SearchBarDefaults.InputField(
            textFieldState = textFieldState,
            searchBarState = searchBarState,
            textStyle = MaterialTheme.typography.bodyLarge,
            onSearch = { query ->
                onSearch?.invoke(query)
            },
            placeholder = {
                Text(placeholder)
            },
            readOnly = readOnly,
            modifier = Modifier.focusRequester(focusRequester!!),
        )
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        SearchBar(
            state = searchBarState,
            inputField = inputField
        )

        if (readOnly) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onClick?.invoke()
                    }
            )
        }
    }
}