package com.vb.footballmatchviewer.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class Fixture(
    val date: String,
    val timestamp: Long
)
