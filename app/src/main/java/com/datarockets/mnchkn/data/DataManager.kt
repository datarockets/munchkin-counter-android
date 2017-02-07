package com.datarockets.mnchkn.data

import android.content.Intent
import com.datarockets.mnchkn.data.local.DatabaseHelper
import com.datarockets.mnchkn.data.local.PreferencesHelper
import com.datarockets.mnchkn.data.local.SharingHelper
import com.datarockets.mnchkn.data.models.GameStep
import com.datarockets.mnchkn.data.models.Player
import com.datarockets.mnchkn.utils.ColorUtil
import com.github.mikephil.charting.data.Entry
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
open class DataManager
@Inject constructor(private val mDatabaseHelper: DatabaseHelper,
                    private val mPreferencesHelper: PreferencesHelper,
                    private val mSharingHelper: SharingHelper) {

    val preferencesHelper = mPreferencesHelper

    fun getPlayer(playerId: Long): Observable<Player> {
        return mDatabaseHelper.getPlayer(playerId)
    }

    open fun getPlayers(): Observable<List<Player>> {
        return mDatabaseHelper.getPlayers().toList()
    }

    open fun getPlayingPlayers(): Observable<List<Player>> {
        return mDatabaseHelper.getPlayingPlayers().toList()
    }

    open fun getPlayers(sortType: Int): Observable<List<Player>> {
        when (sortType) {
            0 -> return mDatabaseHelper.getPlayedPlayersByLevel().toList()
            1 -> return mDatabaseHelper.getPlayedPlayersByStrength().toList()
            2 -> return mDatabaseHelper.getPlayedPlayersByTotal().toList()
        }
        return Observable.just(null)
    }

    fun addPlayer(playerName: String, position: Int): Observable<Player> {
        val player = Player()
        player.name = playerName
        player.levelScore = 1
        player.strengthScore = 1
        player.color = ColorUtil.generatePlayerAvatarColor()
        player.position = position
        return mDatabaseHelper.setPlayer(player)
    }

    fun changePlayerPosition(movedPlayerId: Long,
                             replacedPlayerId: Long,
                             movedPosition: Int,
                             replacedPosition: Int): Observable<Void> {
        return mDatabaseHelper.changePlayersPositions(movedPlayerId, replacedPlayerId, movedPosition, replacedPosition)
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun clearPlayerStats(): Observable<Void> {
        return mDatabaseHelper.clearPlayerStats()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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

    fun addGameStep(playerId: Long, levelScore: Int, strengthScore: Int): Observable<Void> {
        val gameStep = GameStep()
        gameStep.playerId = playerId
        gameStep.playerLevel = levelScore
        gameStep.playerStrength = strengthScore
        return mDatabaseHelper.setGameStep(gameStep)
    }

    fun generateShareableIntent(): Observable<Intent> {
        return mSharingHelper.generateShareableIntent()
    }

    fun updatePlayersPosition() {
        mDatabaseHelper.updatePlayersPositions().subscribe()
    }

}
