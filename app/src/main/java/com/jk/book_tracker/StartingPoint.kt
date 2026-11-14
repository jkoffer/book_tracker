package com.jk.book_tracker

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jk.book_tracker.features.add_book.AddBookScreen
import com.jk.book_tracker.features.add_book.domain.AddBookSource
import com.jk.book_tracker.features.add_book.SearchCoverOnline
import com.jk.book_tracker.features.details.BookDetailsScreen
import com.jk.book_tracker.features.home.BookViewModel
import com.jk.book_tracker.features.home.HomeScreen
import com.jk.book_tracker.features.scanner.ScannerScreen
import com.jk.book_tracker.features.search_books_online.SearchOnlineScreen
import com.jk.book_tracker.features.home.SearchScreen
import com.jk.book_tracker.features.settings.SettingsScreen

@Composable
fun StartingPoint() {
    SharedTransitionLayout {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            composable("home") {
                HomeScreen(
                    animatedVisibilityScope = this,
                    navController = navController
                )
            }
            composable("search") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("home")
                }
                val bookViewModel: BookViewModel = hiltViewModel(parentEntry)

                SearchScreen(navController, bookViewModel)
            }
            composable("settings") {
                SettingsScreen(
                    navController = navController,
                )
            }
            composable("book-details/{bookId}") { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId")

                BookDetailsScreen(this, bookId, navController)
            }
            composable(
                "add-book?bookId={bookId}&isbn={isbn}&openLibId={openLibId}",
                arguments = listOf(
                    navArgument("bookId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                    navArgument("isbn") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                    navArgument("openLibId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId")
                val isbn = backStackEntry.arguments?.getString("isbn")
                val openLibId = backStackEntry.arguments?.getString("openLibId")

                val source = when {
                    !bookId.isNullOrEmpty() -> AddBookSource.Local(bookId)
                    !isbn.isNullOrEmpty() -> AddBookSource.RemoteIsbn(isbn)
                    !openLibId.isNullOrEmpty() -> AddBookSource.RemoteOpenLibId(openLibId)
                    else -> null
                }

                AddBookScreen(source = source, navController = navController)
            }
            composable("scanner") {
                ScannerScreen(navController)
            }
            composable("search-online") {
                SearchOnlineScreen(navController)
            }
            composable("search-cover-online") {
                SearchCoverOnline(navController)
            }
        }
    }
}
