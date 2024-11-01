package com.example.starwars.domain.repository

import com.example.starwars.domain.model.MatchHistoryItem
import com.example.starwars.domain.model.Player
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    suspend fun fetchPlayerInfoAndSaveInDB(): Flow<List<Player>>
    suspend fun getPlayerSummaryListByASC(): Flow<List<Player>>
    suspend fun getPlayerSummaryListByDESC(): Flow<List<Player>>
    suspend fun getMatchesHistoryByPlayerId(id: Int): Flow<MatchHistoryItem>
}