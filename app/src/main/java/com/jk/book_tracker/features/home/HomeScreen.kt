package com.jk.book_tracker.features.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jk.book_tracker.features.filters.FilterModal
import com.jk.book_tracker.features.filters.SortByModal
import com.jk.book_tracker.features.common.MySearchBar
import com.jk.book_tracker.features.force_update.ForceUpdateScreen
import com.jk.book_tracker.features.home.book_list.BookList

@Composable
fun SharedTransitionScope.HomeScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    navController: NavController
) {
    ForceUpdateScreen()

    var showAddBookDialog by remember { mutableStateOf(false) }

    if (showAddBookDialog) {
        AddBookMethodModal(
            navController,
            onDismissRequest = { showAddBookDialog = false }
        )
    }

    HomeScreenContent(
        onAddBook = { showAddBookDialog = true },
        animatedVisibilityScope = animatedVisibilityScope,
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SharedTransitionScope.HomeScreenContent(
    onAddBook: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navController: NavController,
) {
    var isGridView by rememberSaveable { mutableStateOf(false) }
    var showMoreDialog by remember { mutableStateOf(false) }
    var showSortModal by remember { mutableStateOf(false) }
    var showFilterModal by remember { mutableStateOf(false) }

    if (showSortModal) {
        SortByModal(
            onDismissRequest = { showSortModal = false }
        )
    }

    if (showFilterModal) {
        FilterModal(
            onDismissRequest = { showFilterModal = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Book tracker")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate("settings") {
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile and settings"
                        )
                    }
                },
                actions = {
                    Box {
                        IconButton(
                            onClick = { showMoreDialog = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreHoriz,
                                contentDescription = "More"
                            )
                        }

                        MenuButton(
                            isExpanded = showMoreDialog,
                            onDismiss = { showMoreDialog = false },
                            onSortSelect = { showSortModal = true },
                            onFilterSelect = { showFilterModal = true },
                            isGridView = isGridView,
                            onViewSelect = { isGridView = it }
                        )
                    }

                    IconButton(
                        onClick = { onAddBook() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "Add book"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 15.dp)
        ) {
            BookList(
                animatedVisibilityScope = animatedVisibilityScope,
                navController = navController,
                isGridView = isGridView,
                header = {
                    MySearchBar(
                        readOnly = true,
                        onClick = { navController.navigate("search") },
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun HomeScreenPreview() {
    val navController = rememberNavController()

    SharedTransitionLayout {
        AnimatedVisibility(
            visible = true
        ) {
            HomeScreenContent(
                onAddBook = {},
                animatedVisibilityScope = this,
                navController = navController
            )
        }
    }
}
