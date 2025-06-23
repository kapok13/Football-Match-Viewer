package com.vb.footballmatchviewer.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class FootballResponse(
    val response: List<FixtureResponse>
)
