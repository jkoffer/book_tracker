package com.jk.book_tracker.features.search_books_online

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jk.book_tracker.features.home.book_list.BookCoverTile

@Composable
fun SearchBookTile(
    cover: String?,
    title: String,
    author: String?,
    onAdd: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .clickable { onAdd() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BookCoverTile(
            cover = cover,
        )

        Spacer(Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .height(120.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    //Text(id)

                    Text(
                        title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        text = author ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(
                        modifier = Modifier.align(Alignment.End),
                        onClick = { onAdd() }
                    ) {
                        Text("Add")
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Preview
@Composable
fun SearchBookTilePreview() {
    SearchBookTile(
        cover = null,
        title = "The Lord of the Rings",
        author = "J. R. R. Tolkien",
        onAdd = {}
    )
}