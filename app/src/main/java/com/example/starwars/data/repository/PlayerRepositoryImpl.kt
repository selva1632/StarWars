package com.example.starwars.data.repository

import android.util.Log
import com.example.starwars.data.local.PlayerDB
import com.example.starwars.data.mapper.getResults
import com.example.starwars.data.mapper.toMatch
import com.example.starwars.data.mapper.toMatchInfoEntity
import com.example.starwars.data.mapper.toPlayer
import com.example.starwars.data.mapper.toPlayerInfoEntity
import com.example.starwars.data.remote.ApiService
import com.example.starwars.domain.model.MatchHistoryItem
import com.example.starwars.domain.model.MatchResult
import com.example.starwars.domain.model.Player
import com.example.starwars.domain.repository.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val playerDB: PlayerDB
) : PlayerRepository {
    override suspend fun fetchPlayerInfoAndSaveInDB(): Flow<List<Player>> {
        val localPlayerList = playerDB.playerInfoDao.getAllPlayerInfo()
        val shouldLoadLocalData = localPlayerList.isEmpty()

        if (!shouldLoadLocalData) {
            return flow {
                val playerInfo = playerDB.playerInfoDao.getAllPlayerInfo().map {
                    it.toPlayer()
                }
                emit(playerInfo)
            }
        }

        supervisorScope {
            val fetchPlayerInfoJob = async { getPlayerInfoRemote() }
            val fetchMatchInfoJob = async { getMatchInfoRemote() }
            fetchPlayerInfoJob.await()
            fetchMatchInfoJob.await()
        }
        updateScoreOfPlayer()

        return flow {
            val playerInfo = playerDB.playerInfoDao.getAllPlayerInfo().map {
                it.toPlayer()
            }
            emit(playerInfo)
        }
    }

    private suspend fun getPlayerInfoRemote() {
        val playerInfoResponse = try {
            api.getPlayerInfo()
        } catch (e: IOException) {
            Log.i(TAG, "getPlayerInfoRemote: IOException - ${e.message}")
            emptyList()
        } catch (e: HttpException) {
            Log.i(TAG, "getPlayerInfoRemote: HttpException - ${e.message}")
            emptyList()
        } catch (e: Exception) {
            Log.i(TAG, "getPlayerInfoRemote: Exception - ${e.message}")
            emptyList()
        }

        val playerEntity = playerInfoResponse.let {
            it.map { playerItemDto ->
                playerItemDto.toPlayerInfoEntity()
            }
        }

        playerDB.playerInfoDao.insert(playerEntity)
    }

    private suspend fun getMatchInfoRemote() {
        val matchInfoResponse = try {
            api.getMatchInfo()
        } catch (e: IOException) {
            Log.i(TAG, "getMatchInfoRemote: IOException - ${e.message}")
            emptyList()
        } catch (e: HttpException) {
            Log.i(TAG, "getMatchInfoRemote: HttpException - ${e.message}")
            emptyList()
        } catch (e: Exception) {
            Log.i(TAG, "getMatchInfoRemote: Exception - ${e.message}")
            emptyList()
        }

        val playerInfoList = playerDB.playerInfoDao.getAllPlayerInfo()
        val matchEntity = matchInfoResponse.let { response ->
            response.map { matchItemDto ->
                matchItemDto.toMatchInfoEntity(playerInfoList)
            }
        }
        playerDB.matchInfoDao.insert(matchEntity)
    }

    private suspend fun updateScoreOfPlayer() {
        withContext(Dispatchers.IO) {
            playerDB.playerInfoDao.getAllPlayerInfo().forEach {
                var score = 0
                playerDB.matchInfoDao.getMatchesById(it.id).forEach { matchInfoEntity ->
                    val result = matchInfoEntity.getResults(it.id)

                    score += when (result) {
                        MatchResult.WIN -> WIN_SCORE
                        MatchResult.TIE -> TIE_SCORE
                        else -> 0
                    }
                    playerDB.playerInfoDao.updateScoreOfPlayer(it.id, score)
                }
            }
        }
    }

    override suspend fun getPlayerSummaryListByASC(): Flow<List<Player>> {
        return flow {
            emit(
                playerDB.playerInfoDao.getAllPlayerInfoASC().map { playerInfo ->
                    playerInfo.toPlayer()
                }
            )
        }
    }

    override suspend fun getPlayerSummaryListByDESC(): Flow<List<Player>> {
        return flow {
            emit(
                playerDB.playerInfoDao.getAllPlayerInfo().map { playerInfo ->
                    playerInfo.toPlayer()
                }
            )
        }
    }

    override suspend fun getMatchesHistoryByPlayerId(id: Int): Flow<MatchHistoryItem> {
        return flow {
            val playerName = playerDB.playerInfoDao.getPlayerNameById(id)
            val matches = playerDB.matchInfoDao.getMatchesById(id).map { matchInfo ->
                matchInfo.toMatch(id)
            }
            emit(
                MatchHistoryItem(
                    playerName = playerName,
                    matches = matches
                )
            )
        }
    }

    companion object {
        private const val TAG = "PlayerRepositoryImpl"
        private const val WIN_SCORE = 3
        private const val TIE_SCORE = 1
    }
}