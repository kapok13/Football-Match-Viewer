package com.vb.footballmatchviewer.data.remote.model

import kotlinx.serialization.Serializable


@Serializable
data class Teams(
    val home: Team,
    val away: Team
)
