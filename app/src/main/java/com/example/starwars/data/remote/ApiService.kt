package com.example.starwars.data.remote

import com.example.starwars.data.remote.response.MatchItemDto
import com.example.starwars.data.remote.response.PlayerItemDto
import retrofit2.http.GET

interface ApiService {
    @GET("IKQQ")
    suspend fun getPlayerInfo(): List<PlayerItemDto>

    @GET("JNYL")
    suspend fun getMatchInfo(): List<MatchItemDto>

    companion object {
        const val BASE_URL = "https://jsonkeeper.com/b/"
    }
}