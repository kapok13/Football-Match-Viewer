package com.vb.footballmatchviewer.data.mapper

import com.vb.footballmatchviewer.data.local.model.FootballEntity
import com.vb.footballmatchviewer.data.remote.model.FixtureResponse
import com.vb.footballmatchviewer.ui.model.Fixture

fun FootballEntity.toUiFixtures() = Fixture(
    date = this.date,
    timestamp = this.timestamp,
    home = this.home,
    away = this.away,
    team1_name = this.team1_name,
    team1_logo = this.team1_logo,
    team2_name = this.team2_name,
    team2_logo = this.team2_logo
)

fun FixtureResponse.toFootballEntity() = FootballEntity(
    date = this.fixture.date,
    timestamp = this.fixture.timestamp,
    home = this.goals.home,
    away = this.goals.away,
    team1_name = this.teams.home.name,
    team1_logo = this.teams.home.logo,
    team2_logo = this.teams.away.logo,
    team2_name = this.teams.away.name
)
