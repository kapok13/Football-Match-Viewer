package com.vb.footballmatchviewer.ui.model

data class Fixture(
    val date: String,
    val timestamp: Long,
    val home: Int?,
    val away: Int?,
    val team1_name: String,
    val team1_logo: String,
    val team2_name: String,
    val team2_logo: String
)
