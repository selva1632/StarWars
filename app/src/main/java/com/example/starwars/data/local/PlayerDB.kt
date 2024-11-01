package com.example.starwars.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.starwars.data.local.dao.MatchInfoDao
import com.example.starwars.data.local.dao.PlayerInfoDao

@Database(
    entities = [MatchInfoEntity::class, PlayerInfoEntity::class],
    version = 1
)
abstract class PlayerDB: RoomDatabase() {
    abstract val playerInfoDao: PlayerInfoDao
    abstract val matchInfoDao: MatchInfoDao
}