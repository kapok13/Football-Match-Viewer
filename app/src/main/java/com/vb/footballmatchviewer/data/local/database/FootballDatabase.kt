package com.vb.footballmatchviewer.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vb.footballmatchviewer.data.local.dao.FootballDao
import com.vb.footballmatchviewer.data.local.dao.SettingsDao
import com.vb.footballmatchviewer.data.local.model.FootballEntity
import com.vb.footballmatchviewer.data.local.model.SettingsEntity

@Database(entities = [FootballEntity::class, SettingsEntity::class], version = 1)
abstract class FootballDatabase : RoomDatabase() {
    abstract fun footballDao(): FootballDao
    abstract fun settingsDao(): SettingsDao
}
