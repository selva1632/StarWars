package com.example.starwars.domain.model

data class Match(
    val player1: String,
    val player1Score: Int,
    val player2: String,
    val player2Score: Int,
    val result: MatchResult
)