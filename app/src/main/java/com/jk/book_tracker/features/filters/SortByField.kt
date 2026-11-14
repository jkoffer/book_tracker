//package com.jk.book_tracker.features.filters
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.Sort
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.ExposedDropdownMenuAnchorType
//import androidx.compose.material3.ExposedDropdownMenuBox
//import androidx.compose.material3.ExposedDropdownMenuDefaults
//import androidx.compose.material3.FilterChip
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//
//enum class SortByOptions(val displayName: String) {
//    BY_TITLE("Title"),
//    BY_DATE_ADDED("Date added (ascending)"),
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SortByField() {
//    var expanded by remember { mutableStateOf(false) }
//    var selectedOption by remember { mutableStateOf(SortByOptions.BY_DATE_ADDED) }
//
//    ExposedDropdownMenuBox(
//        expanded = expanded,
//        onExpandedChange = { expanded = it },
//    ) {
//        FilterChip(
//            selected = false,
//            onClick = { expanded = !expanded },
//            label = {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Start,
//                    modifier = Modifier
//                        .padding(10.dp)
//                        .fillMaxWidth(),
//                ) {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.Sort,
//                        contentDescription = null,
//                    )
//
//                    Spacer(modifier = Modifier.width(8.dp))
//
//                    Text(selectedOption.displayName)
//
//                    Spacer(modifier = Modifier.weight(1f))
//
//                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
//                }
//            },
//            shape = RoundedCornerShape(16.dp),
//            modifier = Modifier
//                .fillMaxWidth()
//                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
//        )
//
//        ExposedDropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//        ) {
//            SortByOptions.entries.forEach { option ->
//                DropdownMenuItem(
//                    text = { Text(option.displayName) },
//                    onClick = {
//                        selectedOption = option
//                        expanded = false
//                    },
//                )
//            }
//        }
//    }
//}
