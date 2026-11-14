package com.jk.book_tracker.utils

import androidx.navigation.NavController
fun NavController.popBackStackIfCurrent(route: String? = null): Boolean {
    val currentRoute = currentBackStackEntry?.destination?.route
    val matchesRoute = route == null ||
            currentRoute == route ||
            currentRoute?.startsWith("$route/") == true ||
            currentRoute?.startsWith("$route?") == true
    val hasPreviousScreen = previousBackStackEntry != null

    return if (matchesRoute && hasPreviousScreen) {
        popBackStack()
    } else {
        false
    }
}
