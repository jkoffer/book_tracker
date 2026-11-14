package com.jk.book_tracker.features.force_update

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SystemUpdate
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.z4kn4fein.semver.toVersionOrNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForceUpdateScreen(
    remoteConfigViewModel: RemoteConfigViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val remoteConfig by remoteConfigViewModel.config.collectAsStateWithLifecycle()

    val minAppVersion = remoteConfig.appVersion
    val currentAppVersion = remember(context) {
        try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName.orEmpty()
        } catch (_: Exception) {
            null
        }
    }

    val isUpdateRequired = remember(currentAppVersion, minAppVersion) {
        val current = currentAppVersion?.substringBefore("-")?.toVersionOrNull(strict = false)
        val minimum = minAppVersion.toVersionOrNull(strict = false)

        current != null && minimum != null && current < minimum
    }

    if (isUpdateRequired) {
        BasicAlertDialog(
            onDismissRequest = {}
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation,
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Rounded.SystemUpdate,
                        contentDescription = null,
                        modifier = Modifier
                            .size(56.dp)
                            .padding(bottom = 20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "Update required",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Box(modifier = Modifier.height(8.dp))

                    Text(
                        "To use this app, download the latest version",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Box(modifier = Modifier.height(16.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        onClick = {
                            // TODO P0: Navigate to play store
                        }
                    ) {
                        Text("Update now")
                    }
                }
            }
        }
    }
}
