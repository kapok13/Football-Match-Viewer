package com.vb.footballmatchviewer.data.remote.repository

import com.vb.footballmatchviewer.data.remote.model.FixtureResponse

interface FootballNetworkRepository {
    suspend fun getFixtures(): List<FixtureResponse>
}
