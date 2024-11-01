package com.example.starwars.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.starwars.domain.model.Match
import com.example.starwars.domain.model.MatchHistoryItem
import com.example.starwars.domain.model.MatchResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchSummaryInfo(
    state: UiState,
    sendEvent: (UiEvent) -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = {
            sendEvent(UiEvent.DismissDialog)
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                state.matchHistory?.playerName?.let { playerName ->
                    Text(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally),
                        text = playerName,
                        style = MaterialTheme.typography.displayMedium,
                        maxLines = 1
                    )
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    items(state.matchHistory?.matches ?: emptyList()) { match ->
                        HistoryItem(match)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItem(matchSummary: Match) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            when (matchSummary.result) {
                MatchResult.WIN -> Color(0xFF16C46A)
                MatchResult.LOSS -> Color.Red
                MatchResult.TIE -> Color.Gray
            },
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .weight(2f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = matchSummary.player1
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${matchSummary.player1Score} - ${matchSummary.player2Score}",
                    textAlign = TextAlign.Center
                )
            }

            Box(
                modifier = Modifier
                    .weight(2f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = matchSummary.player2,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMatchSummaryInfo() {
    MatchSummaryInfo(
        UiState(
            matchHistory = MatchHistoryItem(
                playerName = "selva",
                matches = listOf(
                    Match("selva", 10, "dinesh", 20, MatchResult.WIN),
                    Match("selva", 10, "dinesh", 20, MatchResult.TIE),
                    Match("selva", 10, "dinesh", 20, MatchResult.LOSS),
                    Match("selva", 10, "dinesh", 20, MatchResult.WIN)
                )
            )
        )
    ) {}
}

@Preview(showBackground = true)
@Composable
fun PreviewHistoryItem() {
    HistoryItem(Match("selva", 10, "pawan", 2, MatchResult.LOSS))
}