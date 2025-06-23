package com.vb.footballmatchviewer.data.remote.api

import com.vb.footballmatchviewer.data.remote.model.FootballResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FootballService {
    @GET("fixtures")
    suspend fun getCityWeather(@Query("date") date: String) : FootballResponse
}
