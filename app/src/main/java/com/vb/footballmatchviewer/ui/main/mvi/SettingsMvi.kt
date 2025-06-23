package com.vb.footballmatchviewer.ui.main.mvi

import com.vb.footballmatchviewer.data.local.model.SettingsEntity


sealed class SettingsMvi {
    data class State(
        val isDarkTheme: Boolean = true
    )

    sealed class Intent {
        object Load : Intent()
        data class Save(val settingsEntity: SettingsEntity) : Intent()
    }
}
