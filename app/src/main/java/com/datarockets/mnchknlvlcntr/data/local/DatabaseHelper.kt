package com.datarockets.mnchknlvlcntr.data.local

import com.datarockets.mnchknlvlcntr.data.mapper.GameEntityMapper
import com.datarockets.mnchknlvlcntr.data.mapper.GameStepMapper
import com.datarockets.mnchknlvlcntr.data.mapper.PlayerEntityMapper
import com.datarockets.mnchknlvlcntr.data.mapper.PlayerMapper
import com.datarockets.mnchknlvlcntr.data.models.GameStep
import com.datarockets.mnchknlvlcntr.data.models.Player
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseHelper
@Inject constructor(private val playerDao: PlayerDao, private val gameDao: GameDao) {

    fun setPlayer(player: Player): Single<Player> = Single.create {
        val playerId = playerDao.setPlayer(PlayerEntityMapper.transform(player))
        val playerEntity = playerDao.getPlayerById(playerId)
        it.onSuccess(PlayerMapper.transform(playerEntity))
    }

    fun getPlayer(playerId: Long): Maybe<Player> = Maybe.create {
        val playerEntity = playerDao.getPlayerById(playerId)
        it.onSuccess(PlayerMapper.transform(playerEntity))
        it.onComplete()
    }

    fun getPlayers(): Single<List<Player>> = Single.create {
        val playerEntityList = playerDao.getPlayers()
        it.onSuccess(PlayerMapper.transform(playerEntityList))
    }

    fun getPlayingPlayersByPosition(): Single<List<Player>> = Single.create {
        val playerEntityList = playerDao.getPlayingPlayersByPosition()
        it.onSuccess(PlayerMapper.transform(playerEntityList))
    }

    fun getPlayedPlayersByLevel(): Single<List<Player>> = Single.create {
        val playerEntityList = playerDao.getPlayingPlayersByLevel()
        it.onSuccess(PlayerMapper.transform(playerEntityList))
    }

    fun getPlayedPlayersByStrength(): Single<List<Player>> = Single.create {
        val playerEntityList = playerDao.getPlayingPlayersByStrength()
        it.onSuccess(PlayerMapper.transform(playerEntityList))
    }

    fun getPlayedPlayersByTotal(): Single<List<Player>> = Single.create {
        val playerEntityList = playerDao.getPlayingPlayersByTotal()
        it.onSuccess(PlayerMapper.transform(playerEntityList))
    }

    fun deletePlayer(playerId: Long): Completable = Completable.create {
        playerDao.deletePlayerById(playerId)
        it.onComplete()
    }

    fun updatePlayerName(playerId: Long, playerName: String): Completable = Completable.create {
        val playerEntity = playerDao.getPlayerById(playerId).copy(name = playerName)
        playerDao.updatePlayer(playerEntity)
        it.onComplete()
    }

    fun updatePlayerScores(playerId: Long, levelScore: Int, strengthScore: Int): Completable = Completable.create {
        val playerEntity = playerDao.getPlayerById(playerId).copy(level = levelScore, strength = strengthScore)
        playerDao.updatePlayer(playerEntity)
        it.onComplete()
    }

    fun updatePlayerPlaying(playerId: Long, isPlaying: Boolean): Completable = Completable.create {
        val playerEntity = playerDao.getPlayerById(playerId).copy(isPlaying = isPlaying)
        playerDao.updatePlayer(playerEntity)
        it.onComplete()
    }

    fun changePlayersPositions(playerId: Long, newPosition: Int): Completable = Completable.create {
        val playerEntity = playerDao.getPlayerById(playerId).copy(position = newPosition)
        playerDao.updatePlayer(playerEntity)
        it.onComplete()
    }

    fun setGameStep(gameStep: GameStep): Completable = Completable.create {
        gameDao.setGameStep(GameEntityMapper.transform(gameStep))
        it.onComplete()
    }

    fun getGameSteps(): Single<List<GameStep>> = Single.create {
        val gameStepList = gameDao.getGameSteps()
        it.onSuccess(GameStepMapper.transform(gameStepList))
    }

    fun deleteGameStepsAndResetPlayingPlayers(): Completable = Completable.create {
        gameDao.deleteGameSteps()
        val playersEntityList = playerDao.getPlayingPlayersByPosition()
        playersEntityList.forEach {
            val playerEntity = it.copy(level = 1, strength = 1)
            playerDao.updatePlayer(playerEntity)
        }
        it.onComplete()
    }
}
