package com.example.starwars.data.local

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "player_info_table")
data class PlayerInfoEntity(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "player_name")
    val name: String,

    @ColumnInfo(name = "image_url")
    val image: String,

    @ColumnInfo(name = "score")
    val score: Int
)
