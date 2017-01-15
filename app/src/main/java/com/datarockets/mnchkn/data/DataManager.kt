package com.datarockets.mnchkn.data

import com.datarockets.mnchkn.data.local.DatabaseHelper
import com.datarockets.mnchkn.data.local.PreferencesHelper
import com.datarockets.mnchkn.data.models.GameStep
import com.datarockets.mnchkn.data.models.Player
import com.datarockets.mnchkn.utils.ColorUtil
import com.github.mikephil.charting.data.Entry
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManager
@Inject constructor(private val mDatabaseHelper: DatabaseHelper,
                    private val mPreferencesHelper: PreferencesHelper) {

    val preferencesHelper = mPreferencesHelper

    fun getPlayer(playerId: Long): Observable<Player> {
        return mDatabaseHelper.getPlayer(playerId)
    }

    val players: Observable<List<Player>>
        get() = mDatabaseHelper.players.toList()

    fun getPlayers(sortType: Int): Observable<List<Player>> {
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

    fun updatePlayer(player: Player): Observable<Player> {
        return mDatabaseHelper.updatePlayer(player)
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
