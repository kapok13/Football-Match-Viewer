package com.vb.footballmatchviewer.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fixtures")
data class FootballEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val timestamp: Long,
    val home: Int?,
    val away: Int?,
    val team1_name: String,
    val team1_logo: String,
    val team2_name: String,
    val team2_logo: String
)
