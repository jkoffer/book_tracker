package com.jk.book_tracker.features.search_books_online

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jk.book_tracker.features.common.MySearchBar
import com.jk.book_tracker.utils.popBackStackIfCurrent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchOnlineScreen(
    navController: NavController,
    searchOnlineViewModel: SearchOnlineViewModel = hiltViewModel(),
) {
    val searchedBooks by searchOnlineViewModel.searchResults.collectAsStateWithLifecycle()
    val isLoading by searchOnlineViewModel.isLoading.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStackIfCurrent("search-online")
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text("Search online") }
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 15.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    MySearchBar(
                        placeholder = "Search online...",
                        onSearch = { query ->
                            searchOnlineViewModel.search(query)
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(top = 30.dp)
                        )
                    }

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        items(searchedBooks) { book ->
                            SearchBookTile(
                                cover = book.cover,
                                title = book.title,
                                author = book.author,
                                onAdd = {
                                    navController.navigate("add-book?openLibId=${book.openLibKey}")
                                },
                            )
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun SearchOnlineScreenPreview() {
    SearchOnlineScreen(
        navController = rememberNavController()
    )
}