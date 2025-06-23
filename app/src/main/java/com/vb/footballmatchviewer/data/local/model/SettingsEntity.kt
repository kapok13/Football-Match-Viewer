package com.vb.footballmatchviewer.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 0,
    val isDarkTheme: Boolean
)
