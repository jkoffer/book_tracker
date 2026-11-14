package com.jk.book_tracker.features.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.jk.book_tracker.domain.ThemeMode
import com.jk.book_tracker.utils.popBackStackIfCurrent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.jk.book_tracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel()
) {
    var showThemeModeDialog by remember { mutableStateOf(false) }
    val themeMode by themeViewModel.themeMode.collectAsState()

    val openLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            settingsViewModel.importData(uri)
        }
    }

    val saveLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip")
    ) { uri ->
        uri?.let {
            settingsViewModel.exportData(uri)
        }
    }

    if (showThemeModeDialog) {
        ThemeModeDialog(
            onValueSelect = { themeMode ->
                themeViewModel.setTheme(themeMode)
            },
            value = themeMode,
            onDismissRequest = { showThemeModeDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStackIfCurrent("settings")
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { }
            )
        },
        content = { innerPadding ->
            SettingsContent(
                innerPadding = innerPadding,
                currentThemeMode = themeMode,
                onThemeClick = { showThemeModeDialog = true },
                onImport = {
                    openLauncher.launch(arrayOf("application/zip"))
                },
                onExport = {
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    val current = LocalDateTime.now().format(formatter)

                    // TODO P0: Change file name
                    saveLauncher.launch("backup_${current}.zip")
                }
            )
        }
    )
}

@Composable
private fun SettingsContent(
    innerPadding: PaddingValues,
    currentThemeMode: ThemeMode,
    onThemeClick: () -> Unit,
    onImport: () -> Unit,
    onExport: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 15.dp),
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Settings",
                fontSize = 28.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text("Appearance & App")
            Spacer(modifier = Modifier.height(8.dp))

            ListItem(
                leadingContent = {
                    Icon(Icons.Outlined.DarkMode, contentDescription = "Dark mode")
                },
                headlineContent = {
                    Text("Theme mode")
                },
                supportingContent = {
                    val themeText = when (currentThemeMode) {
                        ThemeMode.LIGHT -> "Light"
                        ThemeMode.DARK -> "Dark"
                        ThemeMode.SYSTEM -> "System"
                    }
                    Text(themeText)
                },
                modifier = Modifier.clickable(
                    onClick = onThemeClick
                )
            )

            ListItem(
                leadingContent = {
                    Icon(painterResource(R.drawable.file_down), contentDescription = "Import")
                },
                headlineContent = {
                    Text("Import")
                },
                modifier = Modifier.clickable(
                    onClick = {
                        onImport()
                    }
                )
            )

            ListItem(
                leadingContent = {
                    Icon(painterResource(R.drawable.file_up), contentDescription = "Export")
                },
                headlineContent = {
                    Text("Export")
                },
                modifier = Modifier.clickable(
                    onClick = {
                        onExport()
                    }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Support")
            Spacer(modifier = Modifier.height(8.dp))

            ListItem(
                leadingContent = {
                    Icon(Icons.Default.Feedback, contentDescription = "")
                },
                headlineContent = {
                    Text("Send feedback")
                },
                modifier = Modifier.clickable(
                    onClick = {
                        // TODO P0: Send feedback
                    }
                )
            )

            ListItem(
                leadingContent = {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "")
                },
                headlineContent = {
                    Text("Write a review")
                },
                modifier = Modifier.clickable(
                    onClick = {
                        // TODO P0: Write a review
                    }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("About")
            Spacer(modifier = Modifier.height(8.dp))

            ListItem(
                leadingContent = {
                    Icon(Icons.Default.Visibility, contentDescription = "")
                },
                headlineContent = {
                    Text("Privacy Policy")
                },
                modifier = Modifier.clickable(
                    onClick = {
                        // TODO P0: Privacy Policy
                    }
                )
            )

            ListItem(
                leadingContent = {
                    Icon(Icons.AutoMirrored.Filled.List, contentDescription = "")
                },
                headlineContent = {
                    Text("Terms of Service")
                },
                modifier = Modifier.clickable(
                    onClick = {
                        // TODO P0: Terms of Service
                    }
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsContentPreview() {
    SettingsContent(
        innerPadding = PaddingValues(0.dp),
        currentThemeMode = ThemeMode.SYSTEM,
        onThemeClick = {},
        onImport = {},
        onExport = {},
    )
}
