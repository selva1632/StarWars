package com.example.starwars.data.mapper

import com.example.starwars.data.local.MatchInfoEntity
import com.example.starwars.data.local.PlayerInfoEntity
import com.example.starwars.data.remote.response.MatchItemDto
import com.example.starwars.domain.model.Match
import com.example.starwars.domain.model.MatchResult

fun MatchItemDto.toMatchInfoEntity(playerList: List<PlayerInfoEntity>): MatchInfoEntity {
    return MatchInfoEntity(
        player1Id = player1.id,
        player1Name = playerList.find { it.id == player1.id }?.name.toString(),
        player1Score = player1.score,
        player2Id = player2.id,
        player2Name = playerList.find { it.id == player2.id }?.name.toString(),
        player2Score = player2.score
    )
}

fun MatchInfoEntity.toMatch(currentPlayerId: Int): Match {
    return Match(
        player1 = player1Name,
        player1Score = player1Score,
        player2 = player2Name,
        player2Score = player2Score,
        result = getResults(currentPlayerId)
    )
}

fun MatchInfoEntity.getResults(id: Int): MatchResult {
    return when (id) {
        player1Id -> {
            if (player1Score < player2Score) {
                MatchResult.LOSS
            } else if (player1Score > player2Score) {
                MatchResult.WIN
            } else {
                MatchResult.TIE
            }
        }
        player2Id -> {
            if (player1Score < player2Score) {
                MatchResult.WIN
            } else if (player1Score > player2Score) {
                MatchResult.LOSS
            } else {
                MatchResult.TIE
            }
        }
        else -> MatchResult.TIE
    }
}