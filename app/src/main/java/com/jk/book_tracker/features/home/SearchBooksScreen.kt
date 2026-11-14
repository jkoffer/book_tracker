package com.jk.book_tracker.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jk.book_tracker.data.local.database.relation.BookWithTags
import com.jk.book_tracker.features.common.MySearchBar
import com.jk.book_tracker.features.home.book_list.BookTile
import com.jk.book_tracker.utils.popBackStackIfCurrent

@Composable
fun SearchScreen(
    navController: NavController,
    bookViewModel: BookViewModel,
) {
    val books by bookViewModel.booksWithTags.collectAsStateWithLifecycle()

    SearchScreenContent(
        books = books,
        navController = navController,
        onPopBackStack = { navController.popBackStackIfCurrent("search") }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SearchScreenContent(
    books: List<BookWithTags>,
    navController: NavController,
    onPopBackStack: () -> Unit,
) {
    var searchedBooks by remember { mutableStateOf<List<BookWithTags>>(emptyList()) }

    fun searchBooks(query: String) {
        searchedBooks = if (query.isBlank()) {
            emptyList()
        } else {
            books.filter {
                val matchesTitle = it.book.title.contains(query, ignoreCase = true)
                val matchesSubtitle = it.book.subtitle?.contains(query, ignoreCase = true) ?: false
                val matchesAuthors = it.book.author?.contains(query, ignoreCase = true) ?: false

                matchesTitle || matchesSubtitle || matchesAuthors
            }
        }
    }

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
                title = {
                    MySearchBar(
                        onValueChange = { text ->
                            searchBooks(text)
                        }
                    )
                },
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(top = 10.dp)
                    .padding(horizontal = 15.dp)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    items(searchedBooks) { book ->
                        BookTile(
                            navController = navController,
                            bookWithTags = book,
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreenContent(
        books = emptyList(),
        navController = rememberNavController(),
        onPopBackStack = { }
    )
}