package com.example.starwars.data.remote.model

data class MatchItemDto(
    val matchId: Int,
    val player1: MatchPlayersDto,
    val player2: MatchPlayersDto
)
