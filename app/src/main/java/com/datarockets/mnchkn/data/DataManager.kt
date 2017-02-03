package com.datarockets.mnchkn.data

import com.datarockets.mnchkn.data.local.DatabaseHelper
import com.datarockets.mnchkn.data.local.Db
import com.datarockets.mnchkn.data.local.PreferencesHelper
import com.datarockets.mnchkn.data.models.GameStep
import com.datarockets.mnchkn.data.models.Player
import com.datarockets.mnchkn.utils.ColorUtil
import com.github.mikephil.charting.data.Entry
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class DataManager
@Inject constructor(private val mDatabaseHelper: DatabaseHelper,
                    mPreferencesHelper: PreferencesHelper) {

    val preferencesHelper = mPreferencesHelper

    fun getPlayer(playerId: Long): Observable<Player> {
        return mDatabaseHelper.getPlayer(playerId)
    }

    open fun getPlayingPlayers(): Observable<List<Player>> {
        return mDatabaseHelper.getPlayingPlayers().toList()
    }

    open fun getPlayers(): Observable<List<Player>> {
        return mDatabaseHelper.getPlayers(Db.PlayerTable.ORDER_BY_POSITION).toList()
    }

    open fun getPlayers(sortType: Int): Observable<List<Player>> {
        return mDatabaseHelper.getPlayers(sortType).toList()
    }

    fun addPlayer(playerName: String): Observable<Player> {
        val player = Player()
        player.name = playerName
        player.levelScore = 1
        player.strengthScore = 1
        player.color = ColorUtil.generatePlayerAvatarColor()
        return mDatabaseHelper.setPlayer(player)
    }

    fun changePlayerPosition(playerId: Long,
                             position: Int): Observable<Void> {
        return mDatabaseHelper.changePlayerPosition(playerId, position)
    }

    fun setPlayerPlaying(playerId: Long, isPlaying: Boolean): Observable<Void> {
        return mDatabaseHelper.markPlayerPlaying(playerId, isPlaying)
    }

    fun updatePlayerName(playerId: Long, playerName: String): Observable<Void> {
        return mDatabaseHelper.updatePlayerName(playerId, playerName)
    }

    fun updatePlayerScores(playerId: Long, levelScore: Int, strengthScore: Int): Observable<Void> {
        return mDatabaseHelper.updatePlayerScores(playerId, levelScore, strengthScore)
    }

    fun deletePlayer(playerId: Long): Observable<Void> {
        return mDatabaseHelper.deletePlayer(playerId)
    }

    fun clearGameSteps(): Observable<Void> {
        return mDatabaseHelper.clearGameSteps()
    }

    fun clearPlayerStats(): Observable<Void> {
        return mDatabaseHelper.clearPlayerStats()
    }

    fun getEntries() {
        mDatabaseHelper.gameSteps
                .groupBy(GameStep::playerId)
                .map { it ->
                    it.map {
                        val entry = Entry()
                        entry.y = it.playerLevel.toFloat()
                        entry
                    }
                }
                .toList()

    }

    fun addGameStep(player: Player): Observable<Void> {
        val gameStep = GameStep()
        gameStep.playerId = player.id
        gameStep.playerLevel = player.levelScore
        gameStep.playerStrength = player.strengthScore
        return mDatabaseHelper.setGameStep(gameStep)
    }

}
