package com.vb.footballmatchviewer.data.local.repository

import com.vb.footballmatchviewer.data.local.database.FootballDatabase
import com.vb.footballmatchviewer.data.local.model.FootballEntity
import com.vb.footballmatchviewer.data.local.model.SettingsEntity
import kotlinx.coroutines.flow.Flow

class FootballDatabaseRepositoryImpl(private val footballDatabase: FootballDatabase) : FootballDatabaseRepository {
    override suspend fun getFixtures(): Flow<List<FootballEntity>> {
        return footballDatabase.footballDao().getAll()
    }

    override suspend fun saveFixtures(fixtures: List<FootballEntity>) {
        footballDatabase.footballDao().clear()
        fixtures.forEach {
            footballDatabase.footballDao().insert(it)
        }
    }

    override suspend fun getSettings(): Flow<SettingsEntity> = footballDatabase.settingsDao().getSettings()

    override suspend fun saveSettings(settings: SettingsEntity) {
        footballDatabase.settingsDao().saveSettings(settings)
    }
}
