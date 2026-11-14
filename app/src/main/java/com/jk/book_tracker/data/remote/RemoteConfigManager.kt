package com.jk.book_tracker.data.remote

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigManager @Inject constructor() {
    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    suspend fun init() {
        val settings = FirebaseRemoteConfigSettings.Builder()
            .setFetchTimeoutInSeconds(10)
            .setMinimumFetchIntervalInSeconds(21600) // 6h
            .build()

        remoteConfig.setConfigSettingsAsync(settings).await()

        remoteConfig.fetchAndActivate().await()
    }

    fun getAppVersion(): String {
        return remoteConfig.getString("app_version")
    }
}
