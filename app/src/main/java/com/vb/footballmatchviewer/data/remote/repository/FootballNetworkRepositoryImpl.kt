package com.vb.footballmatchviewer.data.remote.repository

import com.vb.footballmatchviewer.data.remote.api.FootballService
import com.vb.footballmatchviewer.data.remote.model.FixtureResponse
import com.vb.footballmatchviewer.utils.getTodayDate

class FootballNetworkRepositoryImpl(private val footballService: FootballService) : FootballNetworkRepository {
    override suspend fun getFixtures(): List<FixtureResponse> {
        return footballService.getCityWeather(getTodayDate()).response
    }
}
