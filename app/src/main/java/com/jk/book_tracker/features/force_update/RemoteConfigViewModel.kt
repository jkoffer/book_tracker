package com.jk.book_tracker.features.force_update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jk.book_tracker.data.remote.RemoteConfigManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppConfig(
    val appVersion: String = "1.0.0"
)

@HiltViewModel
class RemoteConfigViewModel @Inject constructor(
    private val remoteConfigManager: RemoteConfigManager
) : ViewModel() {

    private val _config = MutableStateFlow(AppConfig())
    val config: StateFlow<AppConfig> = _config.asStateFlow()

    init {
        loadConfig()
    }

    private fun loadConfig() {
        viewModelScope.launch {
            try {
                remoteConfigManager.init()

                _config.value = AppConfig(
                    appVersion = remoteConfigManager.getAppVersion()
                )
            } catch (e: Exception) {
                // TODO: Handle exception
            }
        }
    }
}
