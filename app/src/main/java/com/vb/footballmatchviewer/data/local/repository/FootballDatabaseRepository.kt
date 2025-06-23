package com.vb.footballmatchviewer.data.local.repository

import com.vb.footballmatchviewer.data.local.model.FootballEntity
import com.vb.footballmatchviewer.data.local.model.SettingsEntity
import kotlinx.coroutines.flow.Flow

interface FootballDatabaseRepository {
    suspend fun getFixtures(): Flow<List<FootballEntity>>
    suspend fun saveFixtures(fixtures: List<FootballEntity>)
    suspend fun getSettings(): Flow<SettingsEntity>
    suspend fun saveSettings(settings: SettingsEntity)
}
