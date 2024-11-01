package com.example.starwars.screen

import com.example.starwars.domain.model.MatchHistoryItem
import com.example.starwars.domain.model.Player

data class UiState (
    val isLoading: Boolean = false,
    val info: List<Player> = emptyList(),
    val showDialog: Boolean = false,
    val matchHistory: MatchHistoryItem? = null
)