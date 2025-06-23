package com.vb.footballmatchviewer.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class Goals(
    val home: Int?,
    val away: Int?
)
