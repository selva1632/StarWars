package com.example.starwars.data.remote

import com.example.starwars.data.remote.model.MatchItemDto
import com.example.starwars.data.remote.model.PlayerItemDto
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