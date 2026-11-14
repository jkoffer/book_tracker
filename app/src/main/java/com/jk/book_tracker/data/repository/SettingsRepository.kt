package com.jk.book_tracker.data.repository

import com.jk.book_tracker.data.local.datastore.ThemeDataStore
import com.jk.book_tracker.domain.ThemeMode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val themeDataStore: ThemeDataStore
) {
    val themeMode: Flow<ThemeMode> = themeDataStore.themeMode

    suspend fun setThemeMode(themeMode: ThemeMode) {
        themeDataStore.setThemeMode(themeMode)
    }
}