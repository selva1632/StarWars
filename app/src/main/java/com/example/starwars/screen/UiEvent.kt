package com.example.starwars.screen

sealed class UiEvent {
    data object LoadData: UiEvent()
    data object ShowPlayersByScoreASC: UiEvent()
    data object ShowPlayersByScoreDESC: UiEvent()
    data class SelectPlayerProfile(val id: Int): UiEvent()
    data object DismissDialog: UiEvent()
}