package com.jk.book_tracker.features.add_book.components

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.jk.book_tracker.features.add_book.domain.ImageFileSource
import com.jk.book_tracker.features.add_book.domain.ImageState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageField(
    value: ImageFileSource?,
    onValueChange: (ImageState) -> Unit,
    navController: NavController
) {
    var expanded by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            onValueChange(ImageState.New(ImageFileSource.Local(it)))
        }
    }

    val savedStateHandle = navController.currentBackStackEntry
        ?.savedStateHandle

    LaunchedEffect(savedStateHandle) {
        val cover = savedStateHandle?.get<String>("cover")

        if (cover != null) {
            onValueChange(ImageState.New(ImageFileSource.Remote(cover)))

            savedStateHandle["cover"] = null
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        if (value == null) {
            Card(
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                modifier = Modifier
                    .height(120.dp)
                    .width(100.dp)
                    .clickable(
                        onClick = { expanded = true }
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Photo,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        contentDescription = "Add photo"
                    )

                    Box(modifier = Modifier.height(6.dp))

                    Text(
                        "Add cover\nimage",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            AsyncImage(
                model = when (value) {
                    is ImageFileSource.Local -> value.uri
                    is ImageFileSource.Remote -> value.url
                },
                contentDescription = "Book cover",
                modifier = Modifier
                    .height(120.dp)
                    .width(90.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.FillBounds,
                onError = {
                    Log.e("BookCover", "Failed to load image", it.result.throwable)
                }
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("From gallery") },
                onClick = {
                    launcher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
            )

            DropdownMenuItem(
                text = { Text("Search online") },
                onClick = {
                    expanded = false
                    navController.navigate("search-cover-online")
                }
            )
        }
    }
}