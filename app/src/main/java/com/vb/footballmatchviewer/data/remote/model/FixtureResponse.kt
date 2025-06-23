package com.vb.footballmatchviewer.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class FixtureResponse(
    val fixture: Fixture,
    val teams: Teams,
    val goals: Goals
)
