package com.jk.book_tracker.features.common

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

// https://stackoverflow.com/a/69231996
@Composable
fun LockScreenOrientation(orientation: Int) {
    val activity = LocalActivity.current

    DisposableEffect(orientation) {
        val activity = activity ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation

        activity.requestedOrientation = orientation

        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
}