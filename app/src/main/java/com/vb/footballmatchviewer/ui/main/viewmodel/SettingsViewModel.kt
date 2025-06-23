package com.vb.footballmatchviewer.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vb.footballmatchviewer.data.local.model.SettingsEntity
import com.vb.footballmatchviewer.data.local.repository.FootballDatabaseRepository
import com.vb.footballmatchviewer.ui.main.mvi.SettingsMvi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val footballDatabaseRepository: FootballDatabaseRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsMvi.State())
    val state = _state.asStateFlow()
    private val intent = MutableSharedFlow<SettingsMvi.Intent>()

    init {
        viewModelScope.launch {
            intent.collect {
                when (it) {
                    is SettingsMvi.Intent.Load -> getSettings()
                    is SettingsMvi.Intent.Save -> saveSettings(it.settingsEntity)
                }
            }
        }
        sendIntent(SettingsMvi.Intent.Load)
    }

    private fun getSettings() {
        viewModelScope.launch {
            try {
                footballDatabaseRepository.getSettings().collect { settings ->
                    _state.update {
                        it.copy(
                            isDarkTheme = settings.isDarkTheme
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun saveSettings(settingsEntity: SettingsEntity) {
        viewModelScope.launch {
            footballDatabaseRepository.saveSettings(settingsEntity)
        }
    }

    fun sendIntent(currentIntent: SettingsMvi.Intent) {
        viewModelScope.launch { intent.emit(currentIntent) }
    }
}
