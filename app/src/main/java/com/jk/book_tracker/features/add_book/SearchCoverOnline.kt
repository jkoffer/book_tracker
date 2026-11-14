package com.jk.book_tracker.features.add_book

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.jk.book_tracker.features.home.book_list.BookCoverTile
import com.jk.book_tracker.features.search_books_online.SearchOnlineViewModel
import com.jk.book_tracker.utils.popBackStackIfCurrent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCoverOnline(
    navController: NavController,
    searchOnlineViewModel: SearchOnlineViewModel = hiltViewModel(),
) {
    val searchedCovers by searchOnlineViewModel.searchResults.collectAsStateWithLifecycle()
    val isLoading by searchOnlineViewModel.isLoading.collectAsStateWithLifecycle()
    val hasSearched by searchOnlineViewModel.hasSearched.collectAsStateWithLifecycle()

    fun onSelect(selectedCover: String?) {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.set("cover", selectedCover)

        navController.popBackStack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStackIfCurrent("search-cover-online")
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
                        placeholder = "Search cover online...",
                        onSearch = { query ->
                            searchOnlineViewModel.searchCovers(query)
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(top = 30.dp)
                        )
                    }

                    if (hasSearched && searchedCovers.isEmpty()) {
                        Text("No covers found.")
                    }

                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxSize(),
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(searchedCovers) { book ->
                            Box(
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                            onSelect(book.cover)
                                        }
                                    )
                            ) {
                                BookCoverTile(
                                    cover = book.cover,
                                    height = 140,
                                    width = 110
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun SearchCoverOnlinePreview() {
    SearchCoverOnline(
        navController = rememberNavController()
    )
}