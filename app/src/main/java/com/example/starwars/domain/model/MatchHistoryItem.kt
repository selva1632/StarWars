package com.example.starwars.domain.model

data class MatchHistoryItem(
    val playerName: String,
    val matches: List<Match>
)
