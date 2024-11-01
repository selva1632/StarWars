package com.example.starwars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwars.domain.repository.PlayerRepository
import com.example.starwars.screen.UiEvent
import com.example.starwars.screen.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerInfoViewModel @Inject constructor(
    private val playerRepository: PlayerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    fun handleEvent(event: UiEvent) {
        when (event) {
            is UiEvent.LoadData -> fetchData()
            is UiEvent.SelectPlayerProfile -> getMatchHistoryById(event.id)
            is UiEvent.ShowPlayersByScoreASC -> getPlayerSummaryListByASC()
            is UiEvent.ShowPlayersByScoreDESC -> getPlayerSummaryListByDESC()
            is UiEvent.DismissDialog -> dismissDialog()
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            _state.update { state -> state.copy(isLoading = true) }
            playerRepository.fetchPlayerInfoAndSaveInDB().collect { playerInfoList ->
                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        info = playerInfoList
                    )
                }
            }
        }
    }

    private fun getPlayerSummaryListByASC() {
        viewModelScope.launch {
            playerRepository.getPlayerSummaryListByASC().collect { playerInfoList ->
                _state.update { state ->
                    state.copy(info = playerInfoList)
                }
            }
        }
    }

    private fun getPlayerSummaryListByDESC() {
        viewModelScope.launch {
            playerRepository.getPlayerSummaryListByDESC().collect { playerInfoList ->
                _state.update { state ->
                    state.copy(info = playerInfoList)
                }
            }
        }
    }

    private fun getMatchHistoryById(id: Int) {
        viewModelScope.launch {
            _state.update { state -> state.copy(showDialog = true) }
            playerRepository.getMatchesHistoryByPlayerId(id).collect { matchHistoryItem ->
                _state.update { state ->
                    state.copy(matchHistory = matchHistoryItem)
                }
            }
        }
    }

    private fun dismissDialog() {
        _state.update { state ->
            state.copy(
                showDialog = false,
                matchHistory = null
            )
        }
    }
}