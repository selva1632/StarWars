package com.example.starwars.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.starwars.data.local.PlayerInfoEntity

@Dao
interface PlayerInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(player: List<PlayerInfoEntity>)

    @Query("SELECT * FROM player_info_table ORDER BY score DESC")
    suspend fun getAllPlayerInfo(): List<PlayerInfoEntity>

    @Query("SELECT * FROM player_info_table ORDER BY score ASC")
    suspend fun getAllPlayerInfoASC(): List<PlayerInfoEntity>

    @Query("SELECT player_name FROM player_info_table WHERE id == :id")
    suspend fun getPlayerNameById(id: Int): String

    @Query("UPDATE PLAYER_INFO_TABLE SET score = :score WHERE id = :id")
    suspend fun updateScoreOfPlayer(id: Int, score: Int)
}