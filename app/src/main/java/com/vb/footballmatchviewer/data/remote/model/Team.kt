package com.vb.footballmatchviewer.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class Team(
    val name: String,
    val logo: String
)
