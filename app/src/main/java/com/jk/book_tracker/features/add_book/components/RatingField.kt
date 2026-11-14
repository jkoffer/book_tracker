package com.jk.book_tracker.features.add_book.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun RatingField(
    value: Int?,
    onValueChange: (Int?) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        for (rating in 1..5) {
            RatingStar(
                isActive = (value ?: 0) >= rating,
                onClick = { onValueChange(if (value == rating) null else rating) }
            )
        }
    }

}

@Composable
private fun RatingStar(
    isActive: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Rate",
            tint = if (isActive) Color(0xFFFCC94F) else Color.Gray
        )
    }
}