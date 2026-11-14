package com.jk.book_tracker.features.add_book.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jk.book_tracker.domain.ReadingStatus

@Composable
fun StatusField(
    value: ReadingStatus,
    onValueChange: (ReadingStatus) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilterChip(
            shape = RoundedCornerShape(16.dp),
            label = { Text("To read") },
            selected = value == ReadingStatus.WANT_TO_READ,
            onClick = { onValueChange(ReadingStatus.WANT_TO_READ) },
        )

        FilterChip(
            shape = RoundedCornerShape(16.dp),
            label = { Text("Reading") },
            selected = value == ReadingStatus.READING,
            onClick = { onValueChange(ReadingStatus.READING) },
        )

        FilterChip(
            shape = RoundedCornerShape(16.dp),
            label = { Text("Finished") },
            selected = value == ReadingStatus.FINISHED,
            onClick = { onValueChange(ReadingStatus.FINISHED) },
        )

        FilterChip(
            shape = RoundedCornerShape(16.dp),
            label = { Text("Dropped") },
            selected = value == ReadingStatus.DROPPED,
            onClick = { onValueChange(ReadingStatus.DROPPED) },
        )
    }
}

@Composable
@Preview
fun StatusFieldPreview() {
    StatusField(
        value = ReadingStatus.READING,
    ) { }
}