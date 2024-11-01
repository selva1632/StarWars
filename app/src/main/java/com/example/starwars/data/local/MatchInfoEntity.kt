package com.example.starwars.data.local

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "match_info_table")
data class MatchInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val matchId: Int = 0,

    @ColumnInfo(name = "player1_id")
    val player1Id: Int,

    @ColumnInfo(name = "player1_name")
    val player1Name: String,

    @ColumnInfo(name = "player1_score")
    val player1Score: Int,

    @ColumnInfo(name = "player2_id")
    val player2Id: Int,

    @ColumnInfo(name = "player2_name")
    val player2Name: String,

    @ColumnInfo(name = "player2_score")
    val player2Score: Int
)
