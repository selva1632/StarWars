package com.example.starwars.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.starwars.PlayerInfoViewModel
import com.example.starwars.R
import com.example.starwars.domain.model.Player

@Composable
fun PlayerLeaderBoard(
    viewModel: PlayerInfoViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.handleEvent(UiEvent.LoadData)
    }

    when {
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        state.showDialog -> state.matchHistory?.let { MatchSummaryInfo(state, viewModel::handleEvent) }
        else -> LeaderBoardContent(state, viewModel::handleEvent)
    }
}

@Composable
fun LeaderBoardContent(
    state: UiState,
    sendEvent: (UiEvent) -> Unit
) {
    val playerListState = rememberLazyListState()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(5f)
                    .padding(start = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Leader Board",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                DropdownButton(sendEvent)
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            state = playerListState
        ) {
            state.info.forEach {
                item {
                    ProfileItem(it, sendEvent)
                }
            }
        }
    }
}

@Composable
fun DropdownButton(
    sendEvent: (UiEvent) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var highToLowEnabled by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.filter),
                contentDescription = "More"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shadowElevation = 3.dp,
            border = BorderStroke(1.dp, Color.Black)
        ) {
            DropdownMenuItem(
                text = { Text("high - slow") },
                leadingIcon = { Icon(imageVector = Icons.Filled.KeyboardArrowDown, contentDescription = "high - low")},
                enabled = highToLowEnabled,
                onClick = {
                    sendEvent(UiEvent.ShowPlayersByScoreDESC)
                    expanded = false
                    highToLowEnabled = false
                }
            )
            DropdownMenuItem(
                text = { Text("low - high") },
                leadingIcon = { Icon(imageVector = Icons.Filled.KeyboardArrowUp, contentDescription = "high - low")},
                enabled = !highToLowEnabled,
                onClick = {
                    sendEvent(UiEvent.ShowPlayersByScoreASC)
                    expanded = false
                    highToLowEnabled = true
                }
            )
        }
    }
}

@Composable
fun ProfileItem(
    player: Player,
    sendEvent: (UiEvent) -> Unit
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        onClick = {sendEvent(UiEvent.SelectPlayerProfile(player.id)) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            val painter = rememberAsyncImagePainter(model = player.icon)
            Image(
                painter = painter,
                contentDescription = "${player.name}'s profile picture",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(100.dp)
                    .clip(CircleShape)
            )

            Text(
                text = player.name,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = player.score,
                fontSize = 25.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPlayerLeaderBoard() {
    LeaderBoardContent(
        UiState(
            info = listOf(
                Player(name = "selva", score = "10", icon = "", id = 0),
                Player(name = "selva", score = "10", icon = "", id = 0)
            )
        )
    ) {}
}