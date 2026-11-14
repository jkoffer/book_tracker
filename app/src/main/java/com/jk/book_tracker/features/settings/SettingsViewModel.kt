package com.jk.book_tracker.features.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jk.book_tracker.data.local.BackupManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val backupManager: BackupManager
) : ViewModel() {
    fun importData(uri: Uri) {
        viewModelScope.launch {
            backupManager.import(uri)
        }
    }

    fun exportData(uri: Uri) {
        viewModelScope.launch {
            backupManager.export(uri)
        }
    }
}