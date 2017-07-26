package com.datarockets.mnchkn.data.local

import com.datarockets.mnchkn.MunchkinApplication
import com.datarockets.mnchkn.data.mapper.GameEntityMapper
import com.datarockets.mnchkn.data.mapper.GameStepMapper
import com.datarockets.mnchkn.data.mapper.PlayerEntityMapper
import com.datarockets.mnchkn.data.mapper.PlayerMapper
import com.datarockets.mnchkn.data.models.GameStep
import com.datarockets.mnchkn.data.models.Player
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseHelper
@Inject constructor() {

    fun setPlayer(player: Player): Single<Player> = Single.create {
        val playerId = MunchkinApplication.database.playerDao().setPlayer(PlayerEntityMapper.transform(player))
        val playerEntity = MunchkinApplication.database.playerDao().getPlayerById(playerId)
        it.onSuccess(PlayerMapper.transform(playerEntity))
    }

    fun getPlayer(playerId: Long): Maybe<Player> = Maybe.create {
        val playerEntity = MunchkinApplication.database.playerDao().getPlayerById(playerId)
        it.onSuccess(PlayerMapper.transform(playerEntity))
        it.onComplete()
    }

    fun getPlayers(): Single<List<Player>> = Single.create {
        val playerEntityList = MunchkinApplication.database.playerDao().getPlayers()
        it.onSuccess(PlayerMapper.transform(playerEntityList))
    }

    fun getPlayingPlayersByPosition(): Single<List<Player>> = Single.create {
        val playerEntityList = MunchkinApplication.database.playerDao().getPlayingPlayersByPosition()
        it.onSuccess(PlayerMapper.transform(playerEntityList))
    }

    fun getPlayedPlayersByLevel(): Single<List<Player>> = Single.create {
        val playerEntityList = MunchkinApplication.database.playerDao().getPlayingPlayersByLevel()
        it.onSuccess(PlayerMapper.transform(playerEntityList))
    }

    fun getPlayedPlayersByStrength(): Single<List<Player>> = Single.create {
        val playerEntityList = MunchkinApplication.database.playerDao().getPlayingPlayersByStrength()
        it.onSuccess(PlayerMapper.transform(playerEntityList))
    }

    fun getPlayedPlayersByTotal(): Single<List<Player>> = Single.create {
        val playerEntityList = MunchkinApplication.database.playerDao().getPlayingPlayersByTotal()
        it.onSuccess(PlayerMapper.transform(playerEntityList))
    }

    fun deletePlayer(playerId: Long): Completable = Completable.create {
        MunchkinApplication.database.playerDao().deletePlayerById(playerId)
        it.onComplete()
    }

    fun updatePlayerName(playerId: Long, playerName: String): Completable = Completable.create {
        val playerEntity = MunchkinApplication.database.playerDao().getPlayerById(playerId).copy(name = playerName)
        MunchkinApplication.database.playerDao().updatePlayer(playerEntity)
        it.onComplete()
    }

    fun updatePlayerScores(playerId: Long, levelScore: Int, strengthScore: Int): Completable = Completable.create {
        val playerEntity = MunchkinApplication.database.playerDao().getPlayerById(playerId).copy(level = levelScore, strength = strengthScore)
        MunchkinApplication.database.playerDao().updatePlayer(playerEntity)
        it.onComplete()
    }

    fun updatePlayerPlaying(playerId: Long, isPlaying: Boolean): Completable = Completable.create {
        val playerEntity = MunchkinApplication.database.playerDao().getPlayerById(playerId).copy(isPlaying = isPlaying)
        MunchkinApplication.database.playerDao().updatePlayer(playerEntity)
        it.onComplete()
    }

    fun changePlayersPositions(playerId: Long, newPosition: Int): Completable = Completable.create {
        val playerEntity = MunchkinApplication.database.playerDao().getPlayerById(playerId).copy(position = newPosition)
        MunchkinApplication.database.playerDao().updatePlayer(playerEntity)
        it.onComplete()
    }

    fun setGameStep(gameStep: GameStep): Completable = Completable.create {
        MunchkinApplication.database.gameDao().setGameStep(GameEntityMapper.transform(gameStep))
        it.onComplete()
    }

    fun getGameSteps(): Single<List<GameStep>> = Single.create {
        val gameStepList = MunchkinApplication.database.gameDao().getGameSteps()
        it.onSuccess(GameStepMapper.transform(gameStepList))
    }

    fun deleteGameStepsAndResetPlayingPlayers(): Completable = Completable.create {
        MunchkinApplication.database.gameDao().deleteGameSteps()
        val playersEntityList = MunchkinApplication.database.playerDao().getPlayingPlayersByPosition()
        playersEntityList.forEach {
            val playerEntity = it.copy(level = 1, strength = 1)
            MunchkinApplication.database.playerDao().updatePlayer(playerEntity)
        }
        it.onComplete()
    }
}
