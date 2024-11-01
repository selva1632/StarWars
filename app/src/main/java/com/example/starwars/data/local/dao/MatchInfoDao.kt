package com.example.starwars.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.starwars.data.local.MatchInfoEntity

@Dao
interface MatchInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(matches: List<MatchInfoEntity>)

    @Query("SELECT * FROM match_info_table WHERE player1_id == :id OR player2_id == :id")
    suspend fun getMatchesById(id: Int): List<MatchInfoEntity>
}